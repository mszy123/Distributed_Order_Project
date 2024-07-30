package com.example.distributedordermatching;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties; 
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OrderProducer {
    private static final String TOPIC_BUY = "buy-orders";
    private static final String TOPIC_SELL = "sell-orders";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) {
        // Properties for configuring the Kafka producer
        Properties props = new Properties();
        // Address of the Kafka broker
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        // Key serializer class
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Value serializer class
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create and configure a new Kafka producer
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            Random random = new Random();
            String[] orderTypes = {"BUY", "SELL"};
            String[] symbols = {"AAPL", "GOOGL", "AMZN", "MSFT"};

            //send 1000 orders
            for (int i = 0; i < 1000; i++) {
                // Randomly select order type, symbol, quantity, and price
                String orderType = orderTypes[random.nextInt(orderTypes.length)];
                String symbol = symbols[random.nextInt(symbols.length)];
                int quantity = random.nextInt(100) + 1; // Quantity between 1 and 100
                double price = 100 + random.nextDouble() * 900; // Price between 100 and 1000

                String order = String.format("%s,%s,%d,%.2f", orderType, symbol, quantity, price);
                ProducerRecord<String, String> record = new ProducerRecord<>(orderType.equals("BUY") ? TOPIC_BUY : TOPIC_SELL, order);
                producer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        System.err.printf("Error sending record: %s%n", exception.getMessage());
                    } else {
                        System.out.printf("Sent order: %s%n", order);
                    }
                });
                //simulate delay
                TimeUnit.MILLISECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("Producer interrupted: %s%n", e.getMessage());
        }
    }
}

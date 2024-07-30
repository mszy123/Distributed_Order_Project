package com.example.distributedordermatching;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;

public class OrderProducer {
    private static final String TOPIC_BUY = "buy-orders";
    private static final String TOPIC_SELL = "sell-orders";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        Random random = new Random();
        String[] orderTypes = {"BUY", "SELL"};
        String[] symbols = {"AAPL", "GOOGL", "AMZN", "MSFT"};

        for (int i = 0; i < 100; i++) {
            String orderType = orderTypes[random.nextInt(orderTypes.length)];
            String symbol = symbols[random.nextInt(symbols.length)];
            int quantity = random.nextInt(100) + 1;
            double price = random.nextDouble() * 1000;

            String order = String.format("%s,%s,%d,%.2f", orderType, symbol, quantity, price);
            ProducerRecord<String, String> record = new ProducerRecord<>(orderType.equals("BUY") ? TOPIC_BUY : TOPIC_SELL, order);
            producer.send(record);
        }

        producer.close();
    }
}

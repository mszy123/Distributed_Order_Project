package com.example.distributedordermatching;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class OrderConsumer {
    private static final String TOPIC_BUY = "buy-orders";
    private static final String TOPIC_SELL = "sell-orders";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "order-consumers";

    public static void main(String[] args) {
        // Properties for configuring the Kafka consumer
        Properties props = new Properties();
        // Address of the Kafka broker
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        // Consumer group ID
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        // Key deserializer class
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Value deserializer class
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        OrderMatchingEngine engine = new OrderMatchingEngine();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // Subscribe to the buy and sell order topics
            consumer.subscribe(Collections.singletonList(TOPIC_BUY));
            consumer.subscribe(Collections.singletonList(TOPIC_SELL));

            //continuously poll for new records
            while (true) {
                //poll for new records with a timeout of 100 milliseconds
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed order: %s%n", record.value());
                    //add the order to the appropriate queue based on the topic
                    if (record.topic().equals(TOPIC_BUY)) {
                        engine.addBuyOrder(record.value());
                    } else if (record.topic().equals(TOPIC_SELL)) {
                        engine.addSellOrder(record.value());
                    }
                    //attempt to match orders
                    engine.matchOrders();
                }
            }
        }
    }
}

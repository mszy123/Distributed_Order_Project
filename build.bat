@echo off

:: Set the classpath to include the Kafka client library, SLF4J libraries, Zstd, and LZ4 libraries
set CLASSPATH=lib\kafka-clients-3.8.0.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-1.7.36.jar;lib\zstd-jni-1.5.6-4.jar;lib\lz4-java-1.8.0.jar;src

:: Compile Java files
javac -d bin -cp %CLASSPATH% src\com\example\distributedordermatching\OrderProducer.java
javac -d bin -cp %CLASSPATH% src\com\example\distributedordermatching\OrderConsumer.java
javac -d bin -cp %CLASSPATH% src\com\example\distributedordermatching\OrderMatchingEngine.java

:: Run OrderProducer
java -cp bin;%CLASSPATH% com.example.distributedordermatching.OrderProducer

:: Uncomment to run OrderConsumer or OrderMatchingEngine
:: java -cp bin;%CLASSPATH% com.example.distributedordermatching.OrderConsumer
:: java -cp bin;%CLASSPATH% com.example.distributedordermatching.OrderMatchingEngine

pause

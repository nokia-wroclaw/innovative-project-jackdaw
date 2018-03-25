package producerManager;

import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProducer extends Thread {

    private static final String TOPIC_NAME = "test";
    private static final String FILE_NAME = "resources/a.us.txt";
    private static final String HOST_NAME = "192.168.99.100:9092";

    private final org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;
    private final Boolean isAsync;

    //FIXME why topic is unused?
    public KafkaProducer(String topic, Boolean isAsync) {
        Properties props = new Properties();
        props.put("bootstrap.servers", HOST_NAME);
        props.put("client.id", "DemoProducer");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
        this.isAsync = isAsync;
    }

    private void sendMessage(String key, String value) {
        long startTime = System.currentTimeMillis();
        if (isAsync) {
            sendMessageAsynchronously(key, value, startTime);
        } else {
            sendMessageSynchronously(key, value);
        }
    }

    private void sendMessageSynchronously(String key, String value) {
        try {
            producer.send(new ProducerRecord<>(TOPIC_NAME, key, value)).get();
            //FIXME use logger
            System.out.println("Sent message: (" + key + ", " + value + ")");
        } catch (InterruptedException | ExecutionException e) {
            //FIXME handle this exception
            e.printStackTrace();
        }
    }

    private void sendMessageAsynchronously(String key, String value, long startTime) {
        producer.send(new ProducerRecord<>(TOPIC_NAME, key),
                new DemoCallBack(startTime, key, value));
    }

    private void sendToKafka() {
        int lineCount = 0;

        //Construct BufferedReader from InputStreamReader
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            //skip first line (names of columns)
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                this.sendMessage(lineCount + "", line);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            //FIXME handle this exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        KafkaProducer kafkaProducer = new KafkaProducer(TOPIC_NAME, false);
        kafkaProducer.sendToKafka();
    }

    @Override
    public void run() {
        //FIXME override this method or don't extend thread
        super.run();
    }
}


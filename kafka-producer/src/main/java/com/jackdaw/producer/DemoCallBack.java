package com.jackdaw.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DemoCallBack implements Callback {

    private static final Logger LOG = LoggerFactory.getLogger(Callback.class);

    private long startTime;
    private String key;
    private String message;

    DemoCallBack(long startTime, String key, String message) {
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    /**
     * A callback method the user can implement to provide asynchronous handling
     * of request completion. This method will be called when the record sent to
     * the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition
     *                  and offset). Null if an error occurred.
     * @param exception The exception thrown during processing of this record. Null if
     *                  no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            LOG.info(String.format("message(%s, %s) sent to partition(%d), offset(%d) in %d ms%n",
                    key, message, metadata.partition(), metadata.offset(), elapsedTime));

        } else {
            LOG.error("", exception);
        }
    }
}

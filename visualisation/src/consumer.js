'use strict';

const kafka = require('kafka-node');
const Consumer = kafka.Consumer;
const Offset = kafka.Offset;
const Client = kafka.Client;
const topic = 'Visualization';
const consumerGroupName = 'visualization-group';

const client = new Client('localhost:2181', consumerGroupName);
const topics = [{topic: topic, partition: 0}];
const options = {autoCommit: false, fetchMaxWaitMs: 1000, fetchMaxBytes: 1024 * 1024};

const consumer = new Consumer(client, topics, options);
const offset = new Offset(client);

consumer.on('message', message => {
    console.log(message);
    switch (message.properties.timeType) {
        case "Expected arrival":
            addLineToMap(message);
            break;
        default:
            addPointToMap(message);
            break;
    }
});

consumer.on('error', err => {
    console.error(`Error: ${err.stack || err.message}`, err);
});

/*
* If consumer get `offsetOutOfRange` event, fetch data from the smallest(oldest) offset
*/
consumer.on('offsetOutOfRange', topic => {
    topic.maxNum = 2;
    offset.fetch([topic], (err, offsets) => {
        if (err) {
            return console.error(err);
        }
        const min = Math.min.apply(null, offsets[topic.topic][topic.partition]);
        consumer.setOffset(topic.topic, topic.partition, min);
    });
});

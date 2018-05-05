'use strict';

const kafka = require('kafka-node');
const Consumer = kafka.Consumer;
const Offset = kafka.Offset;
const Client = kafka.Client;
const argv = require('optimist').argv;
const topic = argv.topic || 'Visualisation';

const client = new Client('localhost:2181');
const topics = [{topic: topic, partition: 1}, {topic: topic, partition: 0}];
const options = {autoCommit: false, fetchMaxWaitMs: 1000, fetchMaxBytes: 1024 * 1024};

const consumer = new Consumer(client, topics, options);
const offset = new Offset(client);

consumer.on('message', function (message) {
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

consumer.on('error', function (err) {
    console.log('error', err);
});

/*
* If consumer get `offsetOutOfRange` event, fetch data from the smallest(oldest) offset
*/
consumer.on('offsetOutOfRange', function (topic) {
    topic.maxNum = 2;
    offset.fetch([topic], function (err, offsets) {
        if (err) {
            return console.error(err);
        }
        const min = Math.min.apply(null, offsets[topic.topic][topic.partition]);
        consumer.setOffset(topic.topic, topic.partition, min);
    });
});

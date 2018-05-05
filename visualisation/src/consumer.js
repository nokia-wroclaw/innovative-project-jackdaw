'use strict';

let kafka = require('kafka-node');
let Consumer = kafka.Consumer;
let Offset = kafka.Offset;
let Client = kafka.Client;
let topic = 'Visualization';
let consumerGroupName = 'visualization-group';

let client = new Client('localhost:2181', consumerGroupName);
let topics = [{topic: topic, partition: 1}];
let options = {autoCommit: false, fetchMaxWaitMs: 1000, fetchMaxBytes: 1024 * 1024};

let consumer = new Consumer(client, topics, options);
let offset = new Offset(client);


consumer.on('message', function (message) {
    console.info(message);
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
    console.error('Error: ${err.stack || err.message}', err);
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
        let min = Math.min.apply(null, offsets[topic.topic][topic.partition]);
        consumer.setOffset(topic.topic, topic.partition, min);
    });
});

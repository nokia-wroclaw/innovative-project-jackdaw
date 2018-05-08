let express = require("express");
let fs = require('fs');
let http = require('http');

const app = express();

const PORT = 3000;
const HOST = '0.0.0.0';

app.use(express.static(__dirname + '/public'));

app.get('/', function (req, res) {
    res.sendFile('index.html');
    res.sendFile('stylesheets/style.css');
    res.sendFile('javascripts/flight-visualization.js');
    res.sendFile('favicon.ico');
});

app.set('port', PORT);
let server = http.createServer(app);
server.listen(PORT);

console.log(`Running on http://${HOST}:${PORT}`);

eval(fs.readFileSync('src/consumer.js') + '');

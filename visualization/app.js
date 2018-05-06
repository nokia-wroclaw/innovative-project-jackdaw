let express = require("express");
let fs = require('fs');
let http = require('http');

let app = express();
let port = 3000;
app.set('port', port);
let server = http.createServer(app);
server.listen(port);
let io = require('socket.io').listen(server);


app.use(express.static(__dirname + '/public'));

app.get('/', function (req, res) {
    res.sendFile('index.html');
    res.sendFile('stylesheets/style.css');
    res.sendFile('javascripts/flight-visualization.js');
    res.sendFile('favicon.ico');
});


console.log("Running at port 3000");

eval(fs.readFileSync('src/consumer.js') + '');

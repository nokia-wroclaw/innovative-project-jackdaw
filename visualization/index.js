let express = require("express");
let app = express();


app.use(express.static(__dirname + '/public'));

app.get('/', function (req, res) {
    res.sendFile('index.html');
    res.sendFile('css/style.css');
    res.sendFile('flight-visualization.js');
    res.sendFile('favicon.ico');
});

app.listen(3000);

console.log("Running at Port 3000");

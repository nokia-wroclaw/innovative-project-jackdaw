const dataPoints = [
    {y: 10, label: "Example flight"},
    {y: 5.4, label: "Another flight"},
    {y: 0.34, label: "Hello flight"},
    {y: 3.14, label: "Nice to meet you, flight"},
    {y: 4.76, label: "Why so late, flight?"}
];

const chart = new CanvasJS.Chart("chartContainer", {
    animationEnabled: true,
    title: {
        text: "Brazilian Flights Delay Chart"
    },
    data: [{
        type: "pie",
        startAngle: 240,
        yValueFormatString: "##0.00\" min\"",
        indexLabel: "{label}",
        dataPoints: dataPoints
    }]
});

window.onload = function () {
    chart.render();
};

// ------
// Add new data on click – test
// todo delete as a real data-source come into existence
let message = "{ \"key\": \"1\", \"value\": [\"4\", \"I'm new\" ] }";

window.onclick = function () {
    let data = JSON.parse(message);
    dataPoints.push({
        y: parseInt(data.value[0]),
        label: data.value[1]
    });
    chart.render();
};
// ------

(function receiveMessage() {
    const socket = io.connect('http://0.0.0.0:3000', {transports: ['websocket', 'flashsocket']});
    socket.on('delay', function (message) {
        console.info('New message received');
        let data = JSON.parse(message);
        console.log(data);
        dataPoints.push({
            y: parseInt(data[0]),
            label: data[1]
        });
        chart.render();
    });
})(jQuery);  // todo

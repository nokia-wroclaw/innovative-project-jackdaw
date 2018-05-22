const dataPoints = [
    {y: 10, label: "Example flight"},
    {y: 5.4, label: "Another flight"},
    {y: 0.84, label: "Hello flight"},
    {y: 3.14, label: "Nice to meet you, flight"},
    {y: 4.76, label: "Why so late, flight?"}
];

const pieChart = new CanvasJS.Chart("pie", {
    animationEnabled: true,
    title: {
        text: "Brazilian Flights Delay Pie Chart"
    },
    data: [{
        type: "pie",
        startAngle: 240,
        yValueFormatString: "##0.00\" min\"",
        indexLabel: "{label}",
        dataPoints: dataPoints
    }]
});

const barChart = new CanvasJS.Chart("bar", {
    animationEnabled: true,
    title: {
        text: "Brazilian Flights Delay Bar Chart"
    },
    axisY: {
        title: "Minutes",
        titleFontColor: "#888",
        titleFontSize: 20
    },
    axisX: {
        title: "Airline",
        titleFontColor: "#888",
        titleFontSize: 20,
        labelFontColor: "dimGrey"
    },
    data: [{
        type: "bar",
        dataPoints: dataPoints
    }]
});


window.onload = function () {
    pieChart.render();
    barChart.render();
};

// ------
// Add new data on click – test
// todo delete as a real data-source come into existence
let message = "{ \"key\": \"1\", \"value\": [ \"1.34\", \"I'm new\" ] }";

window.onclick = function () {
    let data = JSON.parse(message);
    updateChart(data);
};
// ------

(function receiveMessage() {
    const socket = io.connect('http://0.0.0.0:3000', {transports: ['websocket', 'flashsocket']});
    socket.on('delay', function (message) {
        console.debug('New message received');
        let data = JSON.parse(message);
        console.debug(data);
        updateChart(data);
    });
})(jQuery);

function updateChart(data) {
    let updatePosition = -1;
    for (let index in dataPoints) {
        if (dataPoints[index].label === data.value[1]) {
            updatePosition = index;
        }
    }
    console.log("UpdatePosition: " + updatePosition);
    if (updatePosition !== -1) {
        let newY = parseFloat(dataPoints[updatePosition].y) + parseFloat(data.value[0]);
        dataPoints[updatePosition] = {y: newY, label: data.value[1]};
    } else {
        dataPoints.push({
            y: parseFloat(data.value[0]),
            label: data.value[1]
        });
    }
    barChart.render();
    pieChart.render();
}

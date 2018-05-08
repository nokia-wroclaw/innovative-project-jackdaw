let map = L.map('map').setView([-8.930606, -53.250250], 4);

function onEachFeature(feature, layer) {
    let popupContent =
        "<h3> " + feature.properties.companyAerial + "</h3>" +
        "<p class='left'> Origin airport: <strong>" + feature.properties.airportOrigin + "</strong></p>" +
        "<p class='left'> " + feature.properties.timeType + ": <strong>" +
        feature.properties.time.replace("Z", "").replace("T", " ") + "</strong></p>";
    layer.bindPopup(popupContent);
}

function pointToLayer(feature, latlng) {
    return L.circleMarker(latlng, {
        radius: 8,
        fillColor: feature.properties.color,
        color: "#404040",
        weight: 1,
        opacity: 0.8,
        fillOpacity: 0.8
    });
}

function style(feature) {
    return feature.properties && feature.properties.style;
}

function setUpMap() {
    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        id: 'mapbox.streets'
    }).addTo(map);
}

function addPointToMap(geojson) {
    L.geoJSON(geojson, {
        style: style,
        onEachFeature: onEachFeature,
        pointToLayer: pointToLayer,
    }).addTo(map);
}

function addLineToMap(estimatedArrival) {
    let feature = estimatedArrival.features[0];
    let pointA = [feature.geometry.coordinates[0][1], feature.geometry.coordinates[0][0]];
    let pointB = [feature.geometry.coordinates[1][1], feature.geometry.coordinates[1][0]];
    let popupContent = "<p class='left'>" + feature.properties.timeType + ": <strong>" +
        feature.properties.time.replace("Z", "").replace("T", " ") + "</strong></p>";
    L.Polyline.Arc(
        pointA, pointB, {
            color: feature.properties.color,
            weight: 4,
            opacity: 0.75,
            vertices: 100,
            snakingSpeed: 350,
        }).addTo(map)
        .bindPopup(popupContent)
        .snakeIn();
}


setUpMap();


(function receiveMessage() {
    const socket = io.connect('http://192.168.99.100:3000', {transports: ['websocket', 'flashsocket']});
    socket.on('news', function (message) {
        console.info('New message received');
        const flight = JSON.parse(message);
        console.log(flight);
        switch (flight.features[0].properties.timeType) {
            case "Expected arrival":
                addLineToMap(flight);
                break;
            default:
                addPointToMap(flight);
                break;
        }
    });
})(jQuery);

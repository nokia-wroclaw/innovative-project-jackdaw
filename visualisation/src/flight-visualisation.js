const map = L.map('map').setView([-27.81611, -50.32611], 7);

function onEachFeature(feature, layer) {
    const popupContent =
        "<p> " + "Airline: " + feature.properties.companyAerial + "</p>" +
        "<p> " + "Origin airport:  " + feature.properties.airportOrigin + "</p>" +
        "<p> " + feature.properties.timeType + ":  " + feature.properties.time + "</p>";

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

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function addMap() {
    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        id: 'mapbox.streets'
    }).addTo(map);
}

function addEstimatedDeparture() {
    L.geoJSON(estimatedDeparture, {
        style: style,
        onEachFeature: onEachFeature,
        pointToLayer: pointToLayer,
    }).addTo(map);
}

function addRealDeparture() {
    L.geoJSON(realDeparture, {
        style: style,
        onEachFeature: onEachFeature,
        pointToLayer: pointToLayer,
    }).addTo(map);
}

function addEstimatedArrival() {
    L.geoJSON(estimatedArrival, {
        style: style,
        onEachFeature: onEachFeature,
        pointToLayer: pointToLayer,
    }).addTo(map);
}

function addRealArrival() {
    const feature = realArrival.features[0];
    L.Polyline.Arc(
        [feature.geometry.coordinates[0][1], feature.geometry.coordinates[0][0]],
        [feature.geometry.coordinates[1][1], feature.geometry.coordinates[1][0]], {
            color: feature.properties.color,
            weight: 4,
            opacity: 0.75,
            vertices: 100,
        }).addTo(map);
    //todo popout
}


async function demo() {
    addMap();
    await sleep(1000);
    addEstimatedDeparture();
    await sleep(1000);
    addRealDeparture();
    await sleep(1000);
    addEstimatedArrival();
    await sleep(1000);
    addRealArrival();
}

demo();

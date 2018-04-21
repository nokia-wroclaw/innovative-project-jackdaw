const map = L.map('map').setView([-27.81611, -50.32611], 7);

function addMap() {
    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
        '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="http://mapbox.com">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(map);
}


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
        color: feature.properties.color,
        weight: 1,
        opacity: 1,
        fillOpacity: 0.8
    });

}

function style(feature) {
    return feature.properties && feature.properties.style;
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
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
    L.geoJSON(realArrival, {
        style: feature => ({color: feature.properties.color})
    }).addTo(map);
}


async function demo() {
    addMap();
    await sleep(3500);
    addEstimatedDeparture();
    await sleep(3000);
    addRealDeparture();
    await sleep(3000);
    addEstimatedArrival();
    await sleep(3000);
    addRealArrival();
}

demo();

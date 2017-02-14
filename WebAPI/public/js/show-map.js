var map;
var markers = [];
var coordinates = new Object();

function initMap() {
    let coord = document.getElementById('cord').innerText;
    coord = coord.split('(')[1];
    coord = coord.split(')')[0];
    coord = coord.split(', ');
    console.log(coord[0] + "" + coord[1])
    var haightAshbury = { lat: +coord[0], lng: +coord[1] };

    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: haightAshbury,
        mapTypeId: 'terrain'
    });

    // Adds a marker at the center of the map.
    addMarker(haightAshbury);
}

// Adds a marker to the map and push to the array.
function addMarker(loc) {
    deleteMarkers();
    //console.log(loc);
    if (typeof loc.lat === 'function' && typeof loc.lng === 'function') {
        //console.log(loc.lat() + " " + loc.lng());
        coordinates.lat = loc.lat();
        coordinates.lng = loc.lng();
    } else {
        coordinates.lat = loc.lat;
        coordinates.lng = loc.lng;
    }
    var marker = new google.maps.Marker({
        position: loc,
        map: map
    });
    markers.push(marker);
}

// Sets the map on all markers in the array.
function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
    setMapOnAll(null);
}

// Shows any markers currently in the array.
function showMarkers() {
    setMapOnAll(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
    clearMarkers();
    markers = [];
}
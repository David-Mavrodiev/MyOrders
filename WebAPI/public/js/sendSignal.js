var btn = document.getElementById("button-send");
var coordinates = new Object();
var image = "tEST";
document.getElementById('getval').addEventListener('change', readURL, true);

function readURL() {
    var file = document.getElementById("getval").files[0];
    console.log(file);
    var reader = new FileReader();
    reader.onloadend = function() {
        image = reader.result;
        document.getElementById('clock').style.backgroundImage = "url(" + reader.result + ")";
    }
    if (file) {
        reader.readAsDataURL(file);
    } else {}
}
btn.addEventListener('click', function() {
    var firstname = document.getElementById('firstname').value;
    var lastname = document.getElementById('lastname').value;
    var selectOption = document.getElementById('select');
    var description = document.getElementById('description').value;
    var check = document.getElementById('checker');
    var number = document.getElementById('number').value;

    console.log(selectOption.value);

    if (selectOption.value != "Други нарушения") {
        description = selectOption.value;
    }

    if (firstname != "" && lastname != "" && description != "" && check.checked) {
        document.getElementById('firstname').value = "";
        document.getElementById('lastname').value = "";
        document.getElementById('description').value = "";
        document.getElementById('checker').checked = false;
        document.getElementById('number').value = "";

        var body = {
            firstname: firstname,
            lastname: lastname,
            description: description,
            number: number,
            lat: coordinates.lat,
            lng: coordinates.lng,
            image: image
        };

        $.ajax({
            url: "/send",
            type: "post",
            cors: true,
            contentType: "application/json",
            data: JSON.stringify(body),
            success: function(response) {
                var container = document.getElementsByClassName("container")[0];
                container.innerHTML = `<div style=\"padding:10em 0;\" class=\" text-center\"><h2>Благодарим Ви! Сигналът е приет с входящ номер ${response}.</h2></div>`;
            }
        });
    }
});

var map;
var markers = [];

function initMap() {
    var haightAshbury = { lat: 42.7339, lng: 25.4858 };

    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: haightAshbury,
        mapTypeId: 'terrain'
    });

    // This event listener will call addMarker() when the map is clicked.
    map.addListener('click', function(event) {
        addMarker(event.latLng);
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
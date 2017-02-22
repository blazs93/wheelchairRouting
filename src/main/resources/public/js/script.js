var map;
window.MY = {};
MY.markers = []

// Get auth, to add or remove role specific buttons.

$(document).ready(
  function() {
   $.get("getAuth", function(data, status) {
    if (status == "success") {
     var usersForm = document.getElementById('usersForm');
     var loginButton = document.getElementById('loginButton');
     var logoutButton = document.getElementById('logoutButton');
     var registrationButton = document
     .getElementById('registrationButton');
     if (data == "[ROLE_ADMIN]") {
      usersForm.style.display = '';
    } else {
      usersForm.style.display = 'none';
    }

    if (data == "[ROLE_ANONYMOUS]") {
      loginButton.style.display = '';
      logoutButton.style.display = 'none';
      registrationButton.style.display = '';
    } else {
      loginButton.style.display = 'none';
      logoutButton.style.display = '';
      registrationButton.style.display = 'none';
    }
  } else {
   alert(status);
 }
});

 });

function CustomControl(controlDiv, map) {

	// Add POI button
	var addPOIButton = document.createElement('button');
	addPOIButton.setAttribute("class", "controlUI");
	addPOIButton.title = 'Click to add new POI';
	controlDiv.appendChild(addPOIButton);

	var marker = document.createElement('span');
	marker.setAttribute("class", "glyphicon glyphicon-map-marker");
	addPOIButton.appendChild(marker);

	// Setup the click event listeners
  var buttonActive = false;
  google.maps.event.addDomListener(addPOIButton, 'click', function() {

    buttonActive = !buttonActive;

    if(buttonActive){
      addPOIButton.setAttribute("class", "controlUIactive");
      addPOIButton.title = 'Click to disable add POI function';
      google.maps.event.addListener(map, 'click', function(event) {
        $('#myModal').modal('show');
        $('#latitude').val(event.latLng.lat());
        $('#longitude').val(event.latLng.lng());
      });
    } else {
      addPOIButton.setAttribute("class", "controlUI");
      addPOIButton.title = 'Click to add new POI';
      google.maps.event.clearListeners(map, 'click');
    }
  });

}

// init map
function initMap() {

	var centerPosition = {
		lat : 47.38334311,
		lng : 19.215989
	};

	var directionsService = new google.maps.DirectionsService;
	var directionsDisplay = new google.maps.DirectionsRenderer;
	map = new google.maps.Map(document.getElementById('map'), {
		zoom : 15,
		center : centerPosition
	});
	directionsDisplay.setMap(map);

	var onChangeHandler = function() {
		calculateAndDisplayRoute(directionsService, directionsDisplay);
	};

	document.getElementById('search')
 .addEventListener('click', onChangeHandler);

 var addPOIControlDiv = document.createElement('div');
 var POIControl = new CustomControl(addPOIControlDiv, map);

 addPOIControlDiv.index = 1;
 map.controls[google.maps.ControlPosition.LEFT_TOP].push(addPOIControlDiv);

  //Add POIs
  $.get("/allPoi", function(data, status) {
    if (status == "success") {
      var pois = data;
      var latitude;
      var longitude;
      for (var i = 0; i < pois.length; i++) {
        var point = pois[i];
        latitude = point.latitude;
        longitude = point.longitude;
        var myLatLng = {lat: latitude, lng: longitude};
        var marker = new google.maps.Marker({
          position : myLatLng,
          map : map,
          title : point.description,
          accessible: point.accessible
        });
        marker.id=point.latitude+point.longitude;
        MY.markers[i] = marker;
      }
      MY.markers.forEach(function (element){
          google.maps.event.addDomListener(element, 'click', function() {
            $('#markerModal').modal('show');
            $('#markerDescription').text(element.title);
            $('#markerAccessible').text(element.accessible);      
          });
      });
    }
    else{
      alert(status);
    }
  });
}

// Calculate routing
function calculateAndDisplayRoute(directionsService, directionsDisplay) {
	directionsService.route({
		origin : document.getElementById('from').value,
		destination : document.getElementById('to').value,
		travelMode : 'DRIVING'
	}, function(response, status) {
		if (status === 'OK') {
			// directionsDisplay.setDirections(response);

			/*
			 * var line = new google.maps.Polyline({ path:
			 * dirrections.routes[0].overview_path, strokeColor: '#FF0000',
			 * strokeOpacity: 0.5, strokeWeight: 4 });
			 */

      var path = response.routes[0].overview_path;

      var latitude;
      var longitude;
      for (var i = 0; i < path.length; i++) {
        var point = path[i];
        latitude = point.lat();
        longitude = point.lng();
        var marker = new google.maps.Marker({
         position : point,
         map : map,
         title : latitude + " " + longitude
       });
      }
      var myLatlng = {
        lat : latitude,
        lng : longitude
      };
      map.setCenter(myLatlng);

    } else {
     window.alert('Directions request failed due to ' + status);
   }
 });
}
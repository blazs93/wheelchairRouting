var map;
var geocoder;
window.MY = {};
MY.markers = []
var poly;
var platform;
var fromLocation;
var toLocation;
var path;
var directionsService;
var directionsDisplay;
var routePointCounter = 0;
var waypoints = [];

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

function codeAddress() {
  var address = document.getElementById('address').value;
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == 'OK') {
      map.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
          map: map,
          position: results[0].geometry.location
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });
}

function getRouting(){
  var waypoint1;
  var waypoint2;
  geocoder.geocode( { 'address': document.getElementById('from').value}, function(results, status) {
    if (status == 'OK') {
      $.get('/getClosestWaypoint', 
        { latitude: results[0].geometry.location.lat(),
          longitude: results[0].geometry.location.lng()
        }, 
        function(data) {
          waypoint1 = data[0].id;
          directionsService.route({
            origin: document.getElementById('from').value,
            destination: data[0].latitude +', '+ data[0].longitude,
            travelMode: 'DRIVING',
            provideRouteAlternatives: true
          }, function(response, status) {
              if (status === 'OK') {
                var color = '#65b4ce';
                var path = new google.maps.Polyline({
                  path: response.routes[0].overview_path,
                  geodesic: true,
                  strokeColor: color,
                  strokeOpacity: 1.0,
                  strokeWeight: 4
                });

                path.setMap(map);   

              } else {
                window.alert('Directions request failed due to ' + status);
              }
          });
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });

  geocoder.geocode( { 'address': document.getElementById('to').value}, function(results, status) {
    if (status == 'OK') {
      $.get('/getClosestWaypoint', 
        { latitude: results[0].geometry.location.lat(),
          longitude: results[0].geometry.location.lng()
        }, 
        function(data) {
          waypoint2 = data[0].id;
          directionsService.route({
            origin: document.getElementById('to').value,
            destination: data[0].latitude +', '+ data[0].longitude,
            travelMode: 'DRIVING',
            provideRouteAlternatives: true
          }, function(response, status) {
              if (status === 'OK') {
                var color = '#65b4ce';
                var path = new google.maps.Polyline({
                  path: response.routes[0].overview_path,
                  geodesic: true,
                  strokeColor: color,
                  strokeOpacity: 1.0,
                  strokeWeight: 4
                });
                path.setMap(map);

                $.get('/routing', 
                  { waypoint1: waypoint1,
                    waypoint2: waypoint2
                  }, 
                  function(data) {
                      var points = [];

                      for (var i = 0; i<data.length; i++) {
                        points.push(new google.maps.LatLng(data[i].latitude, data[i].longitude));
                      }
                      var color = '#65b4ce';
                      var path = new google.maps.Polyline({
                        path: points,
                        geodesic: true,
                        strokeColor: color,
                        strokeOpacity: 1.0,
                        strokeWeight: 4
                      });
                path.setMap(map);
                  });
              } else {
                window.alert('Directions request failed due to ' + status);
              }
          });
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });
}

//document.getElementById('from').addEventListener('change', onFromChange);
//document.getElementById('to').addEventListener('change', onToChange);
document.getElementById('search').addEventListener('click', getRouting);



 });

//Add POI and route button
function CustomControl(controlDiv, map) {

  //Add POI button
  var addPOIButton = document.createElement('button');
  addPOIButton.setAttribute("class", "controlUI");
  addPOIButton.title = 'Click to add new POI';
  controlDiv.appendChild(addPOIButton);

  //Marker icon
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

   //Add Waypoint button
  var addWaypointButton = document.createElement('button');
  addWaypointButton.setAttribute("class", "controlUI");
  addWaypointButton.title = 'Click to add new waypoint';
  controlDiv.appendChild(addWaypointButton);

  //Waypoint icon
  var waypoint = document.createElement('span');
  waypoint.setAttribute("class", "glyphicon glyphicon-tags");
  addWaypointButton.appendChild(waypoint);

  // Setup the click event listeners
  var waypointButtonActive = false;
  google.maps.event.addDomListener(addWaypointButton, 'click', function() {

    waypointButtonActive = !waypointButtonActive;

    if(waypointButtonActive){
      addWaypointButton.setAttribute("class", "controlUIactive");
      addWaypointButton.title = 'Click to disable add waypoint function';
      google.maps.event.addListener(map, 'click', function(event) {
        //todo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        $('#waypointModal').modal('show');
        $('#waypointLatitude').val(event.latLng.lat());
        $('#waypointLongitude').val(event.latLng.lng());
      });
    } else {
      addWaypointButton.setAttribute("class", "controlUI");
      addWaypointButton.title = 'Click to add new waypoint';
      google.maps.event.clearListeners(map, 'click');
    }
  });


  // Add Route button
  var addRouteButton = document.createElement('button');
  addRouteButton.setAttribute("class", "controlUI");
  addRouteButton.title = 'Click to add new Route';
  controlDiv.appendChild(addRouteButton);

  //Route icon
  var route = document.createElement('span');
  route.setAttribute("class", "glyphicon glyphicon-road");
  addRouteButton.appendChild(route);

  //Route button handler
  var routeButtonActive = false;
  google.maps.event.addDomListener(addRouteButton, 'click', function() {

  	routeButtonActive = !routeButtonActive;

    if(routeButtonActive){
    	routePointCounter = 0;
      addRouteButton.setAttribute("class", "controlUIactive");
      addRouteButton.title = 'Click to disable add Route function';
      initPoly();
      poly.setMap(map);
      map.addListener('click', addLatLng);
      controlDiv.appendChild(addRouteReadyButton);
    } else {
      poly.setMap(null);
      poly = null;
      controlDiv.removeChild(addRouteReadyButton);
      addRouteButton.setAttribute("class", "controlUI");
      addRouteButton.title = 'Click to add new Route';
      google.maps.event.clearListeners(map, 'click');
    }
  });

  //Add route ready button
  var addRouteReadyButton = document.createElement('button');
  addRouteReadyButton.setAttribute("class", "controlUI");
  addRouteReadyButton.title = 'Route ready!';

  var routeReady = document.createElement('span');
  routeReady.setAttribute("class", "glyphicon glyphicon-ok");
  addRouteReadyButton.appendChild(routeReady);

  google.maps.event.addDomListener(addRouteReadyButton, 'click', routeReadyFunc);

}

function routeReadyFunc() {
  routePointCounter = 0;
  /*var path = poly.getPath();
    var points = [];*/
    /*path.b.forEach(function (element){
     var point = {};
     point.lat = element.lat();
     point.lng= element.lng();
     points.push(point);

   });*/
   /*var points = "";
    waypoints.forEach(function (element){
      points = points + " " + element;
   });*/
  //var json = JSON.stringify(points);
  $('#waypoint1').val(waypoints[0]);
  $('#waypoint2').val(waypoints[1]);
  //$('#jsonRoute').text(points);
  $('#routeModal').modal('show');
  waypoints = [];
}

//Handles click events on a map, and adds a new point to the Polyline.
function addLatLng(event) {
  $.get('/getClosestWaypoint', 
    { latitude: event.latLng.lat(),
      longitude: event.latLng.lng()
    }, 
    function(data) {
      waypoints.push(data[0].id);
      var path = poly.getPath();
      path.push(new google.maps.LatLng(data[0].latitude, data[0].longitude));

      routePointCounter++;
      if (routePointCounter == 2) {
        routeReadyFunc();  
      }    
  });
}

//Inits polyline
function initPoly() {
  poly = new google.maps.Polyline({
    strokeColor: '#000000',
    strokeOpacity: 1.0,
    strokeWeight: 3
  });
}


//Inits map
function initMap() {
  geocoder = new google.maps.Geocoder();

  var centerPosition = {
   lat : 47.38334311,
   lng : 19.215989
  };
  directionsService = new google.maps.DirectionsService;
  directionsDisplay = new google.maps.DirectionsRenderer;
  map = new google.maps.Map(document.getElementById('map'), {
    zoom : 15,
    center : centerPosition,
    mapTypeControl: false,
    zoomControl: true,
    zoomControlOptions: {
    	position: google.maps.ControlPosition.RIGHT_TOP
    },
    streetViewControl: false
  });
  directionsDisplay.setMap(map);

  var CustomControlDiv = document.createElement('div');
  CustomControlDiv.setAttribute("class", "customControlDiv");
  var CUSTOMControl = new CustomControl(CustomControlDiv, map);

  CustomControlDiv.index = 1;
  map.controls[google.maps.ControlPosition.LEFT_TOP].push(CustomControlDiv);

  //Add waypoints
  $.get("/allWaypoint", function(data, status) {
    if (status == "success") {
      var wps = data;
      var latitude;
      var longitude;
      for (var i = 0; i < wps.length; i++) {
        var wp = wps[i];
        latitude = wp.latitude;
        longitude = wp.longitude;
        var myLatLng = {lat: latitude, lng: longitude};
    
        var wpCircle = new google.maps.Circle({
            strokeColor: '#65b4ce',
            strokeOpacity: 1,
            strokeWeight: 2,
            fillColor: '#65b4ce',
            fillOpacity: 1,
            map: map,
            center: myLatLng,
            radius: 5
          });
      }
    
    }
    else{
      alert(status);
    }
  });

  //Add POIs
  /*$.get("/allPoi", function(data, status) {
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
  });*/

//Add Routes
  $.get("/allRoutes", function(data, status) {
    if (status == "success") {
      for (var i = 0; i < data.length; i++) {
        //var points = $.parseJSON(data[i].points);
        var points = [];
        points.push(new google.maps.LatLng(data[i].waypoints[0].latitude, data[i].waypoints[0].longitude));
        points.push(new google.maps.LatLng(data[i].waypoints[1].latitude, data[i].waypoints[1].longitude));
        var color;
        if (data[i].accessible == "Accessible") {
          color = '#00dc04';  
        } else if (data[i].accessible == "Not accessible") {
          color = '#ff0000';
        } else {
          color = '#dcce00';
        }

        var path = new google.maps.Polyline({
          path: points,
          geodesic: true,
          strokeColor: color,
          strokeOpacity: 1.0,
          strokeWeight: 9
        });

        path.setMap(map);
      }
    }
    else{
      alert(status);
    }
  });
}
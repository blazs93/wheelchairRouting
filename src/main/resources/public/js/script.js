var map;
window.MY = {};
MY.markers = []
var poly;
var platform;
var fromLocation;
var toLocation;
var path;
var avoidableRoutes = "";

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

//init HERE service
platform = new H.service.Platform({
      app_id: 'lh8V62MV4HZ0iHUvjauK',
      app_code: '1nhHp4XQqnduvlrQSWHG5Q',
      useCIT: true,
      useHTTPS: true
});

//get routing using HERE api
function getRouting(){
  //remove last ';'
  var editedAvoidableRoute = avoidableRoutes.slice(0, avoidableRoutes.length-2);

  var router = platform.getRoutingService(),
    routeRequestParams = {
      mode: 'shortest;pedestrian',
      representation: 'display',
      waypoint0: fromLocation.lat + ',' + fromLocation.lng, 
      waypoint1: toLocation.lat + ',' + toLocation.lng, 
      routeattributes: 'waypoints,summary,shape,legs',
      maneuverattributes: 'direction,action',
      avoidareas: editedAvoidableRoute
    };

    //'47.389778967223435,19.218285083770752;47.38815191810329,19.22083854675293'

  router.calculateRoute(
    routeRequestParams,
    onSuccess,
    onError
  );

  //draw route
  function onSuccess(result) {
  
  if(path != null){
    path.setMap(null);
    path = null;  
  }
    //parse result to drawable format
    var points = result.response.route[0].shape;
    var routes = [];
    points.forEach(function(point) {
      var parts = point.split(',');
      routes.push(
        {
          lat: Number(parts[0]),
          lng: Number(parts[1])
        }
      );
    });
    var color = '#2e6da4';
    
    path = new google.maps.Polyline({
      path: routes,
      geodesic: true,
      strokeColor: color,
      strokeOpacity: 1.0,
      strokeWeight: 4
    });

    path.setMap(map);
  }
  function onError(error) {
    alert('Ooops!');
  }

}

//getRouting();

var onFromChange = function findLocation(){
  var geocodingParams = {
    searchText: document.getElementById('from').value
  };

  var onResult = function(result) {
    if (result.Response.View.length > 0) {
      var locations = result.Response.View[0].Result;
      var position;
      for (i = 0;  i < locations.length; i++) {
        position = {
          lat: locations[i].Location.DisplayPosition.Latitude,
          lng: locations[i].Location.DisplayPosition.Longitude
        };
        fromLocation = position;
      }
    } else {
      alert("Wrong address!");
    }
    
  };

  var geocoder = platform.getGeocodingService();

  geocoder.geocode(geocodingParams, onResult, function(e) {
    alert(e);
    });
};

var onToChange = function findLocation(){
  var geocodingParams = {
    searchText: document.getElementById('to').value
  };

  var onResult = function(result) {
    if (result.Response.View.length > 0) {
      var locations = result.Response.View[0].Result;
      var position;
      for (i = 0;  i < locations.length; i++) {
        position = {
          lat: locations[i].Location.DisplayPosition.Latitude,
          lng: locations[i].Location.DisplayPosition.Longitude
        };
        toLocation = position;
      }
    } else {
      alert("Wrong address!");
    }
  };

  var geocoder = platform.getGeocodingService();

  geocoder.geocode(geocodingParams, onResult, function(e) {
    alert(e);
    });
};

document.getElementById('from').addEventListener('change', onFromChange);
document.getElementById('to').addEventListener('change', onToChange);
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

google.maps.event.addDomListener(addRouteReadyButton, 'click', function() {
	var path = poly.getPath();
  var points = [];
  path.b.forEach(function (element){
   var point = {};
   point.lat = element.lat();
   point.lng= element.lng();
   points.push(point);

 });
  var json = JSON.stringify(points);
  $('#jsonRoute').text(json);
  $('#routeModal').modal('show');
});

}

//Handles click events on a map, and adds a new point to the Polyline.
function addLatLng(event) {
  var path = poly.getPath();
  path.push(event.latLng);
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

  var centerPosition = {
   lat : 47.38334311,
   lng : 19.215989
  };
  var directionsService = new google.maps.DirectionsService;
  var directionsDisplay = new google.maps.DirectionsRenderer;
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

//Add Routes
  $.get("/allRoutes", function(data, status) {
    if (status == "success") {
      for (var i = 0; i < data.length; i++) {
        var points = $.parseJSON(data[i].points);
        var color;
        if (data[i].accessible == "Accessible") {
          color = '#00dc04';  
        } else if (data[i].accessible == "Not accessible") {
          color = '#ff0000';
          var counter = 1;
          var previous;
          points.forEach(function(point){
            /*if (counter % 2 == 0){
              avoidableRoutes += point.lat+","+point.lng+"!";
            } else {
              avoidableRoutes += point.lat+","+point.lng+";";
            }*/
             if (counter % 2 == 0){
              if(previous.lat <= point.lat) {
                avoidableRoutes += point.lat+","+previous.lng+";"+previous.lat+","+point.lng+"!";
              }
              else {
                avoidableRoutes += previous.lat+","+previous.lng+";"+point.lat+","+point.lng+"!";
              }
            } else {
              //avoidableRoutes += point.lat+","+point.lng+";";
            }
            counter = counter + 1;
            previous = point;
            
          }); 
        } else {
          color = '#dcce00';
        }

        var path = new google.maps.Polyline({
          path: points,
          geodesic: true,
          strokeColor: color,
          strokeOpacity: 1.0,
          strokeWeight: 2
        });

        path.setMap(map);
      }
    }
    else{
      alert(status);
    }
  }); 
}
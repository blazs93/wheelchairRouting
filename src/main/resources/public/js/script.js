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

//roles
var ROLE_ADMIN = "[ROLE_ADMIN]";
var ROLE_USER = "[ROLE_USER]";
var ROLE_ANONYMOUS = "[ROLE_ANONYMOUS]";

//service errors
var DIRECTIONS_ERROR = "Útvonal kérés hiba: ";
var GEOCODE_ERROR = "Geo code hiba: ";

//POI
var ADD_NEW_POI = "Új POI hozzáadása";
var CANCEL_ADD_NEW_POI = "POI hozzáadás elvetése";

//accessible
var ACCESSIBLE = "accessible";
var NOT_ACCESSIBLE = "not accessible";
var NOT_DEFINED = "not defined";

var ACCESSIBLE_HUN = "Állapot: Bejárható";
var NOT_ACCESSIBLE_HUN = "Állapot: Nem bejárható";
var NOT_DEFINED_HUN = "Állapot: Nem meghatározott";
// Get auth, to add or remove role specific buttons.
$(document).ready(
  function() {
   $('#spinner').show();
   $.get("getAuth", function(data, status) {
    if (status == "success") {
     var usersForm = document.getElementById('usersForm');
     var loginButton = document.getElementById('loginButton');
     var logoutButton = document.getElementById('logoutButton');
     var registrationButton = document
     .getElementById('registrationButton');
     if (data == ROLE_ADMIN) {
      usersForm.style.display = '';
      addMapControls();
    } else if (data == ROLE_USER) {
      addMapControls();
      usersForm.style.display = 'none';
    } else{
      usersForm.style.display = 'none';
    }

    if (data == ROLE_ANONYMOUS) {
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
      alert(GEOCODE_ERROR + status);
    }
  });
}

var path1;
var path2;
var route;
var fromMarker;
var destinationMarker;
var markers = [];

function resetRouting(){
  if(path1 != null)
    path1.setMap(null);
  if(path2 != null)
    path2.setMap(null);
  if(route != null)
    route.setMap(null);
  if(markers[0] != null)
    markers[0].setMap(null); 
  if(markers[1] != null)
    markers[1].setMap(null);

  path1=null;
  path2=null;
  route=null;
  markers = [];
  //fromMarker = null;
  //destinationMarker = null;

}

function getRouting(){
  $('#spinner').show();
  resetRouting();
  var waypoint1;
  var waypoint2;
  geocoder.geocode( { 'address': document.getElementById('from').value}, function(results, status) {
    if (status == 'OK') {
      var myLatLng = {lat: results[0].geometry.location.lat(), lng: results[0].geometry.location.lng()};
      var fromMarker = new google.maps.Marker({
        position : myLatLng,
        map : map,
        icon: "marker/from.png",
        title : document.getElementById('from').value,
        });
      markers.push(fromMarker);
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
                path1 = new google.maps.Polyline({
                  path: response.routes[0].overview_path,
                  geodesic: true,
                  strokeColor: color,
                  strokeOpacity: 1.0,
                  strokeWeight: 4
                });

                path1.setMap(map);

                geocoder.geocode( { 'address': document.getElementById('to').value}, function(results, status) {
                if (status == 'OK') {
                  var myLatLng = {lat: results[0].geometry.location.lat(), lng: results[0].geometry.location.lng()};
                  var destinationMarker = new google.maps.Marker({
                    position : myLatLng,
                    map : map,
                    icon: "marker/destination.png",
                    title : document.getElementById('to').value,
                    });
                  markers.push(destinationMarker);
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
                            path2 = new google.maps.Polyline({
                              path: response.routes[0].overview_path,
                              geodesic: true,
                              strokeColor: color,
                              strokeOpacity: 1.0,
                              strokeWeight: 4
                            });
                            path2.setMap(map);

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
                                  route = new google.maps.Polyline({
                                    path: points,
                                    geodesic: false,
                                    strokeColor: color,
                                    strokeOpacity: 1.0,
                                    strokeWeight: 4
                                  });
                                  route.setMap(map);
                              });
                          } else {
                            window.alert(DIRECTIONS_ERROR + status);
                          }
                      });
                  });
                } else {
                  alert(GEOCODE_ERROR + status);
                }
              });  


              } else {
                window.alert(DIRECTIONS_ERROR + status);
              }
          });
      });
    } else {
      alert(GEOCODE_ERROR + status);
    }
    $('#spinner').hide();
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
  addPOIButton.title = ADD_NEW_POI;
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
      addPOIButton.title = CANCEL_ADD_NEW_POI;
      google.maps.event.addListener(map, 'click', function(event) {
        $('#myModal').modal('show');
        $('#latitude').val(event.latLng.lat());
        $('#longitude').val(event.latLng.lng());
      });
    } else {
      addPOIButton.setAttribute("class", "controlUI");
      addPOIButton.title = ADD_NEW_POI;
      google.maps.event.clearListeners(map, 'click');
    }
  });

   //Add Waypoint button
  var addWaypointButton = document.createElement('button');
  addWaypointButton.setAttribute("class", "controlUI");
  addWaypointButton.title = 'Új kereszteződés hozzáadása';
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
      addWaypointButton.title = 'Kereszteződés hozzáadás elvetése';
      google.maps.event.addListener(map, 'click', function(event) {
        //todo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        $('#waypointModal').modal('show');
        $('#waypointLatitude').val(event.latLng.lat());
        $('#waypointLongitude').val(event.latLng.lng());
      });
    } else {
      addWaypointButton.setAttribute("class", "controlUI");
      addWaypointButton.title = 'Új kereszteződés hozzáadása';
      google.maps.event.clearListeners(map, 'click');
    }
  });


  // Add Route button
  var addRouteButton = document.createElement('button');
  addRouteButton.setAttribute("class", "controlUI");
  addRouteButton.title = 'Új út hozzáadása';
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
      addRouteButton.title = 'Út hozzáadás elvetése';
      initPoly();
      poly.setMap(map);
      map.addListener('click', addLatLng);
      controlDiv.appendChild(addRouteReadyButton);
    } else {
      poly.setMap(null);
      poly = null;
      controlDiv.removeChild(addRouteReadyButton);
      addRouteButton.setAttribute("class", "controlUI");
      addRouteButton.title = 'Új út hozzáadása';
      google.maps.event.clearListeners(map, 'click');
    }
  });

  //Add route ready button
  var addRouteReadyButton = document.createElement('button');
  addRouteReadyButton.setAttribute("class", "controlUI");
  addRouteReadyButton.title = 'Út kész!';

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


function addMapControls(){
    var CustomControlDiv = document.createElement('div');
    CustomControlDiv.setAttribute("class", "customControlDiv");
    var CUSTOMControl = new CustomControl(CustomControlDiv, map);

    CustomControlDiv.index = 1;
    map.controls[google.maps.ControlPosition.LEFT_TOP].push(CustomControlDiv);
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
    zoom : 18,
    center : centerPosition,
    mapTypeControl: false,
    zoomControl: true,
    zoomControlOptions: {
    	position: google.maps.ControlPosition.RIGHT_TOP
    },
    streetViewControl: false
  });
  directionsDisplay.setMap(map);

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
  $.get("/allPoi", function(data, status) {
    if (status == "success") {
      var pois = data;
      var latitude;
      var longitude;
      for (var i = 0; i < pois.length; i++) {
        var point = pois[i];
        latitude = point.latitude;
        longitude = point.longitude;
        var icon = "";
        if (point.accessible == ACCESSIBLE) {
              icon = "marker/accessible.png";
          } else if (point.accessible == NOT_ACCESSIBLE) {
              icon = "marker/not_accessible.png";
          } else {
              icon = "marker/not_defined.png";
          }
        var myLatLng = {lat: latitude, lng: longitude};
        var marker = new google.maps.Marker({
          position : myLatLng,
          map : map,
          icon: icon,
          title : point.description,
          accessible: point.accessible
        });
        marker.id=point.latitude+point.longitude;
        MY.markers[i] = marker;
      }
      MY.markers.forEach(function (element){
        google.maps.event.addDomListener(element, 'click', function() {
          $('#markerModal').modal('show');
          var acc = "";
          if (element.accessible == ACCESSIBLE) {
              acc = ACCESSIBLE_HUN;
          } else if (element.accessible == NOT_ACCESSIBLE) {
              acc = NOT_ACCESSIBLE_HUN;
          } else {
              acc = NOT_DEFINED_HUN;
          }
          $('#markerDescription').text(element.title);
          $('#markerAccessible').text(acc);      
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
        //var points = $.parseJSON(data[i].points);
        var points = [];
        points.push(new google.maps.LatLng(data[i].waypoints[0].latitude, data[i].waypoints[0].longitude));
        points.push(new google.maps.LatLng(data[i].waypoints[1].latitude, data[i].waypoints[1].longitude));
        var color;
        if (data[i].accessible == ACCESSIBLE) {
          color = '#00dc04';  
        } else if (data[i].accessible == NOT_ACCESSIBLE) {
          color = '#ff0000';
        } else {
          color = '#dcce00';
        }

        var path = new google.maps.Polyline({
          path: points,
          geodesic: true,
          strokeColor: color,
          strokeOpacity: 0.05,
          strokeWeight: 10
        });

        path.setMap(map);
      }
      $('#spinner').hide();
    }
    else{
      alert(status);
      $('#spinner').hide();
    }
  });
}
/*var map;
var platform;

$(document).ready(
  function() {
  	platform = new H.service.Platform({
  		app_id: 'lh8V62MV4HZ0iHUvjauK',
  		app_code: '1nhHp4XQqnduvlrQSWHG5Q',
  		useCIT: true,
  		useHTTPS: true
	});
	var defaultLayers = platform.createDefaultLayers();

	map = new H.Map(document.getElementById('map'),
  	defaultLayers.normal.map,{
  		center: {
  			lat : 47.38334311,
   			lng : 19.215989
   		},
  		zoom: 16
  	});

	var behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));

	var ui = H.ui.UI.createDefault(map, defaultLayers);

  document.getElementById('searchBarButton').onclick = findLocation;
  document.getElementById('searchRoute').onclick = findParameters;
});

var marker;

function findLocation(){
  var geocodingParams = {
    searchText: document.getElementById('searchBar').value
  };

  var onResult = function(result) {
    if(marker != null) {
      map.removeObject(marker);
    }
    var locations = result.Response.View[0].Result,
      position;
      // Add a marker for each location found
    for (i = 0;  i < locations.length; i++) {
      position = {
        lat: locations[i].Location.DisplayPosition.Latitude,
        lng: locations[i].Location.DisplayPosition.Longitude
      };
      marker = new H.map.Marker(position);
      map.addObject(marker);
    }
  };

  var geocoder = platform.getGeocodingService();

  geocoder.geocode(geocodingParams, onResult, function(e) {
    alert(e);
    });
}

var fromRoute;
var toRoute;

function findParameters(){
  var geocodingParams1 = {
    searchText: document.getElementById('fromRoute').value
  };

  var onResult1 = function(result) {
    var locations = result.Response.View[0].Result;
    
    for (i = 0;  i < locations.length; i++) {
      fromRoute = {
        lat: locations[i].Location.DisplayPosition.Latitude,
        lng: locations[i].Location.DisplayPosition.Longitude
      };
    }
  };

  var geocoder1 = platform.getGeocodingService();

  geocoder1.geocode(geocodingParams1, onResult1, function(e) {
    alert(e);
    });

  var geocodingParams2 = {
    searchText: document.getElementById('toRoute').value
  };

  var onResult2 = function(result) {
    var locations = result.Response.View[0].Result;
    
    for (i = 0;  i < locations.length; i++) {
      fromRoute = {
        lat: locations[i].Location.DisplayPosition.Latitude,
        lng: locations[i].Location.DisplayPosition.Longitude
      };
    }
  };

  var geocoder2 = platform.getGeocodingService();

  geocoder2.geocode(geocodingParams2, onResult2, function(e) {
    alert(e);
    });

  getRouting();
}

function getRouting(){
  var router = platform.getRoutingService(),
    routeRequestParams = {
      mode: 'shortest;pedestrian',
      representation: 'display',
      waypoint0: fromRoute.lat + ',' + fromRoute.lng, 
      waypoint1: toRoute.lat + ',' + toRoute.lng, 
      routeattributes: 'waypoints,summary,shape,legs',
      maneuverattributes: 'direction,action'
    };

  router.calculateRoute(
    routeRequestParams,
    onSuccess,
    onError
  );

  function onSuccess(result) {
  }
  function onError(error) {
    alert('Ooops!');
  }

} */
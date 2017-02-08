var map;

      function initMap() {
        var directionsService = new google.maps.DirectionsService;
        var directionsDisplay = new google.maps.DirectionsRenderer;
        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 7,
          center: {lat: 41.85, lng: -87.65}
        });
        directionsDisplay.setMap(map);

        var onChangeHandler = function() {
          calculateAndDisplayRoute(directionsService, directionsDisplay);
        };

       
       /* document.getElementById('start').addEventListener('change', onChangeHandler);
        document.getElementById('end').addEventListener('change', onChangeHandler);
        document.getElementById('go').addEventListener('click', onChangeHandler);*/
      }

   


      function calculateAndDisplayRoute(directionsService, directionsDisplay) {
        directionsService.route({
          origin: document.getElementById('start').value,
          destination: document.getElementById('end').value,
          travelMode: 'DRIVING'
        }, function(response, status) {
          if (status === 'OK') {
            //directionsDisplay.setDirections(response);

            /*var line = new google.maps.Polyline({
              path: dirrections.routes[0].overview_path,
              strokeColor: '#FF0000',
              strokeOpacity: 0.5,
              strokeWeight: 4
            });*/

            var path = response.routes[0].overview_path;

            var latitude;
            var longitude;
            for (var i = 0; i < path.length; i++) {
              var point = path[i];
              latitude = point.lat();
              longitude = point.lng();
              var marker = new google.maps.Marker({
                position: point,
                map: map,
                title: latitude +" "+longitude
              });         
            }
            var myLatlng = {lat: latitude, lng: longitude};
            map.setCenter(myLatlng);
  
          } else {
            window.alert('Directions request failed due to ' + status);
          }
        });
      }
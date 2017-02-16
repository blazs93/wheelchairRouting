var map;

$(document).ready(function() {
    $.get("getAuth", function(data, status) {
        if (status == "success") {
          var usersForm = document.getElementById('usersForm');
          var loginButton = document.getElementById('loginButton');
          var logoutButton = document.getElementById('logoutButton');
          var registrationButton = document.getElementById('registrationButton');
          if (data == "[ROLE_ADMIN]"){
            usersForm.style.display = '';
          } else{
            usersForm.style.display = 'none';
          }

          if (data == "[ROLE_ANONYMOUS]"){
              loginButton.style.display='';
              logoutButton.style.display='none';
              registrationButton.style.display='';
          } else{
              loginButton.style.display='none';
              logoutButton.style.display='';
              registrationButton.style.display='none';
          }
          
          
        }
        else{
            alert(status);
        }
    });
    
}) ;

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

       
       /* document.getElementById('from').addEventListener('change', onChangeHandler);
        document.getElementById('to').addEventListener('change', onChangeHandler);*/
        document.getElementById('search').addEventListener('click', onChangeHandler);
      }

   


      function calculateAndDisplayRoute(directionsService, directionsDisplay) {
        directionsService.route({
          origin: document.getElementById('from').value,
          destination: document.getElementById('to').value,
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
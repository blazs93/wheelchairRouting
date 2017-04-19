
window.MY = {};
MY.users = [];
MY.pois = [];
MY.routes = [];

$(document).ready(function() {
    $.get("/user", function(data, status) {
        if (status == "success") {
        	MY.users = data;
            var table = document.getElementById('userTableBody');
            for(i = 0;i<data.length; i++ ){
                var row = table.insertRow(-1);
                
                var cell1 = row.insertCell(-1);
                cell1.innerHTML = data[i][0];
                
                var checkbox = document.createElement("INPUT");
                checkbox.setAttribute("class", "customCheckbox");
                checkbox.setAttribute("type", "checkbox");
                checkbox.setAttribute("aria-label",data[i][0] + "activation")
                checkbox.id = data[i][0];
                checkbox.checked = data[i][1];
             
                var t = document.createTextNode(data[i][1]);
                checkbox.appendChild(t);       

                var cell2 = row.insertCell(-1);
                cell2.appendChild(checkbox);
                         
            }
            
            MY.users.forEach(function(element) {
            	 document.getElementById(element[0]).onclick = function(){
                    var active = !element[1];
                    element[1] = !element[1];
                    var username = element[0];
                    $.get("/updateUser", 
                        { "active": active,
                          "username":username
                        }, function(data, status) {
                        if (status == "success") {
                            //$('#successModal').modal('show');
                        }
                        else{
                            alert(status);
                        }   
                    });
                }
            });
        }
        else{
            alert(status);
        }
    });
    
    $.get("/allPoi", function(data, status) {
    	if (status == "success") {
        	MY.pois = data;
            var table = document.getElementById('poiTableBody');
            for(i = 0;i<data.length; i++ ){
                var row = table.insertRow(-1);
                
                //name
                var cell1 = row.insertCell(-1);
                cell1.innerHTML = data[i].name;
                
                //active checkbox
                var checkbox = document.createElement("INPUT");
                checkbox.setAttribute("class", "customCheckbox");
                checkbox.setAttribute("type", "checkbox");
                checkbox.setAttribute("aria-label",data[i].name + "activation")
                checkbox.id = data[i].poiId + "PoiActive";
                checkbox.checked = data[i].active;
             
                var t = document.createTextNode(data[i].active);
                checkbox.appendChild(t);       

                var cell2 = row.insertCell(-1);
                cell2.appendChild(checkbox);
                
                //edit button
                var editButton = document.createElement('button');
                editButton.id = data[i].poiId+"PoiEdit";
                
                var p = document.createElement('p');
                p.setAttribute("class", "hidden");
                p.setAttribute("aria-hidden", "true");
                p.innerHTML = data[i].poiId+"PoiEdit";
                editButton.appendChild(p);
    
                var editSpan = document.createElement('span');
                editSpan.setAttribute("class", "glyphicon glyphicon-pencil");
                editButton.appendChild(editSpan);
                
                var cell3 = row.insertCell(-1);
                cell3.appendChild(editButton);
                
                //delete button
                var button = document.createElement('button');
                button.id = data[i].poiId+"PoiDelete";
                
                var p = document.createElement('p');
                p.setAttribute("class", "hidden");
                p.setAttribute("aria-hidden", "true");
                p.innerHTML = data[i].poiId+"PoiDelete";
                button.appendChild(p);
    
                var span = document.createElement('span');
                span.setAttribute("class", "glyphicon glyphicon-trash");
                button.appendChild(span);
                
                var cell4 = row.insertCell(-1);
                cell4.appendChild(button);
                         
            }
            
            MY.pois.forEach(function(element) {
            	document.getElementById(element.poiId+"PoiDelete").onclick = function(){
                    $.get("/deletePoi", 
                        { 
                    		"id": element.poiId
                        }, function(data, status) {
                        if (status == "success") {
                        	window.location = "/admin.html";
                        }
                        else{
                            alert(status);
                        }   
                    });
                }
            	document.getElementById(element.poiId+"PoiEdit").onclick = function(){
            		$('#editPOIModal').modal({backdrop: "static"});
            		$('#latitude').val(element.latitude);
                    $('#longitude').val(element.longitude);
                    $('#name').val(element.name);
                    $('#description').val(element.description);
                    $('#acc').val(element.accessible);
                    $('#poiId').val(element.poiId);
                }
            	 document.getElementById(element.poiId+"PoiActive").onclick = function(){
            		 var active = !element.active;
                     element.active = !element.active;
                     $.get("/activatePoi", 
                         { "active": active,
                           "poiId":element.poiId
                         }, function(data, status) {
                         if (status == "success") {
                             //$('#successModal').modal('show');
                         }
                         else{
                             alert(status);
                         }   
                     });
                 }
            });
        }
        else{
            alert(status);
        }
   });
    $.get("/allRoutes", function(data, status) {
    	if (status == "success") {
        	MY.routes = data;
            var table = document.getElementById('routeTableBody');
            for(i = 0;i<data.length; i++ ){
                var row = table.insertRow(-1);
                
                //id
                var cell1 = row.insertCell(-1);
                cell1.innerHTML = data[i].id;
                
                //active checkbox
                var checkbox = document.createElement("INPUT");
                checkbox.setAttribute("class", "customCheckbox");
                checkbox.setAttribute("type", "checkbox");
                heckbox.setAttribute("aria-label",data[i].id + "activation")
                checkbox.id = data[i].id + "RouteActive";
                checkbox.checked = data[i].active;
             
                var t = document.createTextNode(data[i].active);
                checkbox.appendChild(t);       

                var cell2 = row.insertCell(-1);
                cell2.appendChild(checkbox);
                
                //edit button
                var editButton = document.createElement('button');
                editButton.id = data[i].id+"RouteEdit";
                
                var p = document.createElement('p');
                p.setAttribute("class", "hidden");
                p.setAttribute("aria-hidden", "true");
                p.innerHTML = data[i].id+"RouteEdit";
                editButton.appendChild(p);
    
                var editSpan = document.createElement('span');
                editSpan.setAttribute("class", "glyphicon glyphicon-pencil");
                editButton.appendChild(editSpan);
                
                var cell3 = row.insertCell(-1);
                cell3.appendChild(editButton);
                
                //delete button
                var button = document.createElement('button');
                button.id = data[i].id+"RouteDelete";
                
                var p = document.createElement('p');
                p.setAttribute("class", "hidden");
                p.setAttribute("aria-hidden", "true");
                p.innerHTML = data[i].id+"RouteDelete";
                button.appendChild(p);
    
                var span = document.createElement('span');
                span.setAttribute("class", "glyphicon glyphicon-trash");
                button.appendChild(span);
                
                var cell4 = row.insertCell(-1);
                cell4.appendChild(button);
                         
            }
            
            MY.routes.forEach(function(element) {
            	document.getElementById(element.id+"RouteDelete").onclick = function(){
                    $.get("/deleteRoute", 
                        { 
                    		"id": element.id
                        }, function(data, status) {
                        if (status == "success") {
                        	window.location = "/admin.html";
                        }
                        else{
                            alert(status);
                        }   
                    });
                }
            	document.getElementById(element.id+"RouteEdit").onclick = function(){
            		$('#editRouteModal').modal({backdrop: "static"});
                    $('#routeAccessible').val(element.accessible);
                    $('#routeId').val(element.id);
                }
            	 document.getElementById(element.id+"RouteActive").onclick = function(){
            		 var active = !element.active;
                     element.active = !element.active;
                     $.get("/activateRoute", 
                         { "active": active,
                           "id":element.id
                         }, function(data, status) {
                         if (status == "success") {
                             //$('#successModal').modal('show');
                         }
                         else{
                             alert(status);
                         }   
                     });
                 }
            });
        }
        else{
            alert(status);
        }
   });
}) ;





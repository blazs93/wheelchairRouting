
window.MY = {};
MY.users = [];

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
                checkbox.setAttribute("type", "checkbox");
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
                            $('#successModal').modal('show');
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





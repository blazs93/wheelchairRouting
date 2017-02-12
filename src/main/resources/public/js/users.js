
window.MY = {};
MY.users = [];

$(document).ready(function() {
    $.get("request/all", function(data, status) {
        if (status == "success") {
        	MY.users = data;
            var table = document.getElementById('userTableBody');
            for(i = 0;i<data.length; i++ ){
                var row = table.insertRow(-1);
                
                var cell1 = row.insertCell(-1);
                cell1.innerHTML = data[i].username;
                
                var btn = document.createElement("BUTTON");
                btn.id = data[i].username;
                
                var t = document.createTextNode(data[i].username);
                btn.appendChild(t);       

                var cell2 = row.insertCell(-1);
                cell2.appendChild(btn);
                         
            }
            
            MY.users.forEach(function(element) {
            	 document.getElementById(element.username).onclick = function(){alert(element.username);}
            });
        }
        else{
            alert(status);
        }
    });
    
}) ;



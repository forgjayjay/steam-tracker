let chart = document.getElementById('chart-1');
chart.style.disabled = true;
chart.style.opacity = 0;
window.addEventListener('load', showChart());
var game;
var newRow;
var newCell;
var jsonData;
var params = new URLSearchParams(document.location.search);
var link;
var th;
var td;
var data;
function prepareLink(){
    params = new URLSearchParams(document.location.search);
    link = '/api/v1/my-games?userID=' + params.get('userID');
    console.log(link);
}

function showChart(){
    prepareLink();
    const tbodyRef = document.getElementById('chart-1').getElementsByTagName('tbody')[0];
    $.ajax(link, {
        type: "GET",
        success: function(data) {
            jsonData = JSON.parse(data);
            
            if(jsonData.games.length > 0){
                for (var i = 0; i < jsonData.games.length; i++) {
                    game = jsonData.games[i];
                    
                    newRow = tbodyRef.insertRow();
                    newCell = newRow.insertCell();
                    newCell.style = '--size: ' + (game.minutes_played_today/1440);

                    th = document.createElement('th');
                    th.innerHTML = game.name;
                    th.style.whiteSpace = "nowrap";

                    td = document.createElement('td');
                    data = document.createElement('span');
                    data.className = 'data';
                    data.innerHTML = game.minutes_played_today;
                    td.style = 'background-color:transparent';
                    td.appendChild(data);
                    
                    newCell.appendChild(th);

                    if (game.minutes_played_today > 0) {
                        newCell.appendChild(td);
                    }
                }
                chart.style.disabled = false;
                chart.style.opacity = 1;
                document.getElementById('chart-1').style = 'max-width: ' + jsonData.games.length*10 + 'vw';
            }else{
                
                
                pElem = document.createElement('p');
                pElem.style.fontSize = "4rem";
                pElem.innerHTML = "Oops! No games found.";
                document.getElementById("mainContainer").appendChild(pElem);
            }
        },
        error: function(data) {
            console.log('error');
        },
        done: function(data) {
            console.log('done');
        }
    });
    
}
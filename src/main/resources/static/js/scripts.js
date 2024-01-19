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

var map = {};

function showChart(){
    prepareLink();
    const tbodyRef = document.getElementById('chart-1').getElementsByTagName('tbody')[0];
    $.ajax(link, {
        type: 'GET',
        success: function(data) {
            jsonData = JSON.parse(data);
            console.log(jsonData.games)
            if(jsonData.games.length > 0){
                for (var i = 0; i < jsonData.games.length; i++) {
                    game = jsonData.games[i];
                    
                    newRow = tbodyRef.insertRow();
                    newCell = newRow.insertCell();
                    newCell.style = '--size: ' + (game.minutes_played_today/60/24);

                    th = document.createElement('th');
                    th.innerHTML = game.name;
                    th.style.fontSize = "1.2rem";
                    th.style.whiteSpace = "nowrap";

                    td = document.createElement('td');
                    //td.id = 'column_'+i;
                    
                    data = document.createElement('span');
                    data.id = 'data_'+i;
                    data.className = 'data';

                    map[i] = '';

                    if(game.minutes_played_today >= 30){
                        if(game.minutes_played_today >= 60){
                            data.innerHTML = parseInt(game.minutes_played_today/60,10) + ' hours<br>' + ((game.minutes_played_today%60 > 0) ? game.minutes_played_today%60 + ' minute(s)' : '');
                        } else{
                            data.innerHTML = game.minutes_played_today +' minutes';
                        }
                        map[i] = game.minutes_played_today + ' minutes';
                    }
                    td.style = 'background-color:transparent; text-align: center';
                    td.appendChild(data);
                    
                    newCell.appendChild(th);

                    if (game.minutes_played_today > 0) {
                        newCell.appendChild(td);
                    }
                }
                chart.style.disabled = false;
                document.querySelector('#min-to-hour').style.opacity = 1;
                chart.style.opacity = 1;
                document.getElementById('chart-1').style = 'max-width: ' + jsonData.games.length*10 + 'vw';
            }else{
                
                
                pElem = document.createElement('p');
                pElem.style.fontSize = "4rem";
                pElem.style.alignSelf = "center";
                pElem.style.position = "absolute";
                pElem.innerHTML = "No information available";
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

function min_to_hour() {
    var tds = document.querySelectorAll('tbody tr'), i;
    for(i = 0; i < tds.length; ++i) {
        data_seg = document.querySelector('#data_'+i);
        let temp = data_seg.innerHTML;
        data_seg.innerHTML = map[i];
        map[i] = temp;
        console.log(temp)
    }
}
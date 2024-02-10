let chart = document.getElementById('chart-1');
chart.style.disabled = true;
chart.style.opacity = 0;
window.addEventListener('load', showChart());

var game, link, th, td, data, newRow, newCell, jsonData;
var params = new URLSearchParams(document.location.search);



function prepareLink(){
    params = new URLSearchParams(document.location.search);
    link = '/api/v1/my-games?userID=' + params.get('userID');
    console.log(link);
}

var map = {};

function showChart(){
    prepareLink();
    const tbodyRef = chart.getElementsByTagName('tbody')[0];

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
                    newCell.style = '--size: ' + (game.minutes_played_today/60/24 + 0.02);

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
                            let hours = parseInt(game.minutes_played_today/60,10);
                            let minutes = game.minutes_played_today%60;
                            data.innerHTML = (hours + ' hour'+ (hours > 1 ? 's' : '')) + '<br>' + (minutes > 0 ? (minutes + ' minute' + (minutes > 1 ? 's' : '')) : '');
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

                postLoad();

            }else{
                document.querySelector('.lds-ring').style.opacity = 0;
                pElem = document.createElement('p');
                pElem.style.fontSize = "4rem";
                pElem.style.alignSelf = "center";
                pElem.style.position = "absolute";
                pElem.innerHTML = "No information available";              
                document.getElementById("main-container").appendChild(pElem);
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

function postLoad() {
    chart.style.disabled = false;
    chart.style.opacity = 1;
    chart.style = 'max-width: ' + jsonData.games.length*10 + 'vw';
    document.querySelector('.lds-ring').style.opacity = 0;
    document.querySelector('.lds-ring').style.position = "absolute";
    document.querySelector('#min-to-hour').style.opacity = 1;
    document.querySelector('.tooltip').style.opacity = 1;
    document.querySelector('.tooltiptext').style.opacity = 1;
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
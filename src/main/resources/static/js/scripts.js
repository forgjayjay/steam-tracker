window.addEventListener('load', showChart());
let game;
let newRow;
let newCell;
let jsonData;
let params;
let link;
let th;
let td;
let data;
function prepareLink(){
    params = new URLSearchParams(document.location.search);
    link = 'my-games-json?userID=' + params.get('userID');
}

function showChart(){
    prepareLink();
    const tbodyRef = document.getElementById('chart-1').getElementsByTagName('tbody')[0];
    $.ajax(link, {
        type: "GET",
        success: function(data) {
            jsonData = JSON.parse(data)
            for (let i = 0; i < jsonData.games.length; i++) {
                game = jsonData.games[i];
                
                newRow = tbodyRef.insertRow();
                newCell = newRow.insertCell();
                newCell.style = '--size: ' + (game.minutes_played_today/1440);

                th = document.createElement('th');
                th.innerHTML = game.name;

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
        },
        error: function() {
        },
        done: function() {
        }
    });
    
}
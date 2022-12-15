window.addEventListener('load', showChart());
var game;
var newRow;
var newCell;
var jsonData;

var params;
var link;

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
            console.log("success: ", data);
            jsonData = JSON.parse(data)
            for (var i = 0; i < jsonData.games.length; i++) {
                game = jsonData.games[i];
                newRow = tbodyRef.insertRow();
                newCell = newRow.insertCell();
                newCell.style = '--size: ' + (game.minutes_played_today/1440);
                th = document.createElement('th');
                th.innerHTML = game.name;
                newCell.appendChild(th);
            }
        },
        error: function(data) {
            console.log("error: ", data);
            console.log(link);
        },
        done: function(e) {
            console.log("done");
        }
    });
    
}
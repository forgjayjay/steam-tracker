window.addEventListener('load', showChart());

function showChart(){
    var games = ['0.1', '0.2', '1.0'];
    let params = new URLSearchParams(document.location.search);
    const tbodyRef = document.getElementById('chart-1').getElementsByTagName('tbody')[0];
    var link = 'my-games-json?userID=' + params.get('userID');
    $.ajax(link, {
        type: "GET",
        success: function(data) {
            console.log(data);
            var json = JSON.parse(data);
            var games = json.games;
        },
        error: function(data) {
            console.log("error: ", data);
            console.log(link);
        },
        done: function(e) {
            console.log("done");
        }
    });
    console.log(link);
    console.log(games);
    games.forEach(element => {
        var jsonObject = JSON.parse(element);
        var newRow = tbodyRef.insertRow();
        var newCell = newRow.insertCell();
        newCell.style = '--size: ' + /*element*/(jsonObject.getMinutes_played_today/1000);
        //console.log(element);
    });
}
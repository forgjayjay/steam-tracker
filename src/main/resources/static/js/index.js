const form = document.querySelector('form');
var link;
form.addEventListener('submit', (event) => {
  event.preventDefault(); // Prevent the form from submitting
  
  var inputValue = form.querySelector('input').value.trim();

  inputValue = trimUrl(inputValue);
  prepareLink(inputValue);
    $.ajax(link, {
        type: "GET",
        success: function(data) {
            jsonData = JSON.parse(data);
            form.querySelector('input').value = inputValue;
            if(jsonData.response.games.length > 0) form.submit();
        },
        error: function(data) {
            console.log('error', data);
        },
        done: function(data) {
            console.log('done', data);
        }
    });
});

function trimUrl(urlString) {
    let returnString = urlString;
    const pattern = new RegExp("(?:https?:\\/\\/)?steamcommunity\\.com\\/(?:profiles)\\/[a-zA-Z0-9]+");
    if (pattern.test(urlString)) {
        returnString = urlString.substring(urlString.indexOf("s/") + 2);
        var c = returnString.charAt(returnString.length - 1);
        if (c >= '0' && c <= '9') {
            returnString = returnString;
        } else{
            returnString = (returnString.substring(0, returnString.length - 1));
        }
    }
    return returnString;
}

function prepareLink(userID){
    link = '/api/v1/my-games-raw?userID=' + userID;
    console.log(link);
}

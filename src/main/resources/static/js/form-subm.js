const form = document.querySelector('form');
var link;
form.addEventListener('submit', (event) => {
  event.preventDefault(); // Prevent the form from submitting
  
  const input = form.querySelector('input');

  const inputValue = input.value.trim();

  prepareLink(inputValue);
    $.ajax(link, {
        type: "GET",
        success: function(data) {
            jsonData = JSON.parse(data);
            if(jsonData.response.games.length > 0)form.submit();
        },
        error: function(data) {
            console.log('error');
        },
        done: function(data) {
            console.log('done');
        }
    });
});

function prepareLink(userID){
    link = '/api/v1/my-games-raw?userID=' + userID;
    console.log(link);
}

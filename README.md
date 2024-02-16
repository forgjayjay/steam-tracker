
# steam-tracker

Simple application that tracks your steam in-game activity and displays time You spent playing today.

## Configuration:
- Create an application.properties file in the template folder of the app located at 'src\main\resources\application.properties'. More detailed explanation of required fields is located in the same resources folder under 'example.properties'
- You also need to configure a MySQL database where all the information will be stored. The application will create the table on it's own, but it needs to know where.

To access the application functions you need to go to the 'localhost:*{Your port}*' and enter your steam ID, or 
directly to 'localhost:*{Your port}*/my-games?userID=*{Your Steam ID}*'.

You can also go to the 'localhost:{Your port}/my-games-json?userID=*{Your Steam ID}*' to receive a raw JSON response.

The project folder also contains the 'example.json' that has an example of how your response file should look like.

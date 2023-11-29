# steam-tracker application

Simple application that tracks your steam game activity and displays time you spent playing today.

## This application requires some manual configuration to run properly:
- You need to create an application.properties file in the template folder of the app located at 'src\main\resources\application.properties'. More detailed explanation of required fields is located in the same resources folder under 'example.properties'
- Setting up your own database is in order, though all you need is to give the app the connection that it will utilize to create the database for you (I love hibernate)
- You need to have at least Java 8 and Maven installed on your machine.
- To boot up the application you need to input 'mvn clean spring-boot:run' into the console while being in the same folder as the 'pom.xml' file.


Finally, to access the application functions you need to go to the 'localhost:8080/my-games?userID=**your steam ID**'.

You can also go to the 'localhost:8080/my-games-json?userID=**your steam ID**' to receive a raw JSON response.

The folder also contains the 'example.json' that has an example of how your response file should look.

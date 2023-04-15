FROM openjdk:17-oracle
COPY ./target/steam-tracker.jar steam-tracker.jar
ENTRYPOINT ["java","-jar","/steam-tracker.jar"]
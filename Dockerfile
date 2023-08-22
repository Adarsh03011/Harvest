FROM maven:3.6.3-jdk-8
COPY ../src/main/resources/harvest.csv /
COPY ../src/main/resources/prices.csv /
COPY ./target/Assignment1-1.0-SNAPSHOT.jar /
CMD ["java","-jar","Assignment1-1.0-SNAPSHOT.jar"]
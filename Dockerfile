FROM maven:3.6.3-jdk-8
COPY ./ ./
COPY ../src/main/resources/harvest.csv /
COPY ../src/main/resources/prices.csv /
RUN mvn clean package
CMD ["java","-jar","target/Assignment1-1.0-SNAPSHOT.jar"]
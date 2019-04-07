FROM openjdk:8-jre-alpine
COPY api/target/*.jar sdehunt.jar
COPY certs certs
CMD java -jar sdehunt.jar
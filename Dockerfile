FROM openjdk:8-jre-alpine
COPY /target/*.jar sdehunt.jar
COPY rds-ca-2015-root.pem .
CMD java -jar sdehunt.jar
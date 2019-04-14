FROM openjdk:11
COPY api/target/*.jar sdehunt.jar
COPY certs certs
RUN keytool -import -alias rds -cacerts -noprompt -file certs/rds-ca-2015-root.pem -storepass changeit
CMD java -jar sdehunt.jar
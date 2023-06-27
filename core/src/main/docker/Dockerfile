FROM openjdk:11-jre-slim-sid

LABEL maintainer="Brainweb"
LABEL version="1.0"

ENV DB_NAME="postgres"
ENV DB_HOSTNAME="localhost"
ENV DB_PORT="5432"
ENV DB_USERNAME="gubee"
ENV DB_PWD="gubee"

EXPOSE 8080

WORKDIR /app

COPY gubee-interview.jar /app/gubee-interview.jar

ENTRYPOINT java -Xms128m -Xmx400m -jar -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector /app/gubee-interview.jar --jdbc.url=jdbc:postgresql://${DB_HOSTNAME}:${DB_PORT}/${DB_NAME} --jdbc.password=${DB_PWD} --jdbc.username=${DB_USERNAME}

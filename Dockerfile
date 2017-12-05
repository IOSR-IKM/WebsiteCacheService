FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENV AWS_DEFAULT_REGION=us-east-2
ENV AWS_DEFAULT_OUTPUT=json

ENV AWS_REGION=us-east-2
ENV AWS_OUTPUT=json

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

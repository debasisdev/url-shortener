FROM openjdk:8-jdk-alpine
MAINTAINER debasis.babun@gmail.com

VOLUME /tmp
ARG JAR_FILE=./target/urlapp-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} urlapp.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/urlapp.jar"]

EXPOSE 8080
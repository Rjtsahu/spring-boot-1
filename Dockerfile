FROM arm64v8/gradle:7.5.1-jdk17 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

# FROM openjdk:17-jre-slim

EXPOSE 8080

RUN mkdir /app

RUN ls /app
RUN ls /home/gradle/src

# COPY  /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/fluxdemo-0.0.1-SNAPSHOT.jar"]
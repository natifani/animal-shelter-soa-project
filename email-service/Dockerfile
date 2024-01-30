FROM openjdk:17-jdk-slim

WORKDIR .

ENV PORT 8085
EXPOSE 8085

COPY ./build/libs/* build/libs/
COPY ./build/libs/email-service-1.0-SNAPSHOT.jar build/

WORKDIR /./build

ENTRYPOINT java -jar email-service-1.0-SNAPSHOT.jar

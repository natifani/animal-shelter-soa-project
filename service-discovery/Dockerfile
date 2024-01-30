FROM openjdk:17-jdk-slim

WORKDIR .

ENV PORT 8761
EXPOSE 8761

COPY ./build/libs/* build/libs/
COPY ./build/libs/service-discovery-0.0.1-SNAPSHOT.jar build/

WORKDIR /./build

ENTRYPOINT java -jar service-discovery-0.0.1-SNAPSHOT.jar
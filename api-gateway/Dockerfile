FROM openjdk:17-jdk-slim

WORKDIR .

ENV PORT 8081
EXPOSE 8081

COPY ./build/libs/* build/libs/
COPY ./build/libs/api-gateway-0.0.1-SNAPSHOT.jar build/

WORKDIR /./build

ENTRYPOINT java -jar api-gateway-0.0.1-SNAPSHOT.jar
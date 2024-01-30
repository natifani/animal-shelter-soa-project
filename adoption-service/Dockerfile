FROM openjdk:17-jdk-slim

WORKDIR .

ENV PORT 8083
EXPOSE 8083

COPY ./build/libs/* build/libs/
COPY ./build/libs/adoption-service-0.0.1-SNAPSHOT.jar build/

WORKDIR /./build

ENTRYPOINT java -jar adoption-service-0.0.1-SNAPSHOT.jar
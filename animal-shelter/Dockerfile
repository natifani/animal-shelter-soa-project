FROM openjdk:17-jdk-slim

WORKDIR .

ENV PORT 8080
EXPOSE 8080

COPY ./build/libs/* build/libs/
COPY ./build/libs/animal-shelter-0.0.1-SNAPSHOT.jar build/

WORKDIR /./build

ENTRYPOINT java -jar animal-shelter-0.0.1-SNAPSHOT.jar

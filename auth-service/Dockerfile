FROM openjdk:17-jdk-slim

WORKDIR .

ENV PORT 8082
EXPOSE 8082

COPY ./build/libs/* build/libs/
COPY ./build/libs/auth-service-0.0.1-SNAPSHOT.jar build/

WORKDIR /./build

ENTRYPOINT java -jar auth-service-0.0.1-SNAPSHOT.jar
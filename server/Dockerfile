FROM openjdk:24-slim-bookworm
WORKDIR /
ADD target/server-1.0.0-jar-with-dependencies.jar server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]
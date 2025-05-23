FROM amazoncorretto:21-alpine-jdk

COPY Citas-0.0.1-SNAPSHOT.jar citas.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/citas.jar"]
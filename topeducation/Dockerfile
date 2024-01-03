FROM openjdk:20
ARG JAR_FILE=target/TopEducationApp.jar
COPY ${JAR_FILE} TopEducationApp.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/TopEducationApp.jar"]
FROM openjdk:11

ARG JAR

EXPOSE 8080

COPY target/${JAR} tic-tac-toe.jar

ENTRYPOINT ["java", "-jar", "/tic-tac-toe.jar"]

FROM openjdk:11

WORKDIR /app

COPY build/libs/user-manager.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

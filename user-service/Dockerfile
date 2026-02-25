FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY . .

RUN mvn -pl user-service -am clean package -DskipTests


FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /build/user-service/target/*.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]
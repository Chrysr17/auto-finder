LABEL authors="Christian"
# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom y descargamos deps (cache)
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Copiamos código y compilamos
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el jar generado (toma el único jar del target)
COPY --from=build /app/target/*.jar app.jar

# Puerto por env (docker-compose ya lo setea)
ENV SERVER_PORT=8080
EXPOSE 8080

# Inicia la app
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${SERVER_PORT}"]
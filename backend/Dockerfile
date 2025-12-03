# Stage 1: Build frontend
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend

COPY frontend/package.json frontend/package-lock.json* ./
RUN npm install

COPY frontend/ .
RUN npm run build

# Stage 2: Build Spring Boot backend
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app

# copy backend project into /app/backend
COPY backend/ ./backend/

# copy frontend dist inside backend static folder
COPY --from=frontend-build /app/frontend/dist ./backend/src/main/resources/static

# build the jar using the backend's pom.xml
RUN mvn -f backend/pom.xml -B -DskipTests package

# Stage 3: Runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
EXPOSE 8080

COPY --from=backend-build /app/backend/target/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

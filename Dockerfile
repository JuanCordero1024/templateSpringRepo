# ----------------------------------------------------------------------
# 1. BUILD STAGE: Utiliza una imagen con Maven y JDK 21 para compilar.
# ----------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Maven (pom.xml) primero para aprovechar el cache de Docker
COPY pom.xml .

# Descarga las dependencias, si el pom.xml no cambia, este paso se cachea
RUN mvn dependency:go-offline

# Copia el código fuente completo
COPY src ./src

# Empaqueta la aplicación como un JAR ejecutable (spring-boot:repackage)
# El nombre del JAR se basa en el artifactId y version del pom.xml
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------
# 2. RUNTIME STAGE: Utiliza una imagen JRE más ligera para el entorno de ejecución
# ----------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

# Define un argumento para el nombre del archivo JAR (obtenido del pom.xml)
ARG JAR_FILE=/app/target/templateMicroService-0.0.1-SNAPSHOT.jar

# Copia el JAR generado desde la etapa de compilación
COPY --from=build ${JAR_FILE} app.jar

# Define el puerto que expone la aplicación Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]

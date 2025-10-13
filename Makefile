# Variables
APP_NAME := templateMicroService
DOCKER_COMPOSE := docker compose

# Comandos de compilación y limpieza de Maven
MAVEN_BUILD := mvn clean package -DskipTests
MAVEN_CLEAN := mvn clean

.PHONY: all build up start stop clean purge logs db-logs app-logs check-tools

# Tarea para verificar herramientas necesarias
check-tools:
	@if ! command -v $(DOCKER_COMPOSE) &> /dev/null; then \
		echo "Error: 'docker compose' (o 'docker-compose') no encontrado. Asegúrate de que Docker esté instalado y funcionando."; \
		exit 1; \
	fi
	@if ! command -v mvn &> /dev/null; then \
		echo "Error: 'mvn' (Maven) no encontrado. Asegúrate de que Maven esté instalado y en tu PATH."; \
		exit 1; \
	fi

# Tarea principal: construye la aplicación, la imagen de Docker y levanta los servicios
all: check-tools build up

# Compila el proyecto Spring Boot (genera el archivo JAR)
build:
	@echo "--- 📦 Compilando el proyecto Spring Boot (Generando JAR) ---"
	$(MAVEN_BUILD)
	@echo "--- ✅ Compilación de Spring Boot finalizada ---"

# Construye las imágenes de Docker (base de datos y aplicación) y levanta los contenedores
up:
	@echo "--- 🚀 Levantando los servicios con Docker Compose ---"
	# La bandera --build fuerza la recompilación de la imagen 'app' si el Dockerfile o el código cambiaron
	$(DOCKER_COMPOSE) up --build -d
	@echo "--- ✅ Servicios de PostgreSQL y Aplicación iniciados en segundo plano ---"
	@echo "--- Revisa logs con 'make logs' ---"

# Levanta los contenedores existentes sin reconstruir
start:
	@echo "--- ▶️ Iniciando contenedores existentes ---"
	$(DOCKER_COMPOSE) start

# Detiene los contenedores
stop:
	@echo "--- 🛑 Deteniendo los contenedores ---"
	$(DOCKER_COMPOSE) stop

# Detiene y elimina contenedores, redes e imágenes (limpieza total)
clean:
	@echo "--- 🗑️ Deteniendo y eliminando contenedores y redes ---"
	$(DOCKER_COMPOSE) down
	$(MAVEN_CLEAN)
	@echo "--- ✅ Limpieza completada. Directorio 'target' y contenedores eliminados ---"

# Elimina contenedores, redes e imágenes Y VOLÚMENES de datos (¡Cuidado! Borra los datos de la DB)
purge:
	@echo "--- ☢️ ELIMINANDO TODOS LOS CONTENEDORES Y VOLÚMENES DE DATOS (Base de datos incluida) ---"
	$(DOCKER_COMPOSE) down -v --rmi all
	$(MAVEN_CLEAN)
	@echo "--- ✅ Purga completa. Datos de la DB eliminados ---"

# Muestra los logs de todos los servicios
all-logs:
	@echo "--- 📜 Logs de todos los servicios (Ctrl+C para salir) ---"
	$(DOCKER_COMPOSE) logs -f --tail 50

# Muestra solo los logs de la base de datos
db-logs:
	@echo "--- 📜 Logs del servicio 'db' (PostgreSQL) ---"
	$(DOCKER_COMPOSE) logs -f db

# Muestra solo los logs de la aplicación
app-logs:
	@echo "--- 📜 Logs del servicio 'app' (Spring Boot) ---"
	$(DOCKER_COMPOSE) logs -f app
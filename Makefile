# ----------------------------------------------------------------------
# Variables
# ----------------------------------------------------------------------
APP_NAME := templateMicroService
DOCKER_COMPOSE := docker compose

# Comandos de compilación y limpieza de Maven
MAVEN_PACKAGE := mvn package -DskipTests
MAVEN_CLEAN := mvn clean

.PHONY: all build up start stop clean purge logs db-logs app-logs fast-build

# ----------------------------------------------------------------------
# Tarea principal
# ----------------------------------------------------------------------
all: check-tools fast-build up

# ----------------------------------------------------------------------
# Verifica herramientas
# ----------------------------------------------------------------------
check-tools:
	@if ! command -v $(DOCKER_COMPOSE) &> /dev/null; then \
		echo "Error: 'docker compose' no encontrado."; \
		exit 1; \
	fi
	@if ! command -v mvn &> /dev/null; then \
		echo "Error: 'mvn' (Maven) no encontrado."; \
		exit 1; \
	fi

# ----------------------------------------------------------------------
# Compilación completa (limpia + empaqueta)
# ----------------------------------------------------------------------
build:
	@echo "--- 🧹 Limpieza y compilación completa ---"
	$(MAVEN_CLEAN)
	$(MAVEN_PACKAGE)
	@echo "--- ✅ JAR regenerado completamente ---"

# ----------------------------------------------------------------------
# Compilación rápida (sin clean)
# ----------------------------------------------------------------------
fast-build:
	@echo "--- ⚡ Compilación rápida (sin limpiar target/) ---"
	$(MAVEN_PACKAGE)
	@echo "--- ✅ JAR actualizado sin recompilar todo ---"

rebuild:
	@echo "--- 🔁 Reconstruyendo solo la imagen de la app ---"
	$(DOCKER_COMPOSE) build app

# ----------------------------------------------------------------------
# Levantar servicios sin recompilar JAR
# ----------------------------------------------------------------------
up:
	@echo "--- 🚀 Levantando servicios ---"
	$(DOCKER_COMPOSE) up -d
	@echo "--- ✅ Servicios activos ---"

# ----------------------------------------------------------------------
# Iniciar contenedores existentes
# ----------------------------------------------------------------------
start:
	@echo "--- ▶️ Iniciando contenedores existentes ---"
	$(DOCKER_COMPOSE) start

# ----------------------------------------------------------------------
# Detener contenedores
# ----------------------------------------------------------------------
stop:
	@echo "--- 🛑 Deteniendo contenedores ---"
	$(DOCKER_COMPOSE) stop

# ----------------------------------------------------------------------
# Limpiar contenedores e imágenes
# ----------------------------------------------------------------------
clean:
	@echo "--- 🧹 Limpiando contenedores y target ---"
	$(DOCKER_COMPOSE) down
	$(MAVEN_CLEAN)

# ----------------------------------------------------------------------
# Purga completa
# ----------------------------------------------------------------------
purge:
	@echo "--- ☢️ Purga total (base de datos incluida) ---"
	$(DOCKER_COMPOSE) down -v --rmi all
	$(MAVEN_CLEAN)

# ----------------------------------------------------------------------
# Logs
# ----------------------------------------------------------------------
all-logs:
	$(DOCKER_COMPOSE) logs -f --tail 50

db-logs:
	$(DOCKER_COMPOSE) logs -f db

app-logs:
	$(DOCKER_COMPOSE) logs -f app

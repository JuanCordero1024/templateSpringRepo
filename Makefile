# Nombre del servicio de la aplicación en Docker-compose.yml
APP_SERVICE = app

# Nombre del archivo JAR que se construye (debe coincidir con Dockerfile y pom.xml)
JAR_NAME = FilesMicroservice-0.0.1-SNAPSHOT.jar

# Alias para docker compose (para compatibilidad con diferentes versiones de Docker)
DOCKER_COMPOSE = docker-compose

# ==============================================================================
# COMANDOS PRINCIPALES DE DESPLIEGUE (Los que intentaste usar)
# ==============================================================================

# 1. Compila el proyecto con Maven y construye la imagen Docker de la aplicación
build: clean
	@echo "==========================================="
	@echo "    🛠️ 1. Compilando y Construyendo JAR    "
	@echo "==========================================="
	@mvn clean package -DskipTests
	@echo "==========================================="
	@echo "    🐳 2. Construyendo imagen Docker       "
	@echo "==========================================="
	$(DOCKER_COMPOSE) build $(APP_SERVICE)

# 2. Inicia los servicios definidos en el docker-compose
up:
	@echo "==========================================="
	@echo "    🚀 Iniciando Contenedores (DB + App)   "
	@echo "==========================================="
	$(DOCKER_COMPOSE) up -d

# 3. Muestra el estado de los contenedores (Sustituye 'makest')
status:
	@echo "==========================================="
	@echo "    🔍 Estado de los Contenedores          "
	@echo "==========================================="
	$(DOCKER_COMPOSE) ps

# 4. Muestra los logs de la aplicación y la base de datos (Sustituye 'make all logs')
logs:
	@echo "==========================================="
	@echo "    📄 Mostrando Logs (Ctrl+C para salir)  "
	@echo "==========================================="
	$(DOCKER_COMPOSE) logs -f

# ==============================================================================
# COMANDOS DE MANTENIMIENTO
# ==============================================================================

# Detiene y elimina los contenedores y redes
down:
	@echo "==========================================="
	@echo "    🛑 Deteniendo y Eliminando Contenedores"
	@echo "==========================================="
	$(DOCKER_COMPOSE) down -v

# Limpia el directorio de construcción de Maven
clean:
	@echo "==========================================="
	@echo "    🧹 Limpiando directorio 'target'       "
	@echo "==========================================="
	@mvn clean

# Ejecuta el flujo completo de construcción y despliegue
deploy: build up status logs

.PHONY: build up status logs down clean deploy
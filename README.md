# Microservicio de Firma Electrónica (Python / FastAPI) - Plantilla con Docker

Plantilla lista para usar que incluye:
- FastAPI + uvicorn
- SQLite (data en volumen)
- Endpoints para generar pares de claves RSA, firmar texto y verificar firmas
- Dockerfile y docker-compose.yml

## Requisitos
- Docker & Docker Compose
- (Opcional) Python 3.11+ si desea ejecutar localmente sin Docker

## Ejecutar con Docker
1. Construir y levantar:
   ```bash
   docker compose up --build -d
   ```
2. La API estará disponible en: `http://localhost:8000`
3. Docs interactivos: `http://localhost:8000/docs`

## Notas de seguridad
- En esta plantilla las claves privadas se guardan en la base de datos en texto plano (solo para pruebas).
- En producción use HSM, Vault, o cifrado fuerte para las privadas.

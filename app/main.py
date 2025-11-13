from fastapi import FastAPI, File, UploadFile, Form
from fastapi.responses import StreamingResponse
from starlette.responses import JSONResponse
from app.services.watermark_pdf import add_watermark_pdf
from app.services.watermark_word import add_watermark_word
import json
from datetime import datetime
import os

# Ruta del archivo de registro
LOG_FILE = "firmas_log.json"


def registrar_firma(usuario: str, tipo: str, archivo: str):
    """Registra información de la firma en un archivo JSON."""
    registro = {
        "usuario": usuario,
        "tipo": tipo,
        "archivo": archivo,
        "fecha": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    }

    # Crear archivo si no existe
    if not os.path.exists(LOG_FILE):
        with open(LOG_FILE, "w", encoding="utf-8") as f:
            json.dump([], f, indent=4)

    # Leer el contenido actual
    with open(LOG_FILE, "r", encoding="utf-8") as f:
        datos = json.load(f)

    # Agregar nuevo registro
    datos.append(registro)

    # Guardar nuevamente
    with open(LOG_FILE, "w", encoding="utf-8") as f:
        json.dump(datos, f, indent=4)


app = FastAPI(title="Microservicio de Firma Beta")


@app.post("/firmar")
async def firmar_documento(
    usuario: str = Form(...),
    tipo: str = Form(...),
    archivo: UploadFile = File(...)
):
    """Recibe un archivo PDF o Word, lo marca con el nombre del usuario y lo devuelve firmado."""
    if tipo.lower() == "pdf":
        result = add_watermark_pdf(archivo.file, usuario)
        registrar_firma(usuario, tipo, archivo.filename)
        return StreamingResponse(
            result,
            media_type="application/pdf",
            headers={
                "Content-Disposition": f'attachment; filename="firmado_{archivo.filename}"'
            },
        )

    elif tipo.lower() in ["word", "docx"]:
        result = add_watermark_word(archivo.file, usuario)
        registrar_firma(usuario, tipo, archivo.filename)
        return StreamingResponse(
            result,
            media_type="application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            headers={
                "Content-Disposition": f'attachment; filename="firmado_{archivo.filename}"'
            },
        )

    else:
        return {"error": "Tipo de documento no soportado"}


# 🔹 NUEVO ENDPOINT para consultar el historial de firmas
@app.get("/firmas")
def obtener_firmas():
    """Devuelve el historial de documentos firmados."""
    if not os.path.exists(LOG_FILE):
        return JSONResponse(content={"mensaje": "Aún no hay firmas registradas"}, status_code=200)

    with open(LOG_FILE, "r", encoding="utf-8") as f:
        datos = json.load(f)

    return JSONResponse(content=datos, status_code=200)

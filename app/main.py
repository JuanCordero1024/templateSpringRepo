from fastapi import FastAPI, File, UploadFile, Form
from fastapi.responses import StreamingResponse, JSONResponse
from app.services.sign_pdf import sign_file
import io

app = FastAPI()

@app.post("/firmar")
async def firmar_documento(
        usuario: str = Form(...),
        tipo: str = Form(...),
        private_key_file: UploadFile = File(..., alias="private_key"),
        archivo: UploadFile = File(...)
):
    try:
        contenido_original = await archivo.read()

        private_key_bytes = await private_key_file.read()
        private_key_str = private_key_bytes.decode('utf-8')

        if tipo.lower() == "pdf":
            # Función que inyecta en metadata
            pdf_final_io, firma_real = sign_file(
                contenido_original,
                usuario,
                private_key_str
            )

            headers = {
                "X-Digital-Signature": firma_real,
                "Content-Disposition": f'attachment; filename="signed_{archivo.filename}"'
            }

            return StreamingResponse(
                pdf_final_io,
                media_type="application/pdf",
                headers=headers
            )
        else:
            return JSONResponse({"error": "Solo PDF soportado por ahora"}, status_code=400)

    except Exception as e:
        return JSONResponse({"error": str(e)}, status_code=500)
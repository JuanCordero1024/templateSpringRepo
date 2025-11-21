from docx import Document
from io import BytesIO
from datetime import datetime

def add_watermark_word(input_docx, usuario, usuario_id: str):
    doc = Document(input_docx)

    # --- Agregar metadatos al archivo Word ---
    core = doc.core_properties
    core.author = usuario                       # Autor visible
    core.comments = f"UserID={usuario_id}"      # Guarda el UUID como comentario
    core.title = f"Documento firmado por {usuario}"
    core.subject = "Firma Beta - Microservicio"
    core.keywords = "firma, beta, microservicio"

    # --- Agregar texto visible de firma ---
    fecha = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    doc.add_paragraph(f"\n--- Documento firmado por: {usuario} el {fecha} ---")

    # Guardar documento Word firmado en memoria
    output = BytesIO()
    doc.save(output)
    output.seek(0)
    return output



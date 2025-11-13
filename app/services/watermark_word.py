from docx import Document
from io import BytesIO
from datetime import datetime

def add_watermark_word(input_docx, usuario):
    doc = Document(input_docx)

    # Agregar texto de firma
    fecha = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    doc.add_paragraph(f"\n--- Documento firmado por: {usuario} el {fecha} ---")

    # Guardar documento Word firmado en memoria
    output = BytesIO()
    doc.save(output)
    output.seek(0)
    return output


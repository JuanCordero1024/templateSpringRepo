from PyPDF2 import PdfReader, PdfWriter
from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import letter
from datetime import datetime
import io

def add_watermark_pdf(input_pdf, usuario, usuario_id=str):
    reader = PdfReader(input_pdf)
    writer = PdfWriter()

    # Crear marca de agua en memoria
    packet = io.BytesIO()
    can = canvas.Canvas(packet, pagesize=letter)
    can.setFont("Helvetica-Bold", 36)
    can.setFillAlpha(0.2)
    can.drawCentredString(300, 400, f"Documento Firmado por: {usuario}")
    can.save()
    packet.seek(0)
    watermark = PdfReader(packet)

    # Fusionar marca de agua en todas las páginas
    for page in reader.pages:
        page.merge_page(watermark.pages[0])
        writer.add_page(page)

    # Agregar metadatos
    fecha = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    writer.add_metadata({
        "/Author": usuario,
        "/UserID": str(usuario_id),
        "/Title": f"Documento firmado por {usuario}",
        "/Subject": "Firma Beta - Microservicio",
        "/Keywords": "firma, beta, microservicio",
        "/Producer": "MicroservicioFirma v1.0",
        "/CreationDate": fecha,
        "/ModDate": fecha
    })

    # Guardar PDF firmado en memoria
    output = io.BytesIO()
    writer.write(output)
    output.seek(0)
    return output


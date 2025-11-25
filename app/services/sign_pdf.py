from PyPDF2 import PdfReader, PdfWriter
from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import letter
from datetime import datetime
import io
from app.services.crypto_signer import digital_sign_rsa


def sign_file(input_pdf_bytes, usuario, private_key_str):
    # --- PASO 1: MARCA DE AGUA VISUAL ---
    packet = io.BytesIO()
    can = canvas.Canvas(packet, pagesize=letter)
    can.setFont("Helvetica-Bold", 36)
    can.setFillAlpha(0.2)
    can.saveState()
    can.translate(300, 400)
    can.rotate(45)
    can.drawCentredString(0, 0, f"Firmado por: {usuario}")
    can.restoreState()
    can.save()
    packet.seek(0)

    watermark_pdf = PdfReader(packet)
    original_pdf = PdfReader(io.BytesIO(input_pdf_bytes))
    writer_intermedio = PdfWriter()

    for page in original_pdf.pages:
        page.merge_page(watermark_pdf.pages[0])
        writer_intermedio.add_page(page)

    # Guardamos el PDF "visual" en memoria
    buffer_intermedio = io.BytesIO()
    writer_intermedio.write(buffer_intermedio)
    buffer_intermedio.seek(0)

    # Obtenemos los bytes para firmarlos
    bytes_para_firmar = buffer_intermedio.read()

    # --- PASO 2: CALCULAR LA FIRMA DIGITAL ---
    firma_digital = digital_sign_rsa(bytes_para_firmar, private_key_str)

    if not firma_digital:
        raise Exception("Error al generar la firma RSA")

    # --- PASO 3: INYECTAR FIRMA EN METADATA ---
    buffer_intermedio.seek(0)
    reader_final = PdfReader(buffer_intermedio)
    writer_final = PdfWriter()

    for page in reader_final.pages:
        writer_final.add_page(page)

    fecha = datetime.now().strftime("D:%Y%m%d%H%M%S")

    # Metadatos en el PDF
    writer_final.add_metadata({
        "/Author": usuario,
        "/Producer": "TheWorkers Team",
        "/CreationDate": fecha,
        "/Custom-Digital-Signature": firma_digital
    })

    # Generamos el archivo final-final
    output_final = io.BytesIO()
    writer_final.write(output_final)
    output_final.seek(0)

    # Retornamos el archivo y la firma (para el header)
    return output_final, firma_digital
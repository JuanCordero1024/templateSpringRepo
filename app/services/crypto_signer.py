import base64
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives.serialization import load_der_private_key
from cryptography.hazmat.backends import default_backend

def digital_sign_rsa(contenido_archivo: bytes, private_key_base64: str) -> str:
    """
    Firma los bytes de un archivo usando una llave privada RSA.
    Retorna la firma en Base64.
    """
    try:
        # 1. Decodificar el string Base64 a bytes binarios (DER)
        key_bytes = base64.b64decode(private_key_base64)

        # 2. Cargar la llave privada RSA
        private_key = load_der_private_key(key_bytes, password=None, backend=default_backend())

        # 3. Firmar los datos
        # Usamos RSA con Padding PSS y Hash SHA256
        signature = private_key.sign(
            contenido_archivo,
            padding.PSS(
                mgf=padding.MGF1(hashes.SHA256()),
                salt_length=padding.PSS.MAX_LENGTH
            ),
            hashes.SHA256()
        )

        # 4. Retornar la firma codificada en Base64
        return base64.b64encode(signature).decode('utf-8')

    except Exception as e:
        print(f"Error crítico al firmar con RSA: {e}")
        return None
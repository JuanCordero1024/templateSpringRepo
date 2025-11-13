from pydantic import BaseModel

class SignRequest(BaseModel):
    usuario: str
    tipo: str  # "pdf" o "word"

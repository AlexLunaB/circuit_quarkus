from fastapi import FastAPI, HTTPException
import random
import asyncio
import uvicorn
import logging
from datetime import datetime

# Configurar logging con timestamp
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)

app = FastAPI()

@app.get("/api/flaky-service")
async def flaky_endpoint():
    logger.info("Nueva solicitud recibida")
    # Simulador de caos
    outcome = random.random()
    # 30% de probabilidad: Timeout/Latencia alta (espera 3 segundos)
    if outcome < 0.30:
        logger.warning("Llamada lenta - esperando 3 segundos")
        await asyncio.sleep(3)
        logger.info("Llamada lenta completada exitosamente")
        return {"status": "success", "message": "Respuesta con retraso"}

    # 30% de probabilidad: Falla del servidor (HTTP 500)
    elif outcome < 0.60:
        logger.error("Llamada con error - HTTP 500")
        raise HTTPException(status_code=500, detail="Error interno simulado")

    # 40% de probabilidad: Éxito rápido
    else:
        logger.info("Llamada exitosa - respuesta rápida")
        return {"status": "success", "message": "Respuesta rápida y exitosa"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8005)

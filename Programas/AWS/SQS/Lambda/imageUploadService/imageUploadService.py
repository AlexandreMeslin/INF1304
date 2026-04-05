'''
Função Lambda para upload de imagens via API Gateway para um bucket S3.
Espera receber um JSON com 'filename' e 'filedata' (base64).
Retorna a URL pública do arquivo carregado.
'''
import json
import base64
import boto3
from datetime import datetime

s3 = boto3.client("s3")
BUCKET_NAME = "image-app-2025-11-23"

def lambda_handler(event, context):
    # Suporte para chamadas via API Gateway (REST ou HTTP)
    if isinstance(event.get("body"), str):
        try:
            body = json.loads(event["body"])
        except Exception:
            return response(400, {"error": "body nao e JSON valido"})
    else:
        body = event

    filename = body.get("filename")
    filedata = body.get("filedata")

    if not filename or not filedata:
        return response(400, {"error": "Parametros obrigatorios: filename, filedata (base64)"})

    try:
        # Decodifica a imagem base64
        image_bytes = base64.b64decode(filedata)

        # Evita sobrescrever arquivos – coloca timestamp
        timestamp = datetime.utcnow().strftime("%Y%m%d-%H%M%S")
        object_key = f"uploads/{timestamp}-{filename}"

        # Grava no S3
        s3.put_object(
            Bucket=BUCKET_NAME,
            Key=object_key,
            Body=image_bytes,
            ContentType="image/jpeg" if filename.lower().endswith(".jpg") or filename.lower().endswith(".jpeg")
                        else "image/png" if filename.lower().endswith(".png")
                        else "application/octet-stream"
        )

        return response(200, {
            "message": "Upload realizado com sucesso!",
            "file_url": f"https://{BUCKET_NAME}.s3.amazonaws.com/{object_key}"
        })

    except Exception as e:
        return response(500, {"error": str(e)})


def response(status, body_dict):
    """Resposta padrão com cabeçalhos CORS."""
    return {
        "statusCode": status,
        "headers": {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Headers": "Content-Type",
            "Access-Control-Allow-Methods": "OPTIONS,POST"
        },
        "body": json.dumps(body_dict)
    }
# Fim do arquivo imageUploadService.py

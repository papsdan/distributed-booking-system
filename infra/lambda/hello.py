import os
import boto3
from botocore.exceptions import ClientError

s3 = boto3.client("s3")
BUCKET = os.environ["BUCKET_NAME"]


def read(key):
    try:
        obj = s3.get_object(Bucket=BUCKET, Key=key)
        return obj["Body"].read().decode("utf-8")
    except ClientError as e:
        return f"DENIED ({e.response['Error']['Code']})"


def handler(event, context):
    result = {
        "welcome.txt": read("welcome.txt"),
        "secret.txt": read("secret.txt"),
        "rules.txt": read("rules.txt"),
    }
    print(result)
    return result
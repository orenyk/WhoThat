# PY 3.x
# python3 t.py
# Lec Maj
import http.client

# Create enrolment
conn = http.client.HTTPSConnection("api.projectoxford.ai")
data = open('./lec_hello.wav', 'rb').read()

headers = {
    'ocp-apim-subscription-key': "41c331eb960d4c5599f599b2dc00e4c1",
    'content-type': "multipart/form-data",
    'cache-control': "no-cache",
    'postman-token': "3f452a2c-9c23-edc6-cb94-288420313125"
    }

conn.request("POST", "/spid/v1.0/identificationProfiles/4dd26ac5-1859-4c6b-88e4-b96bc848e45a/enroll?shortAudio=true", data, headers=headers)

res = conn.getresponse()
print("-----> Status: ", res.status)
#print("-----> Reason: ", res.reason)
#print("-----> Msg: ", res.msg)
print("-----> Headers: ", res.getheaders())


data = res.read()
print(data.decode("utf-8"))

# PY 3.x
# python3 t2.py
# Lec Maj
import http.client

# Create enrolment
conn = http.client.HTTPSConnection("api.projectoxford.ai")
data = open('./lec_id.wav', 'rb').read()

headers = {
    'ocp-apim-subscription-key': "41c331eb960d4c5599f599b2dc00e4c1",
    'content-type': "application/octet-stream",
    'cache-control': "no-cache",
    'postman-token': "88604f61-92ec-520d-f94d-bbda543d5c52"
    }

conn.request("POST", "/spid/v1.0/identify?identificationProfileIds=4dd26ac5-1859-4c6b-88e4-b96bc848e45a%2C598f3ffd-fc3b-461e-9d60-30ac69959ac3%2C73e64453-3351-4b92-abc8-a4e62b336cf3%2Cb6d307ec-6a85-4fce-9294-f641218c937c&shortAudio=true", data, headers=headers)

res = conn.getresponse()
print("-----> Status: ", res.status)
#print("-----> Reason: ", res.reason)
#print("-----> Msg: ", res.msg)
#print("-----> Headers: ", res.getheaders())
#print("-----> Header apim-request-id: ", res.getheader("apim-request-id"))

# pull out of header the operation location and check status 
print("-----> Header Operation-Location: ", res.getheader("Operation-Location")) 

data = res.read()
#print("-----> response data lenght: ", len(data))
print(data.decode("utf-8"))

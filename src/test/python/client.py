import requests
url = 'http://localhost:9000/upload'
files = {'file': open('/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/interdomain-manifest.rdf', 'rb')}
r = requests.post(url,files=files)
print r.text
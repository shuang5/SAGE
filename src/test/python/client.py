import requests
from datetime import datetime

url = 'http://localhost:9000/'

def upload(file):
    files = {'file': open(file, 'rb')}
    #files = {'file': open('/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/interdomain-manifest.rdf', 'rb')}
    data ={'name':file}
    r = requests.post(url+'upload',data=data,files=files)
    print r.text
    
def allnodes(graph):
    query=url+"allnodes?graph="+graph
    r= requests.get(query)
    print r.text

def nodes(graph, node):
    query=url+"nodes?graph="+graph+"&id="+str(node)
    r=requests.get(query)
    print r.text
  
def shortestpath(graph, start, end):  
    query=url+"shortestpath?graph="+graph+"&start="+str(start)+"&end="+str(end)
    #print query
    r=requests.get(query)
    print r.text

def nonblockingshortestpath(graph, start, end):  
    query=url+"nb/shortestpath?graph="+graph+"&start="+str(start)+"&end="+str(end)
    #print query
    r=requests.get(query)
    print r.text    

from threading import Thread   
threads=[] 
threads.append(Thread(target=upload, args=("manifest1.rdf",)))    
threads.append(Thread(target=upload, args=("mp-manifest",)))  
threads.append(Thread(target=upload, args=("interdomain",)))  
threads.append(Thread(target=allnodes, args=("manifest1.rdf",)))  
threads.append(Thread(target=allnodes, args=("manifest1.rdf",)))  
threads.append(Thread(target=allnodes, args=("interdomain",)))  
threads.append(Thread(target=allnodes, args=("interdomain",))) 
threads.append(Thread(target=allnodes, args=("mp-manifest",)))  
threads.append(Thread(target=allnodes, args=("mp-manifest",))) 
threads.append(Thread(target=nodes, args=("manifest1.rdf",1)))  
threads.append(Thread(target=nodes, args=("manifest1.rdf",2))) 
threads.append(Thread(target=nodes, args=("manifest1.rdf",3)))  
threads.append(Thread(target=nodes, args=("manifest1.rdf",4))) 
threads.append(Thread(target=nodes, args=("interdomain",1)))  
threads.append(Thread(target=nodes, args=("interdomain",2))) 
threads.append(Thread(target=nodes, args=("interdomain",3)))  
threads.append(Thread(target=nodes, args=("interdomain",4))) 
threads.append(Thread(target=nodes, args=("mp-manifest",1)))  
threads.append(Thread(target=nodes, args=("mp-manifest",2))) 
threads.append(Thread(target=nodes, args=("mp-manifest",3)))  
threads.append(Thread(target=nodes, args=("mp-manifest",4))) 
threads.append(Thread(target=shortestpath, args=("manifest1.rdf",1,2))) 
threads.append(Thread(target=shortestpath, args=("manifest1.rdf",1,3))) 
threads.append(Thread(target=shortestpath, args=("manifest1.rdf",1,4))) 
threads.append(Thread(target=shortestpath, args=("manifest1.rdf",1,5))) 
threads.append(Thread(target=nonblockingshortestpath, args=("manifest1.rdf",1,2))) 
threads.append(Thread(target=nonblockingshortestpath, args=("manifest1.rdf",1,3))) 
threads.append(Thread(target=nonblockingshortestpath, args=("manifest1.rdf",1,4)))
threads.append(Thread(target=nonblockingshortestpath, args=("manifest1.rdf",1,5)))
threads.append(Thread(target=shortestpath, args=("interdomain",1,2))) 
threads.append(Thread(target=shortestpath, args=("interdomain",1,3))) 
threads.append(Thread(target=shortestpath, args=("interdomain",1,4))) 
threads.append(Thread(target=nonblockingshortestpath, args=("interdomain",1,2))) 
threads.append(Thread(target=nonblockingshortestpath, args=("interdomain",1,3))) 
threads.append(Thread(target=nonblockingshortestpath, args=("interdomain",1,4)))
threads.append(Thread(target=shortestpath, args=("mp-manifest",1,2))) 
threads.append(Thread(target=shortestpath, args=("mp-manifest",1,3))) 
threads.append(Thread(target=shortestpath, args=("mp-manifest",1,4))) 
threads.append(Thread(target=nonblockingshortestpath, args=("mp-manifest",1,2))) 
threads.append(Thread(target=nonblockingshortestpath, args=("mp-manifest",1,3))) 
threads.append(Thread(target=nonblockingshortestpath, args=("mp-manifest",1,4)))
time1=datetime.now()
for t in threads:
    t.start()
    
for t in threads:
    t.join() 
    
time2=datetime.now()

print "number of threads: %d" % len(threads)
print time2-time1
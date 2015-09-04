# SAGE
A REST server using spring.

Example usage:

1. upload 

	I. use html: http://localhost:9000
	
	II. use curl: curl -F 'name=interdomain' -F 'file=@/path/to/data.rdf' http://localhost:9000/upload
	
	III. use compressed string (gzip+base64): http://localhost:9000/receive?file=...
	
REST returns a key for every successful uploaded graph, which is used by users as an ID
 	
2. get the shortestpath between node id 1 and 3 on graph x

http://localhost:9000/shortestpath?graph=x&start=1&end=3

3. get the attributes of node id 1 on graph x

http://localhost:9000/nodes?graph=x&id=1 

4. get all nodes with attributes on graph x

http://localhost:9000/allnodes?graph=x

5. get the shortestpath between 1 and 3 using non-blocking call on graph x

http://localhost:9000/nb/shortestpath?graph=x&start=1&end=3



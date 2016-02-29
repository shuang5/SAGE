# SAGE
A REST server using spring.

Example usage:

I. upload 

	I. use html: http://localhost:9000
	
	II. use curl: curl -F 'name=interdomain' -F 'file=@/path/to/data.rdf' http://localhost:9000/upload
	
	III. use compressed string (gzip+base64): http://localhost:9000/receive?file=...
	
REST returns a key for every successful uploaded graph, which is used by users as an ID for the graph (gid). Each element (node or link) also has an ID (nid). Specifically for an NDL manifest, a resource is unqiuely identified by something like:
http://geni-orca.renci.org/owl/f38fd903-e77f-40c6-8749-888fd144e9ad#Node0
In this case, Node0 can be used as nid.
 	
II. get the shortestpath between nid 1 and 3 on gid x

http://localhost:9000/shortestpath?graph=x&start=1&end=3

III. get the neighbors of nid 1 on gid x

http://localhost:9000/neighbors?graph=x&id=1

IV. get the attributes of nid 1 on gid x

http://localhost:9000/nodes?graph=x&id=1 

V. get all nodes with attributes on gid x

http://localhost:9000/allnodes?graph=x

VI. get all VMs with attributes on gid x

http://localhost:9000/allVMs?graph=x

VII. get the shortestpath between nid 1 and 3 using non-blocking call on gid x

http://localhost:9000/nb/shortestpath?graph=x&start=1&end=3



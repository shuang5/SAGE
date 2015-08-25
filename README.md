# SAGE
A REST server using spring.

Example userage:

I. get the shortestpath between node id 1 and 3

http://localhost:9000/shortestpath?start=1&end=3

II. get the attributes of node id 1

http://localhost:9000/nodes?id=1

III. get all nodes with attributes

http://localhost:9000/allnodes

V. get the shortestpath between 1 and 3 using non-blocking call

http://localhost:9000/nb/shortestpath?start=1&end=3

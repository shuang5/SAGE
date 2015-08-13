package ndl_propertygraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import orca.ndllib.propertygraph.ManifestPropertygraphImpl;
import orca.ndllib.propertygraph.connector.OrcaNode;
import orca.ndllib.propertygraph.connector.PropertyGraphNode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe.LoopBundle;

@RestController
public class ResourceController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private ManifestLoader ml;
    public ResourceController(){
    	String rdfFile="/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/interdomain-manifest.rdf";
    	/*
    	GraphDatabaseService graphDatabaseService=new GraphDatabaseFactory().
				newEmbeddedDatabase("/Users/shuang/Downloads/neo4j-community-2.0.4/data/graph.db");
		Neo4j2Graph neo4jGraph=new Neo4j2Graph(graphDatabaseService);
    	ManifestPropertygraphImpl.convertManifestNDL(rdfFile,neo4jGraph);
		neo4jGraph.commit();
		neo4jGraph.stopTransaction(null);
		neo4jGraph.shutdown();
		*/
		ml=new ManifestLoader(rdfFile);
    }
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    @RequestMapping("/nodes")
    public PropertyGraphNode nodes(@RequestParam(value="id", defaultValue="1") int id) {
    	//ManifestLoader ml=new ManifestLoader("/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/manifest.rdf");
    	Graph graph=ml.getGraph();
    	//int i=Integer.parseInt(id);
    	if(graph.getVertex(id)==null)
    		throw new NodeNotFoundException(String.valueOf(id));
    	for(Vertex vertex:graph.getVertices()){
    		
    		if(id==Integer.parseInt((String) vertex.getId()))
    			return new PropertyGraphNode(vertex);
    	}
		return null;    	
    }
    
    @RequestMapping("/allnodes")
    public List<PropertyGraphNode> allnodes() {
    	//ManifestLoader ml=new ManifestLoader("/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/manifest.rdf");
    	final Graph graph=ml.getGraph();
    	List<PropertyGraphNode> list=new ArrayList<PropertyGraphNode>();
    	for(Vertex vertex:graph.getVertices()){
    		list.add(new PropertyGraphNode(vertex));
    	}
		return list;    	
    }
    @RequestMapping("neighbors")
    public List<PropertyGraphNode> neighbors(@RequestParam(value="id", defaultValue="1") int id) {
    	//ManifestLoader ml=new ManifestLoader("/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/manifest.rdf");
    	final Graph graph=ml.getGraph();
    	if(graph.getVertex(id)==null)
    		throw new NodeNotFoundException(String.valueOf(id));
    	List<PropertyGraphNode> list=new ArrayList<PropertyGraphNode>();
    	GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>();
    	pipe.start(graph.getVertex(id)).both("connectedTo");
    	for(Object vertex:pipe){
    		list.add(new PropertyGraphNode((Vertex)vertex));
    	}
		return list;    	
    }
    @RequestMapping("shortestpath")
    public List<PropertyGraphNode> shortestpath(@RequestParam(value="start",required=true) int id1,
    		@RequestParam(value="end",required=true) int id2) {
    	//ManifestLoader ml=new ManifestLoader("/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/manifest.rdf");
    	final Graph graph=ml.getGraph();
    	if(graph.getVertex(id1)==null)
    		throw new NodeNotFoundException(String.valueOf(id1));
    	else 
        	if(graph.getVertex(id2)==null)
        		throw new NodeNotFoundException(String.valueOf(id2));
    	final Vertex start=graph.getVertex(id1);
    	final Vertex end=graph.getVertex(id2);
    	int n=0;
    	for(@SuppressWarnings("unused") Object v:graph.getVertices()){
    		n++;
    	}
    	final int nn=n*n;
    	@SuppressWarnings("rawtypes")
		GremlinPipeline<Vertex, List> pipe = new GremlinPipeline<Vertex, List<Vertex>>(start).as("orcaNode").both("connectedTo").
    			loop("orcaNode", new PipeFunction<LoopBundle<Vertex>, Boolean>() {
    				@Override
    				public Boolean compute(LoopBundle<Vertex> bundle) {
    					//System.out.println(bundle.getLoops());
    					return bundle.getLoops() <nn && bundle.getObject() != end;
    				}
    			}).path();
    	ArrayList<PropertyGraphNode> list=new ArrayList<PropertyGraphNode>();
    	if (pipe.hasNext()) {
    		final ArrayList<Vertex> shortestPath = (ArrayList<Vertex>) pipe.next();
    		for(final Vertex v:shortestPath){
    			list.add(new PropertyGraphNode(v));
    		}
    	}
		return list;    	
    }
    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such node")  // 404
    public class NodeNotFoundException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NodeNotFoundException(String id) {
			super(id);
		}
        // ...
    }
}

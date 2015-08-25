package ndl_propertygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import orca.ndllib.propertygraph.connector.PropertyGraphNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

@RestController
public class ResourceController {

    private final AtomicLong counter = new AtomicLong();
    private static final Logger LOG=LoggerFactory.getLogger(ResourceController.class);
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
    @RequestMapping("/neighbors")
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
    @RequestMapping("/shortestpath")
    public List<PropertyGraphNode> shortestpath(@RequestParam(value="start",required=true) int id1,
    		@RequestParam(value="end",required=true) int id2) {
    	//ManifestLoader ml=new ManifestLoader("/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/manifest.rdf");
    	final Graph graph=ml.getGraph();
    	return GraphUtil.shortestpath(graph, id1, id2);
    }
    @RequestMapping("/nb/shortestpath")
    public DeferredResult<List<PropertyGraphNode>> nonBlockingShortestPath(@RequestParam(value="start",required=true) int id1,
    		@RequestParam(value="end",required=true) int id2) {
        // Initiate the processing in another thread
        DeferredResult<List<PropertyGraphNode>> deferredResult = new DeferredResult<>();
        ProcessingTask task = new ProcessingTask(ml.getGraph(),id1,id2, deferredResult);
        task.start();
        return deferredResult;
    }
    @ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception ex) {

		ModelAndView model = new ModelAndView("error/generic_error");
		model.addObject("errMsg", "this is Exception.class");

		return model;

	}
}

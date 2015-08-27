package ndl_propertygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import orca.ndllib.propertygraph.connector.PropertyGraphNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;


@RestController
public class ResourceController {

    private final AtomicLong counter = new AtomicLong();
    private static final Logger LOG=LoggerFactory.getLogger(ResourceController.class);
    private final String defaultFile="interdomain";
    private GraphCache cache;
    public ResourceController(){
    	cache= new GraphCache();
    	/*
    	GraphDatabaseService graphDatabaseService=new GraphDatabaseFactory().
				newEmbeddedDatabase("graph.db");
		Neo4j2Graph neo4jGraph=new Neo4j2Graph(graphDatabaseService);
    	ManifestPropertygraphImpl.convertManifestNDL(rdfFile,neo4jGraph);
		neo4jGraph.commit();
		neo4jGraph.stopTransaction(null);
		neo4jGraph.shutdown();
		*/
		//ml=new ManifestLoader(rdfFile);
    }
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(
    		@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        return GraphFile.saveFile(name, file);
    }
    @RequestMapping("/nodes")
    public PropertyGraphNode nodes(
    		@RequestParam(value="graph", defaultValue=defaultFile) String graphId,
    		@RequestParam(value="id", defaultValue="1") int id) {
    	Graph graph=cache.getEntry(graphId);
    	if(graph.getVertex(id)==null)
    		throw new NodeNotFoundException(String.valueOf(id));
    	for(Vertex vertex:graph.getVertices()){
    		
    		if(id==Integer.parseInt((String) vertex.getId()))
    			return new PropertyGraphNode(vertex);
    	}
		return null;    	
    }
    
    @RequestMapping("/allnodes")
    public List<PropertyGraphNode> allnodes(
    		@RequestParam(value="graph", defaultValue=defaultFile) String graphId) {
    	final Graph graph=cache.getEntry(graphId);
    	List<PropertyGraphNode> list=new ArrayList<PropertyGraphNode>();
    	for(Vertex vertex:graph.getVertices()){
    		list.add(new PropertyGraphNode(vertex));
    	}
		return list;    	
    }
    @RequestMapping("/neighbors")
    public List<PropertyGraphNode> neighbors(
    		@RequestParam(value="graph", defaultValue=defaultFile) String graphId,
    		@RequestParam(value="id", defaultValue="1") int id) {
    	final Graph graph=cache.getEntry(graphId);
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
    public List<PropertyGraphNode> shortestpath(
    		@RequestParam(value="graph", defaultValue=defaultFile) String graphId,
    		@RequestParam(value="start",required=true) int id1,
    		@RequestParam(value="end",required=true) int id2) {   	
    	final Graph graph=cache.getEntry(graphId);
    	return GraphUtil.shortestpath(graph, id1, id2);
    }
    @RequestMapping("/nb/shortestpath")
    public DeferredResult<List<PropertyGraphNode>> nonBlockingShortestPath(
    		@RequestParam(value="graph", defaultValue=defaultFile) String graphId,
    		@RequestParam(value="start",required=true) int id1,
    		@RequestParam(value="end",required=true) int id2) {
        // Initiate the processing in another thread
        DeferredResult<List<PropertyGraphNode>> deferredResult = new DeferredResult<>();
        ProcessingTask task = new ProcessingTask(cache.getEntry(graphId),id1,id2, deferredResult);
        task.start();
        return deferredResult;
    }
    @ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception ex) {
    	LOG.info("bad request");
		ModelAndView model = new ModelAndView("error/generic_error");
		model.addObject("errMsg", "this is Exception.class");

		return model;

	}
}

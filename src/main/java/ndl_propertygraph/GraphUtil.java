package ndl_propertygraph;

import java.util.ArrayList;
import java.util.List;

import orca.ndllib.propertygraph.connector.PropertyGraphNode;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe.LoopBundle;

public class GraphUtil {
	static List<PropertyGraphNode> shortestpath(Graph graph, int id1, int id2){
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
}

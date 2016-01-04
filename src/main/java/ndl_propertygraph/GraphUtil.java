package ndl_propertygraph;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import orca.ndllib.propertygraph.connector.PropertyGraphNode;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe.LoopBundle;

public final class GraphUtil {
	static List<PropertyGraphNode> shortestpath(final Graph graph, final int id1, final int id2){
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
	static List<PropertyGraphNode> shortestpath(final Graph graph, final String id1, final String id2){
		int  i1=Integer.valueOf((String) getIdByUrl(graph,id1));
		int  i2=Integer.valueOf((String) getIdByUrl(graph,id2));
		return shortestpath(graph,i1,i2);
	}
	static boolean isVertexVM(final Vertex v){
		String url=v.getProperty("url");
		if (!url.substring(url.length()-4).equals("vlan")) 
			return true;
		else 
			return false;
	}
	static boolean isVertexVlan(final Vertex v){
		final String url=v.getProperty("url");
		if (url.substring(url.length()-4).equals("vlan")) 
			return true;
		else 
			return false;
	}
	static Object getIdByUrl(final Graph graph, final String id){
		for(Vertex v:graph.getVertices()){
			try{
			URL u=new URL((String) v.getProperty("url"));
			if(id.equals(u.getRef()))
				return v.getId();
			}catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//return null;
		throw new NodeNotFoundException(id);
	}
	static Vertex getVertexByUrl(final Graph graph, final String id){
		for(Vertex v:graph.getVertices()){
			try {
				URL u = new URL((String) v.getProperty("url"));
				if(id.equals(u.getRef()))
					return v;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//return null;
		throw new NodeNotFoundException(id);
	}
}

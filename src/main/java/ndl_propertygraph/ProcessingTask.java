package ndl_propertygraph;


import java.util.List;

import orca.ndllib.propertygraph.connector.PropertyGraphNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import com.tinkerpop.blueprints.Graph;

public class ProcessingTask extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessingTask.class);

	private final DeferredResult<List<PropertyGraphNode>> deferredResult;
	private final Graph graph;
	private final int id1;
	private final int id2;
	public ProcessingTask( final Graph graph, final int id1,final int id2,DeferredResult<List<PropertyGraphNode>> deferredResult) {
		this.deferredResult = deferredResult;
		this.graph=graph;
		this.id1=id1;
		this.id2=id2;
	}
	
	@Override
	public void run() {
		try{
	    	boolean deferredStatus = deferredResult.setResult(GraphUtil.shortestpath(graph, id1, id2));  	
	    	if(!deferredStatus){
	    		LOG.warn("non-blocking request set or expired");
	    	}
		}catch(Exception ex){deferredResult.setErrorResult(ex);}
	}
}
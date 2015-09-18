package ndl_propertygraph;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public final class GraphCache {
	private static final long MAX_SIZE = 100;
	 
	  private final LoadingCache<String, Graph> cache;
	 
	  public GraphCache() {
	    cache = CacheBuilder.newBuilder()
	    		.maximumSize( MAX_SIZE )
	    		.expireAfterAccess(60, TimeUnit.MINUTES)
	    		.build( new CacheLoader<String, Graph>() {
	        @Override
	        public Graph load( String key ) throws Exception {
	        	Graph graph = new TinkerGraph();
	        	GraphFile.openFile(key, graph);
	        	return graph;
	        }
	      }
	    );
	  }
	 
	  public Graph getEntry(final String key ) {
	    return cache.getUnchecked( key );
	  }
	 
}

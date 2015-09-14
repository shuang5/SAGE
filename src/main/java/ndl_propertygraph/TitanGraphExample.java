package ndl_propertygraph;


import java.io.File;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import orca.ndllib.propertygraph.ManifestPropertygraphImpl;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Graph;

public class TitanGraphExample {
	public static final String INDEX_NAME = "search";
	public static TitanGraph create(final String directory, String key){
		
		TitanFactory.Builder config = TitanFactory.build();
        config.set("storage.backend", "cassandra");
        config.set("storage.hostname","127.0.0.1");
        config.set("storage.cassandra.keyspace", key);
        config.set("index."+INDEX_NAME+".backend","elasticsearch");
        config.set("index."+INDEX_NAME +".directory",directory+File.separator+"es");
        config.set("index."+INDEX_NAME+".elasticsearch.local-mode",true);
        config.set("index."+INDEX_NAME+".elasticsearch.client-only",false);
        TitanGraph graph1 = config.open();
        return graph1;
        
	
/*
		final Configuration conf = (Configuration) new BaseConfiguration();
		conf.setProperty("storage.backend", "cassandra");
		conf.setProperty("storage.hostname", "127.0.0.1");
		conf.setProperty("storage.keyspace", "graphA");

        conf.setProperty("index."+INDEX_NAME+".backend","elasticsearch");
        conf.setProperty("index." + INDEX_NAME + ".directory", directory + File.separator + "es");
        conf.setProperty("index."+INDEX_NAME+".elasticsearch.local-mode",true);
        conf.setProperty("index."+INDEX_NAME+".elasticsearch.client-only",false);
		TitanGraph graph = TitanFactory.open(conf);
		return graph;
		*/
	}
	public static void main(String[] args){
		Graph g1=(Graph) TitanGraphExample.create("titanDB", "titan");
		ManifestPropertygraphImpl.convertManifestNDL("data/interdomain",g1);
		Graph g2=(Graph) TitanGraphExample.create("titanDB", "mpmanifest");
		ManifestPropertygraphImpl.convertManifestNDL("data/mp-manifest",g2);
		Graph g3=(Graph) TitanGraphExample.create("titanDB", "manifest1");
		ManifestPropertygraphImpl.convertManifestNDL("data/manifest1",g3);
		g1.shutdown();
		g2.shutdown();
		g3.shutdown();
	}
}

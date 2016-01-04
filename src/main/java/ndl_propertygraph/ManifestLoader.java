package ndl_propertygraph;

import java.io.IOException;

import orca.ndllib.propertygraph.ManifestPropertygraphImpl;

//import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.tinkerpop.blueprints.Graph;
//import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

public class ManifestLoader {
	//private static String rdfFile="/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/mp-manifest.rdf";
	//private static String graphmlfile="/Users/shuang/git/radii/mainfest.graphml";
	//private static String jsonfile="/Users/shuang/git/radii/mainfest.json";
	//GraphDatabaseService graphDatabaseService=new GraphDatabaseFactory().
	//		newEmbeddedDatabase("/Users/shuang/Downloads/neo4j-community-2.0.4/data/graph.db");
	//Neo4j2Graph neo4jGraph=new Neo4j2Graph("/Users/shuang/Downloads/neo4j-community-2.0.4/data/graph.db");
	//Graph tinker=new TinkerGraph();
	//PropertyGraphFactory propertyGraphFactory=new PropertyGraphFactory(neo4jGraph);
	//NdlToPropertyGraph ndl=new NdlToPropertyGraph(propertyGraphFactory);
	//ndl.loadGraph(new File(rdfFile));
	private final Graph graph;
	private final String rdfFile;
	public ManifestLoader(final Graph graph,final String file){
		this.rdfFile=file;
		this.graph=graph;
		ManifestPropertygraphImpl.convertManifestNDL(rdfFile,graph);
		//graph.commit();
		//graph.stopTransaction(null);
	}
	public ManifestLoader(final String file){
		this.rdfFile=file;
		this.graph=new TinkerGraph();
		ManifestPropertygraphImpl.convertManifestNDL(rdfFile,graph);
		//graph.commit();
		//graph.stopTransaction(null);
	}
	public void writeGraphML(final String graphmlfile) throws IOException{
		GraphMLWriter writer=new GraphMLWriter(graph);
		writer.outputGraph(graphmlfile);
	}
	public void writeGraphSON(final String jsonfile) throws IOException{
		GraphSONWriter.outputGraph(graph,jsonfile);	
	}
	public void close(){
		graph.shutdown();			
	}
	public Graph getGraph(){
		return graph;
	}
}

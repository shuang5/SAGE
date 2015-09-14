package ndl_propertygraph;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... arg0) throws Exception {
		/*
		//GraphDatabaseService graphDatabaseService=new RestGraphDatabase("http://localhost:7474/db/data","neo4j","12345678");
		String rdfFile="/Users/shuang/Sandbox/gremlin-groovy-2.6.0/data/mp-manifest.rdf";
		String graphmlfile="/Users/shuang/git/radii/mainfest.graphml";
		String jsonfile="/Users/shuang/git/radii/mainfest.json";
		GraphDatabaseService graphDatabaseService=new GraphDatabaseFactory().
				newEmbeddedDatabase("/Users/shuang/Downloads/neo4j-community-2.0.4/data/graph.db");
		Neo4j2Graph neo4jGraph=new Neo4j2Graph(graphDatabaseService);
		//Graph tinker=new TinkerGraph();
		//PropertyGraphFactory propertyGraphFactory=new PropertyGraphFactory(neo4jGraph);
		//NdlToPropertyGraph ndl=new NdlToPropertyGraph(propertyGraphFactory);
		//ndl.loadGraph(new File(rdfFile));
		ManifestPropertygraphImpl.convertManifestNDL(rdfFile,neo4jGraph);
		neo4jGraph.commit();

		neo4jGraph.stopTransaction(null);
		GraphMLWriter writer=new GraphMLWriter(neo4jGraph);
		writer.outputGraph(graphmlfile);
		GraphSONWriter.outputGraph(neo4jGraph,jsonfile);
		neo4jGraph.shutdown();
		*/
	}
}

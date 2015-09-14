package ndl_propertygraph;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Graph;

import orca.ndllib.propertygraph.ManifestPropertygraphImpl;

public class TitanGraphBackEnd implements BackEnd{
	private static final Logger LOG = LoggerFactory.getLogger(TitanGraphBackEnd.class);
	public static final String INDEX_NAME = "search";
	private TitanFactory.Builder createDefaultConfig(){		
		TitanFactory.Builder config = TitanFactory.build();
        config.set("storage.backend", "cassandra");
        config.set("storage.hostname","127.0.0.1");
        config.set("index."+INDEX_NAME+".backend","elasticsearch");
        config.set("index."+INDEX_NAME +".directory","titanDB"+File.separator+"es");
        config.set("index."+INDEX_NAME+".elasticsearch.local-mode",true);
        config.set("index."+INDEX_NAME+".elasticsearch.client-only",false);
        return config;
	}
	private TitanFactory.Builder createDefaultConfig(String key){		
		TitanFactory.Builder config = TitanFactory.build();
        config.set("storage.backend", "cassandra");
        config.set("storage.hostname","127.0.0.1");
        config.set("index."+INDEX_NAME+key+".backend","elasticsearch");
        config.set("index."+INDEX_NAME+key+".directory","titanDB"+File.separator+"es");
        config.set("index."+INDEX_NAME+key+".elasticsearch.local-mode",true);
        config.set("index."+INDEX_NAME+key+".elasticsearch.client-only",false);
        return config;
	}
	@Override
	public Graph getEntry(String key) {
		TitanFactory.Builder config=createDefaultConfig(key).set("storage.cassandra.keyspace", key);
		TitanGraph graph = config.open();
        return graph;		
	}
	@Override
	public UploadedFile saveEntry(String file) throws DataFormatException {
		if(file.isEmpty())throw new IllegalStateException("File is empty");
		final String result= CompressEncode.decodeDecompress(file);
		final byte[] bytes = result.getBytes();
		byte[] thedigest=String.valueOf(Math.random()).getBytes();
		try{
			final MessageDigest md = MessageDigest.getInstance("MD5");
			thedigest = md.digest(bytes); 
		}
		catch(NoSuchAlgorithmException e){
			LOG.error("MD5 not available");
		}
        String name="graph_"+thedigest;
        Graph g=getEntry(name);
        ManifestPropertygraphImpl.convertManifestNDLFromString(file,g);
        return new UploadedFile(name);
	}
	@Override
	public UploadedFile saveEntry(String name, MultipartFile file) throws IOException{
		if (!file.isEmpty()) {
            final byte[] bytes = file.getBytes();
            String f=new String(bytes);
            Graph g=getEntry(name);
            ManifestPropertygraphImpl.convertManifestNDLFromString(f,g);
            return new UploadedFile(name);
        } else {
            throw new IllegalStateException("File is empty");
        }
	}
	
}

package ndl_propertygraph;

import java.io.File;
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
	private final MyProperties mp;
	public static final String INDEX_NAME = "search";	
	public TitanGraphBackEnd(MyProperties mp) {
		this.mp=mp;
	}

	@Override
	public Graph getEntry(final String key) {		
		if(!KeyCheck.keyIsValid(key))throw new KeyFormatException("Key format error:");
		TitanGraph graph=null;
        if (mp.getTitanbackend().equals("berkeleyje")){
        	String dir=mp.getBackenddir();
    		if (!dir.endsWith(File.separator))dir+=File.separator;
        	graph = TitanFactory.open(dir+key);
        }
        else if(mp.getTitanbackend().equals("cassandra")){
        	TitanFactory.Builder config = TitanFactory.build();
    		config.set("storage.backend", "cassandra");
            config.set("storage.hostname","127.0.0.1");
    		config.set("storage.cassandra.keyspace", key);  
    		config.set("index."+INDEX_NAME+".backend","elasticsearch");
            config.set("index."+INDEX_NAME+".directory",mp.getBackenddir());
            config.set("index."+INDEX_NAME+".elasticsearch.local-mode",true);
            config.set("index."+INDEX_NAME+".elasticsearch.client-only",false);
            graph = config.open();
        }
        else {
        	LOG.error("unknown Titan backend");
        }
        return graph;		
	}
	@Override
	public UploadedFile saveEntry(final String file) throws DataFormatException,IllegalStateException {
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
        ManifestPropertygraphImpl.convertManifestNDLFromString(result,g);
        g.shutdown();
        return new UploadedFile(name);
	}
	public UploadedFile saveEntry(final String name, final String file) throws DataFormatException,KeyFormatException,IllegalStateException {
		if(!KeyCheck.keyIsValid(name))throw new KeyFormatException("Key format error:");
		if(file.isEmpty())throw new IllegalStateException("File is empty");
		//TODO: should warn user if name exists, now it assumes user takes the reponisibility
		//i.e., if name exists, multiple graphs will co-exist in the same instance
		//this may or may not cause trouble to queries...
		final String result= CompressEncode.decodeDecompress(file);
        Graph g=getEntry(name);
        ManifestPropertygraphImpl.convertManifestNDLFromString(result,g);
        return new UploadedFile(name);
	}
	@Override
	public UploadedFile saveEntry(final String name, final MultipartFile file) throws IOException,KeyFormatException,IllegalStateException{
		if(!KeyCheck.keyIsValid(name))throw new KeyFormatException("Key format error:");
		if (file.isEmpty())throw new IllegalStateException("File is empty");
		//TODO: should warn user if name exists, now it assumes user takes the reponisibility
		//i.e., if name exists, multiple graphs will co-exist in the same instance
		//this may or may not cause trouble to queries...
        final byte[] bytes = file.getBytes();
        String f=new String(bytes);
        Graph g=getEntry(name);
        ManifestPropertygraphImpl.convertManifestNDLFromString(f,g);
        g.shutdown();
        return new UploadedFile(name);
	}

	/*
	public static void main(String[] args){
		final String name="interdomain";
		final BufferedReader br;
		TitanGraphBackEnd tb=new TitanGraphBackEnd("berkeleyje");
		
		try {
			br = new BufferedReader(new FileReader("data/"+name));
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = CompressEncode.compressEncode(sb.toString());
		    tb.saveEntry(name,everything);
		    br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Graph g=tb.getEntry(name);
		//ManifestPropertygraphImpl.convertManifestNDL("data/"+name,g);
		//tb.map.put(name,g);
		//g=tb.getEntry(name);
		
	}
	*/
}

package ndl_propertygraph;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import orca.ndllib.propertygraph.ManifestPropertygraphImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.tinkerpop.blueprints.Graph;

public class GraphFile {
	private static final Logger LOG = LoggerFactory.getLogger(GraphFile.class);
	private static String path="data/";
	public static void setPath(String path){
		GraphFile.path=path;
	}
	public static String getPath(){
		return GraphFile.path;
	}
	static UploadedFile saveFile(final String name, final MultipartFile file) throws IOException, NoSuchAlgorithmException{
		if (!file.isEmpty()) {
            try {
                final byte[] bytes = file.getBytes();
                final MessageDigest md = MessageDigest.getInstance("MD5");
                final byte[] thedigest = md.digest(bytes);                
                final File f=new File(path+name);
                
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(f));
                stream.write(bytes);
                stream.close();
                return new UploadedFile(name);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new IllegalStateException("File is empty");
        }
	}
	static UploadedFile saveString(final String file) throws DataFormatException, IOException{
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
        String name="data."+thedigest;
        File f=new File(path+name);
        if(f.exists()){
        	return new UploadedFile(name);
        }
        else{
        	try{
	            BufferedOutputStream stream = 
	                    new BufferedOutputStream(new FileOutputStream(f));
	            stream.write(bytes);
	            stream.close();
	            return new UploadedFile(name);
        	}
        	catch(FileNotFoundException e){
        		LOG.error("file not found!");
        		return null;
        	}
        	catch(IOException e){
        		LOG.error("IO exception");
        		throw e;
        	}    		
        }
		
	}
	static ManifestLoader openFile(final String name) throws Exception{
		final File f=new File(path+name);
        if(!f.exists())throw new FileNotFoundException(path+name);
		return new ManifestLoader(path+name);
	}
	static void openFile(final String name, Graph graph) throws Exception{
		final File f=new File(path+name);
        if(!f.exists())throw new FileNotFoundException(path+name);
		ManifestPropertygraphImpl.convertManifestNDL(path+name, graph);
	}
	
	public static void main(String[] args){
		final String name="interdomain";
		final BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path+name));
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    saveString(CompressEncode.compressEncode(everything));
		    br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

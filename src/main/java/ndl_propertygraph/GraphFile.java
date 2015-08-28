package ndl_propertygraph;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import orca.ndllib.propertygraph.ManifestPropertygraphImpl;

import org.springframework.web.multipart.MultipartFile;

import com.tinkerpop.blueprints.Graph;

public class GraphFile {
	private static String path="data/";
	static UploadedFile saveFile(String name, final MultipartFile file) throws Exception{
		if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] thedigest = md.digest(bytes);                
                File f=new File(path+name);
                if(f.exists()){
                	String newName=name+'.'+thedigest;
                	File newfile=new File(path+newName);
                	if(newfile.exists())
                		return new UploadedFile(newName);
                	else{
                		name=newName;
                		f=newfile;
                	}
                }
                
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(f));
                stream.write(bytes);
                stream.close();
                return new UploadedFile(name);
            } catch (Exception e) {
                throw e;
            }
        } else {
            throw new IllegalStateException("File is empty");
        }
	}
	static ManifestLoader openFile(final String name) throws Exception{
		File f=new File(path+name);
        if(!f.exists())throw new FileNotFoundException(path+name);
		return new ManifestLoader(path+name);
	}
	static void openFile(final String name, Graph graph) throws Exception{
		File f=new File(path+name);
        if(!f.exists())throw new FileNotFoundException(path+name);
		ManifestPropertygraphImpl.convertManifestNDL(path+name, graph);
	}
}

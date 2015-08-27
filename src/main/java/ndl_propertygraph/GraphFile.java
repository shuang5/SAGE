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
	static String saveFile(String name, final MultipartFile file){
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
                		return "file exists, use key: "+newName;
                	else{
                		name=newName;
                		f=newfile;
                	}
                }
                
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(f));
                stream.write(bytes);
                stream.close();
                return "Successfully uploaded " + name + "! " + "You can use key: "+name+" to refer to it!";
            } catch (Exception e) {
                return "Failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "Failed to upload " + name + " because the file was empty.";
        }
	}
	static ManifestLoader openFile(final String name) throws Exception{
		File f=new File(path+name);
        if(!f.exists())throw new FileNotFoundException("File not found!");
		return new ManifestLoader(path+name);
	}
	static void openFile(final String name, Graph graph) throws Exception{
		File f=new File(path+name);
        if(!f.exists())throw new FileNotFoundException("File not found!");
		ManifestPropertygraphImpl.convertManifestNDL(path+name, graph);
	}
}

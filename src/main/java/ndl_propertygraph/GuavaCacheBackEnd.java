package ndl_propertygraph;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import org.springframework.web.multipart.MultipartFile;

import com.tinkerpop.blueprints.Graph;

public class GuavaCacheBackEnd implements BackEnd{
	GraphCache cache=new GraphCache();
	@Override
	public Graph getEntry(String key) {
		return cache.getEntry(key);
	}

	@Override
	public UploadedFile saveEntry(String file) throws DataFormatException, IOException {
		return GraphFile.saveString(file);
	}

	@Override
	public UploadedFile saveEntry(String name, MultipartFile file) throws IOException, NoSuchAlgorithmException {
		return GraphFile.saveFile(name, file);
	}
	

}

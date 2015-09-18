package ndl_propertygraph;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import org.springframework.web.multipart.MultipartFile;

import com.tinkerpop.blueprints.Graph;

public class GuavaCacheBackEnd implements BackEnd{
	final GraphCache cache=new GraphCache();
	@Override
	public Graph getEntry(final String key) {
		return cache.getEntry(key);
	}

	@Override
	public UploadedFile saveEntry(final String file) throws DataFormatException, IOException {
		return GraphFile.saveString(file);
	}

	@Override
	public UploadedFile saveEntry(final String name, final MultipartFile file) throws IOException, NoSuchAlgorithmException {
		String key=name;
		if(KeyCheck.fileExist(name))
			key=KeyCheck.generateNewKey(name);
		return GraphFile.saveFile(key, file);
	}
	

}

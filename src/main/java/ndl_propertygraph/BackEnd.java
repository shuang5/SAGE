package ndl_propertygraph;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import org.springframework.web.multipart.MultipartFile;

import com.tinkerpop.blueprints.Graph;

public interface BackEnd {
	public Graph getEntry(final String key);
	public UploadedFile saveEntry(final String file) throws DataFormatException, IOException;
	public UploadedFile saveEntry(final String name, final MultipartFile file) throws IOException, NoSuchAlgorithmException;
}

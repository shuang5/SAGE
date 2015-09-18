package ndl_propertygraph;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such node")  // 404
public class KeyFormatException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KeyFormatException(final String file) {
		super("name must be alphanumerical!");
	}
    // ...
}
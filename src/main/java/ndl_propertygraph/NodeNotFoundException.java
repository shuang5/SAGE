package ndl_propertygraph;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such node")  // 404
public class NodeNotFoundException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NodeNotFoundException(String id) {
		super("Node "+id+" can not be found!");
	}
    // ...
}
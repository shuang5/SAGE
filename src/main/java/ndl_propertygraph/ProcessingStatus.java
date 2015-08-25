package ndl_propertygraph;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessingStatus extends Throwable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement
    private final String status;


    public ProcessingStatus() {
        status = "UNKNOWN";
    }
    
    public ProcessingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
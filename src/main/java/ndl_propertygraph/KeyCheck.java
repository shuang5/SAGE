package ndl_propertygraph;

import java.io.File;
import java.util.UUID;
import java.util.regex.Pattern;

public class KeyCheck {
	static public boolean keyIsValid(final String key){
		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		boolean hasSpecialChar = p.matcher(key).find();
		return !hasSpecialChar;
	}
	static public boolean fileExist(final String key){
		String path=GraphFile.getPath();
		File f=new File(path+key);
        return f.exists();        
	}
	static public String generateNewKey(String key){
		return key+UUID.randomUUID().toString().replaceAll("-", "");
	}
}

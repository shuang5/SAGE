package ndl_propertygraph;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value="classpath:config.properties",ignoreResourceNotFound=true)
public class MyPropertiesConfig {
	@Value("${backend}")
    private String backend;
	
	@Value("${titanbackend}")
    private String titanbackend;

	@Value("${backenddir}")
    private String backenddir;
	
	@Bean
	public MyProperties MyProperties (){
		MyProperties mp=new MyProperties();
		mp.setBackend(backend);
		mp.setTitanbackend(titanbackend);
		mp.setBackenddir(backenddir);
		return mp;
	}
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

package ndl_propertygraph;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:config.properties")
public class MyPropertiesConfig {
	@Value("${backend}")
    private String backend;
	@Bean
	public MyProperties MyProperties (){
		MyProperties mp=new MyProperties();
		mp.setBackend(backend);
		return mp;
	}
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

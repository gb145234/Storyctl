package fscut.manager.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyPicConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\public\\upload\\";
        registry.addResourceHandler("/picture/**").addResourceLocations("file:"+ path);
    }
}

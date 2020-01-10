package fscut.manager.demo.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedHeaders("*")
				.allowedMethods("*")
				.allowedOrigins("*");
	}
	
	@Override
	protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(new ConcurrentTaskExecutor(
				new ThreadPoolExecutor(3, 3, 0L, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy())
		));
		configurer.setDefaultTimeout(30000);
	}

	/**
	 * 使用swagger2所需配置
	 * @param registry
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");
		String path = System.getProperty("user.dir")+"\\src\\main\\resources\\public\\upload\\";
		registry.addResourceHandler("/picture/**").addResourceLocations("file:"+ path);
	}


	//@Override
	//protected void addInterceptors(InterceptorRegistry registry) {
	//	registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").
	//			excludePathPatterns("/emp/toLogin","/emp/login","/js/**","/css/**","/images/**");
	//	super.addInterceptors(registry);
	//}


}

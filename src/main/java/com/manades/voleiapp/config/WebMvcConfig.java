package com.manades.voleiapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// ResourceHandler per a que totes les peticions desconegudes passin per l'index.html
		registry.
				addResourceHandler(getJsAppStaticFolder() + "/**").
				addResourceLocations("classpath:/static" + getJsAppStaticFolder() + "/").
				resourceChain(true).
				addResolver(new PathResourceResolver() {
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException {
						Resource requestedResource = location.createRelative(resourcePath);
						if (requestedResource.exists() && requestedResource.isReadable()) {
							return requestedResource;
						} else {
							return new ClassPathResource("static" + getJsAppStaticFolder() + "/index.html");
						}
					}
				});
	}

	protected String getJsAppStaticFolder() {
		return "";
	}

}
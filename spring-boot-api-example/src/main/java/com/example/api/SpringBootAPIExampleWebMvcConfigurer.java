package com.example.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.common.request.RequestCheckHandlerInterceptor;
import com.example.common.request.RequestConvertProcessor;

@Configuration
@ComponentScan( basePackages= { "com.example" } )
public class SpringBootAPIExampleWebMvcConfigurer implements WebMvcConfigurer
{
	@Autowired
	private RequestCheckHandlerInterceptor requestCheckHandlerInterceptor;
	@Autowired
	private RequestConvertProcessor requestConvertProcessor;

	@Override
	public void addInterceptors( InterceptorRegistry registry )
	{
		registry.addInterceptor( requestCheckHandlerInterceptor );
	}

	@Override
	public void addArgumentResolvers( List<HandlerMethodArgumentResolver> resolvers )
	{
		resolvers.add( requestConvertProcessor );
	}
}

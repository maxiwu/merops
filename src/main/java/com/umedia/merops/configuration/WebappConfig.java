package com.umedia.merops.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.umedia.merops.DragonflyException;
import com.umedia.merops.client.DragonflyServiceConsumer;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.umedia.merops.controller",
		"com.umedia.merops.configuration" })
@PropertySource("classpath:dragonfly.properties")
public class WebappConfig extends WebMvcConfigurerAdapter {
	
	//for reading properties
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	//json resolver ?
	@Bean
	public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
		ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
		ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
		contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);
		contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
		contentViewResolver.setDefaultViews(Arrays.<View> asList(new MappingJackson2JsonView()));
		return contentViewResolver;
	}
	
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/");
	}

	// wire bean values, service implementation
	@Bean
	public DragonflyServiceConsumer dragonflyServiceConsumer(
			@Value("${dragonflyDeviceListURL}") String dragonflyDeviceListURL) {
		DragonflyServiceConsumer dragonflyservice = new DragonflyServiceConsumer();
		dragonflyservice.setDragonflyDeviceListURL(dragonflyDeviceListURL);

		return dragonflyservice;
	}
	
	@Configuration
	@EnableOAuth2Client
	protected static class ResourceConfig{
		@Value("${accessTokenUri}")
		private String accessTokenUri;
		
		@Value("${userAuthorizationUri}")
		private String userAuthorizationUri;
		
		@Bean
		public OAuth2ProtectedResourceDetails dragonfly()
		{
			//should store in database
			AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
			/*details.setId("dragonfly/merops");
			details.setClientId("my-trusted-client-with-secret");
			details.setClientSecret("somesecret");
			details.setAccessTokenUri(accessTokenUri);
			details.setUserAuthorizationUri(userAuthorizationUri);
			details.setScope(Arrays.asList("read"));*/
			details.setId("sparklr/tonr");
			details.setClientId("tonr");
			details.setClientSecret("secret");
			details.setAccessTokenUri(accessTokenUri);
			details.setUserAuthorizationUri(userAuthorizationUri);
			details.setScope(Arrays.asList("read","write"));
			
			return details;
		}
		
		@Bean
		public OAuth2RestTemplate dragonflyRestTemplate(OAuth2ClientContext clientContext)
		{
			return new OAuth2RestTemplate(dragonfly(), clientContext);
		}
	}
}

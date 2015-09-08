package com.umedia.merops.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebappInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

		ctx.scan("com.umedia.merops.configuration");

		servletContext.addListener(new ContextLoaderListener(ctx));

		ServletRegistration.Dynamic servlet = servletContext.addServlet(
				"dispatcher", new DispatcherServlet(ctx));

		// do not need any security
		/*
		 * DelegatingFilterProxy filter = new
		 * DelegatingFilterProxy("springSecurityFilterChain");
		 * filter.setContextAttribute
		 * ("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher"
		 * ); servletContext.addFilter("springSecurityFilterChain",
		 * filter).addMappingForUrlPatterns(null, false, "/*");
		 */
		// spring, oauth jar 裡面有定義的filter
		//registerProxyFilter(servletContext, "springSecurityFilterChain");
		//registerProxyFilter(servletContext, "oauth2ClientContextFilter");

		servlet.setAsyncSupported(true);
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
	}

	// 可能就是跟redirect授權頁有關係
	private void registerProxyFilter(ServletContext servletContext, String name) {
		DelegatingFilterProxy filter = new DelegatingFilterProxy(name);
		filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
		servletContext.addFilter(name, filter).addMappingForUrlPatterns(null,
				false, "/*");
	}

}

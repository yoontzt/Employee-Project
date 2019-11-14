package com.axonactive.employeecore.restconfiguration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("api")
public class RestConfiguration extends Application {
	public RestConfiguration() {
		BeanConfig beanConfig=new BeanConfig();
		beanConfig.setSchemes(new String[] {"http"});
		beanConfig.setBasePath("employee-core/api");
		beanConfig.setResourcePackage(RestConfiguration.class.getPackage().getName());
		beanConfig.setScan(true);
	}
}

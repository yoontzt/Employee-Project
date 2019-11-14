package com.axonactive.employeecore.util;

import java.security.Principal;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * @author nvmuon
 *
 */
public class LoggerProducer { 

	@Inject 
	Principal principal;

	@Produces
	public Logger getLogger(final InjectionPoint ip)
	{
		ThreadContext.put("userId", principal.getName());
		return LogManager.getLogger(ip.getMember().getDeclaringClass());
	}

}
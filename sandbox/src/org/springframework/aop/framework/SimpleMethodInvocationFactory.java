/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */
 
package org.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Simple MethodInvocationFactory implementation that 
 * constructs a new MethodInvocationImpl on every call.
 * @author Rod Johnson
 * @version $Id$
 */
public class SimpleMethodInvocationFactory implements MethodInvocationFactory {

	public MethodInvocation getMethodInvocation(Object proxy, Method method, Class targetClass, Object target, Object[] args, List interceptorsAndDynamicInterceptionAdvice, AdvisedSupport advised) {
		return new ReflectiveMethodInvocation(
			proxy,
			target,
			method,
			args,
			targetClass,
			interceptorsAndDynamicInterceptionAdvice);
	}
	
	public void release(MethodInvocation invocation) {
		// Not necessary to implement for this implementation
		//	TODO move into AOP Alliance
		//((MethodInvocationImpl) invocation).clear();
	}

}

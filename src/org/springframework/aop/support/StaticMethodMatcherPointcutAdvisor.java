/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.springframework.aop.support;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;

/**
 * Convenient superclass for Advisors that are also static pointcuts.
 * @author Rod Johnson
 * @version $Id$
 */
public abstract class StaticMethodMatcherPointcutAdvisor extends StaticMethodMatcherPointcut implements PointcutAdvisor {

	private Advice advice;
	
	public StaticMethodMatcherPointcutAdvisor() {
	}

	public StaticMethodMatcherPointcutAdvisor(Advice advice) {
		this.advice = advice;
	}

	public void setAdvice(Advice advice) {
		this.advice = advice;
	}

	public Advice getAdvice() {
		return advice;
	}

	public Pointcut getPointcut() {
		return this;
	}

	public boolean isPerInstance() {
		throw new UnsupportedOperationException("perInstance property of Advisor is not yet supported in Spring");
	}

}

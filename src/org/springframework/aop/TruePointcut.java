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

package org.springframework.aop;

import java.io.Serializable;

/**
 * Canonical Pointcut instance that always matches.
 * @author Rod Johnson
 * @version $Id$
 */
class TruePointcut implements Pointcut, Serializable {
	
	public static final TruePointcut INSTANCE = new TruePointcut();
	
	/**
	 * Enforce Singleton
	 */
	private TruePointcut() {
	}

	public ClassFilter getClassFilter() {
		return ClassFilter.TRUE;
	}

	public MethodMatcher getMethodMatcher() {
		return MethodMatcher.TRUE;
	}

	public String toString() {
		return "Pointcut.TRUE";
	}
}
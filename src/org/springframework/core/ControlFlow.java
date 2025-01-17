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

package org.springframework.core;

/**
 * Interface to be implemented by objects that can return information about
 * the current call stack. Useful in AOP (as in AspectJ cflow concept)
 * but not AOP-specific.
 * @author Rod Johnson
 * @version $Id$
 */
public interface ControlFlow {
	
	boolean under(Class clazz);

	/**
	 * Matches whole method name.
	 */
	boolean under(Class clazz, String methodName);
	
	boolean underToken(String token);

}

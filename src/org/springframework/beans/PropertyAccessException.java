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

package org.springframework.beans;

import java.beans.PropertyChangeEvent;

import org.springframework.core.ErrorCoded;

/**
 * Superclass for exceptions related to a property access,
 * such as type mismatch or invocation target exception.
 * @author Rod Johnson
 * @version $Revision$
 */
public abstract class PropertyAccessException extends BeansException implements ErrorCoded {

	private PropertyChangeEvent propertyChangeEvent;

	public PropertyAccessException(String msg, PropertyChangeEvent propertyChangeEvent) {
		super(msg);
		this.propertyChangeEvent = propertyChangeEvent;
	}

	public PropertyAccessException(String msg, PropertyChangeEvent propertyChangeEvent, Throwable ex) {
		super(msg, ex);
		this.propertyChangeEvent = propertyChangeEvent;
	}

	/**
	 * Return the PropertyChangeEvent that resulted in the problem.
	 */
	public PropertyChangeEvent getPropertyChangeEvent() {
		return propertyChangeEvent;
	}

}

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
package org.springframework.web.flow;

import java.util.Collection;

/**
 * A simple interface for accessing attributes - helps prevent accidental
 * misuse/manipulation of more enabling interfaces like Map, for example --
 * through better encapsulation.
 * @author Keith Donald
 */
public interface AttributesAccessor {
    public Object getAttribute(String attributeName);

    public Object getAttribute(String attributeName, Class requiredType) throws IllegalStateException;

    public Object getRequiredAttribute(String attributeName) throws IllegalStateException;

    public Object getRequiredAttribute(String attributeName, Class requiredType) throws IllegalStateException;

    public boolean containsAttribute(String attributeName);
    
    public Collection attributeNames();
    
    public Collection attributeEntries();
    
    public Collection attributeValues();
}
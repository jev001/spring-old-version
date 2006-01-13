/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springframework.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.beans.TestBean;

/**
 * @author Rob Harrop
 */
public class ConventionUtilsTests extends TestCase {

	public void testSimpleObject() {
		TestBean testBean = new TestBean();
		assertEquals("Incorrect singular variable name", "testBean", ConventionUtils.getVariableName(testBean));
	}

	public void testArray() {
		TestBean[] testBeans = new TestBean[0];
		assertEquals("Incorrect plural array form", "testBeanList", ConventionUtils.getVariableName(testBeans));
	}

	public void testCollections() {
		List list = new ArrayList();
		list.add(new TestBean());
		assertEquals("Incorrect plural List form", "testBeanList", ConventionUtils.getVariableName(list));

		Set set = new HashSet();
		set.add(new TestBean());
		assertEquals("Incorrect plural Set form", "testBeanList", ConventionUtils.getVariableName(set));

		List emptyList = new ArrayList();
		assertEquals("List without items should just return class name", "arrayList", ConventionUtils.getVariableName(emptyList));
	}

}

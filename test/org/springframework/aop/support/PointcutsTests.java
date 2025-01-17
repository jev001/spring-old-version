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

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.beans.TestBean;

import junit.framework.TestCase;

/**
 * 
 * @author Rod Johnson
 * @version $Id$
 */
public class PointcutsTests extends TestCase {
	
	public static Method TEST_BEAN_SET_AGE;
	public static Method TEST_BEAN_GET_AGE;
	public static Method TEST_BEAN_GET_NAME;
	public static Method TEST_BEAN_ABSQUATULATE;
	
	static {
		try {
			TEST_BEAN_SET_AGE = TestBean.class.getMethod("setAge", new Class[] { int.class });
			TEST_BEAN_GET_AGE = TestBean.class.getMethod("getAge", null);
			TEST_BEAN_GET_NAME = TestBean.class.getMethod("getName", null);
			TEST_BEAN_ABSQUATULATE = TestBean.class.getMethod("absquatulate", null);
		}
		catch (Exception ex) {
			throw new RuntimeException("Shouldn't happen: error in test suite");
		}
	}
	
	/**
	 * Matches only TestBean class, not subclasses
	 */
	public static Pointcut allTestBeanMethodsPointcut = new StaticMethodMatcherPointcut() {
		public ClassFilter getClassFilter() {
			return new ClassFilter() {
				public boolean matches(Class clazz) {
					return clazz.equals(TestBean.class);
				}
			};
		}

		public boolean matches(Method m, Class targetClass) {
			return true;
		}
	};
		
	public static Pointcut allClassSetterPointcut = new StaticMethodMatcherPointcut() {
		public boolean matches(Method m, Class targetClass) {
			return m.getName().startsWith("set");
		}
	};
	
	// Subclass used for matching
	public static class MyTestBean extends TestBean {
	}
	
	public static Pointcut myTestBeanSetterPointcut = new StaticMethodMatcherPointcut() {
		public ClassFilter getClassFilter() {
			return new RootClassFilter(MyTestBean.class);
		}

		public boolean matches(Method m, Class targetClass) {
			return m.getName().startsWith("set");
		}
	};
	
	// Will match MyTestBeanSubclass
	public static Pointcut myTestBeanGetterPointcut = new StaticMethodMatcherPointcut() {
		public ClassFilter getClassFilter() {
			return new RootClassFilter(MyTestBean.class);
		}

		public boolean matches(Method m, Class targetClass) {
			return m.getName().startsWith("get");
		}
	};
	
	// Still more specific class
	public static class MyTestBeanSubclass extends MyTestBean {
	}
	
	public static Pointcut myTestBeanSubclassGetterPointcut = new StaticMethodMatcherPointcut() {
		public ClassFilter getClassFilter() {
			return new RootClassFilter(MyTestBeanSubclass.class);
		}

		public boolean matches(Method m, Class targetClass) {
			return m.getName().startsWith("get");
		}
	};
	
	public static Pointcut allClassGetterPointcut = new StaticMethodMatcherPointcut() {
		public boolean matches(Method m, Class targetClass) {
			return m.getName().startsWith("get");
		}
	};
	
	public static Pointcut allClassGetAgePointcut = new StaticMethodMatcherPointcut() {
		public boolean matches(Method m, Class targetClass) {
			return m.getName().equals("getAge");
		}
	};
	
	public static Pointcut allClassGetNamePointcut = new StaticMethodMatcherPointcut() {
		public boolean matches(Method m, Class targetClass) {
			return m.getName().equals("getName");
		}
	};
	
	
	public PointcutsTests(String s) {
		super(s);
	}
	
	public void testTrue() {
		assertTrue(Pointcuts.matches(Pointcut.TRUE, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertTrue(Pointcuts.matches(Pointcut.TRUE, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertTrue(Pointcuts.matches(Pointcut.TRUE, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
		assertTrue(Pointcuts.matches(Pointcut.TRUE, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertTrue(Pointcuts.matches(Pointcut.TRUE, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertTrue(Pointcuts.matches(Pointcut.TRUE, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
	}

	public void testMatches() {
		assertTrue(Pointcuts.matches(allClassSetterPointcut, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertFalse(Pointcuts.matches(allClassSetterPointcut, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(allClassSetterPointcut, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
		assertFalse(Pointcuts.matches(allClassGetterPointcut, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertTrue(Pointcuts.matches(allClassGetterPointcut, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(allClassGetterPointcut, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
	}
	
	/**
	 * Should match all setters and getters on any class
	 */
	public void testUnionOfSettersAndGetters() {
		Pointcut union = Pointcuts.union(allClassGetterPointcut, allClassSetterPointcut);
		assertTrue(Pointcuts.matches(union, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(union, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
	}
	
	public void testUnionOfSpecificGetters() {
		Pointcut union = Pointcuts.union(allClassGetAgePointcut, allClassGetNamePointcut);
		assertFalse(Pointcuts.matches(union, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(allClassGetAgePointcut, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertFalse(Pointcuts.matches(union, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
		
		// Union with all setters
		union = Pointcuts.union(union, allClassSetterPointcut);
		assertTrue(Pointcuts.matches(union, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(allClassGetAgePointcut, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertFalse(Pointcuts.matches(union, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
		
		assertTrue(Pointcuts.matches(union, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
	}
	
	/**
	 * Tests vertical composition. First pointcut matches all setters.
	 * Second one matches all getters in the MyTestBean class. TestBean getters shouldn't pass.
	 */
	public void testUnionOfAllSettersAndSubclassSetters() {
		assertFalse(Pointcuts.matches(myTestBeanSetterPointcut, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertTrue(Pointcuts.matches(myTestBeanSetterPointcut, TEST_BEAN_SET_AGE, MyTestBean.class, new Object[] { new Integer(6)}));
		assertFalse(Pointcuts.matches(myTestBeanSetterPointcut, TEST_BEAN_GET_AGE, TestBean.class, null));
		
		Pointcut union = Pointcuts.union(myTestBeanSetterPointcut, allClassGetterPointcut);
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_AGE, MyTestBean.class, null));
		// Still doesn't match superclass setter
		assertTrue(Pointcuts.matches(union, TEST_BEAN_SET_AGE, MyTestBean.class, new Object[] { new Integer(6)}));
		assertFalse(Pointcuts.matches(union, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
	}
	
	/**
	 * Intersection should be MyTestBean getAge() only:
	 * it's the union of allClassGetAge and subclass getters
	 */
	public void testIntersectionOfSpecificGettersAndSubclassGetters() {
		assertTrue(Pointcuts.matches(allClassGetAgePointcut, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertTrue(Pointcuts.matches(allClassGetAgePointcut, TEST_BEAN_GET_AGE, MyTestBean.class, null));
		assertFalse(Pointcuts.matches(myTestBeanGetterPointcut, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertFalse(Pointcuts.matches(myTestBeanGetterPointcut, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertTrue(Pointcuts.matches(myTestBeanGetterPointcut, TEST_BEAN_GET_NAME, MyTestBean.class, null));
		assertTrue(Pointcuts.matches(myTestBeanGetterPointcut, TEST_BEAN_GET_AGE, MyTestBean.class, null));
		
		Pointcut intersection = Pointcuts.intersection(allClassGetAgePointcut, myTestBeanGetterPointcut);
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_NAME, MyTestBean.class, null));
		assertTrue(Pointcuts.matches(intersection, TEST_BEAN_GET_AGE, MyTestBean.class, null));
		// Matches subclass of MyTestBean
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_NAME, MyTestBeanSubclass.class, null));
		assertTrue(Pointcuts.matches(intersection, TEST_BEAN_GET_AGE, MyTestBeanSubclass.class, null));
		
		// Now intersection with MyTestBeanSubclass getters should eliminate MyTestBean target
		intersection = Pointcuts.intersection(intersection, myTestBeanSubclassGetterPointcut);
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_NAME, MyTestBean.class, null));
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_AGE, MyTestBean.class, null));
		// Still matches subclass of MyTestBean
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_NAME, MyTestBeanSubclass.class, null));
		assertTrue(Pointcuts.matches(intersection, TEST_BEAN_GET_AGE, MyTestBeanSubclass.class, null));
		
		// Now union with all TestBean methods
		Pointcut union = Pointcuts.union(intersection, allTestBeanMethodsPointcut);
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_NAME, TestBean.class, null));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(union, TEST_BEAN_GET_NAME, MyTestBean.class, null));
		assertFalse(Pointcuts.matches(union, TEST_BEAN_GET_AGE, MyTestBean.class, null));
		// Still matches subclass of MyTestBean
		assertFalse(Pointcuts.matches(union, TEST_BEAN_GET_NAME, MyTestBeanSubclass.class, null));
		assertTrue(Pointcuts.matches(union, TEST_BEAN_GET_AGE, MyTestBeanSubclass.class, null));
		
		assertTrue(Pointcuts.matches(union, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
		assertFalse(Pointcuts.matches(union, TEST_BEAN_ABSQUATULATE, MyTestBean.class, null));
	}
	
	
	/**
	 * The intersection of these two pointcuts leaves nothing.
	 */
	public void testSimpleIntersection() {
		Pointcut intersection = Pointcuts.intersection(allClassGetterPointcut, allClassSetterPointcut);
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_SET_AGE, TestBean.class, new Object[] { new Integer(6)}));
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_GET_AGE, TestBean.class, null));
		assertFalse(Pointcuts.matches(intersection, TEST_BEAN_ABSQUATULATE, TestBean.class, null));
	}

}

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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Singleton factory to conceal automatic choice of Java 1.4 or 1.3
 * ControlFlow implementation class. We want to use the more efficient
 * Java 1.4 StackTraceElement if we can, and we don't want to impose
 * a runtime dependency on 1.4.
 * @author Rod Johnson
 * @version $Id$
 */
public abstract class ControlFlowFactory {
	
	public static ControlFlow createControlFlow() {
		return JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_14 ?
					(ControlFlow) new Jdk14ControlFlow() :
					(ControlFlow) new Jdk13ControlFlow();
	}


	/**
	 * Utilities for cflow-style pointcuts. Note that such pointcuts are
	 * 5-10 times more expensive to evaluate than other pointcuts, as they require
	 * analysis of the stack trace (through constructing a new throwable).
	 * However, they are useful in some cases.
	 * <p>This implementation uses the StackTraceElement class introduced in Java 1.4.
	 * @author Rod Johnson
	 * @version $Id$
	 */
	static class Jdk14ControlFlow implements ControlFlow {

		private StackTraceElement[] stack;

		public Jdk14ControlFlow() {
			stack = new Throwable().getStackTrace();
		}

		public boolean under(Class clazz) {
			String className = clazz.getName();
			for (int i = 0; i < stack.length; i++) {
				//System.out.println(stack[i]);
				if (stack[i].getClassName().equals(className)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Matches whole method name
		 * @param clazz
		 * @param methodName
		 * @return
		 */
		public boolean under(Class clazz, String methodName) {
			String className = clazz.getName();
			for (int i = 0; i < stack.length; i++) {
				if (stack[i].getClassName().equals(className) && stack[i].getMethodName().equals(methodName)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Leave it up to the caller to decide what matches.
		 * Caller must understand stack trace format, so there's less abstraction.
		 * @param token
		 * @return
		 */
		public boolean underToken(String token) {
			StringWriter sw = new StringWriter();
			new Throwable().printStackTrace(new PrintWriter(sw));
			//System.err.println(sw);
			String stackTrace = sw.toString();
			return stackTrace.indexOf(token) != -1;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer("Jdk14ControlFlow: ");
			for (int i = 0; i < stack.length; i++) {
				if (i > 0)
					sb.append("\n\t@");
				sb.append(stack[i]);
			}
			return sb.toString();
		}
	}


	/**
	 * Java 1.3 version of utilities for cflow-style pointcuts. We can't
	 * rely on the Java 1.4 StackTraceElement class.
	 * <p>Note that such pointcuts are 10-15 times more expensive to evaluate under
	 * JDK 1.3 than other pointcuts, as they require analysis of the stack trace
	 * (through constructing a new throwable). However, they are useful in some cases.
	 * @author Rod Johnson
	 * @version $Id$
	 */
	static class Jdk13ControlFlow implements ControlFlow {

		private String stackTrace;


		public Jdk13ControlFlow() {
			StringWriter sw = new StringWriter();
			new Throwable().printStackTrace(new PrintWriter(sw));
			stackTrace = sw.toString();
		}

		public boolean under(Class clazz) {
			return stackTrace.indexOf(clazz.getName()) != -1;
		}

		/**
		 * Matches whole method name
		 * @param clazz
		 * @param methodName
		 * @return
		 */
		public boolean under(Class clazz, String methodName) {
			return stackTrace.indexOf(clazz.getName() + "." + methodName + "(") != -1;
		}

		/**
		 * Leave it up to the caller to decide what matches.
		 * Caller must understand stack trace format, so there's less abstraction.
		 * @param token
		 * @return
		 */
		public boolean underToken(String token) {
			return stackTrace.indexOf(token) != -1;
		}
	}

}

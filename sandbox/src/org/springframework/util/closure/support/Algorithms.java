/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.util.closure.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.util.closure.Closure;
import org.springframework.util.closure.Constraint;
import org.springframework.util.closure.ProcessTemplate;

/**
 * Convenience utility class which provides a number of algorithms involving
 * function objects such as closures and constraints.
 * 
 * @author Keith Donald
 */
public class Algorithms {
	/**
	 * The shared instance.
	 */
	private static Algorithms INSTANCE = new Algorithms();

	/**
	 * Load the shared instance
	 * @param instance
	 */
	public static void load(Algorithms instance) {
		INSTANCE = instance;
	}

	/**
	 * Singleton instance accessor
	 * 
	 * @return The algorithms instance
	 */
	public static Algorithms instance() {
		return INSTANCE;
	}

	/**
	 * Returns true if any elements in the given collection meet the specified
	 * predicate condition.
	 * 
	 * @param collection
	 * @param constraint
	 * @return true or false
	 */
	public boolean anyTrue(Collection collection, Constraint constraint) {
		return anyTrue(collection.iterator(), constraint);
	}

	/**
	 * Returns true if any elements in the given collection meet the specified
	 * predicate condition.
	 * 
	 * @param collection
	 * @param constraint
	 * @return true or false
	 */
	public boolean anyTrue(Iterator it, Constraint constraint) {
		return findFirst(it, constraint) != null;
	}

	/**
	 * Returns true if all elements in the given collection meet the specified
	 * predicate condition.
	 * 
	 * @param collection
	 * @param constraint
	 * @return true or false
	 */
	public boolean allTrue(Collection collection, Constraint constraint) {
		return allTrue(collection.iterator(), constraint);
	}

	/**
	 * @param it
	 * @param constraint
	 * @return
	 */
	public boolean allTrue(Iterator it, Constraint constraint) {
		return new IteratorProcessTemplate(it).allTrue(constraint);
	}

	/**
	 * Find the first element in the collection matching the specified
	 * constraint.
	 * 
	 * @param collection the collection
	 * @param constraint the predicate
	 * @return The first object match, or null if no match
	 */
	public Object findFirst(Collection collection, Constraint constraint) {
		return findFirst(collection.iterator(), constraint);
	}

	/**
	 * Find the first element in the collection matching the specified
	 * constraint.
	 * 
	 * @param collection the collection
	 * @param constraint the predicate
	 * @return The first object match, or null if no match
	 */
	public Object findFirst(Iterator it, Constraint constraint) {
		return new IteratorProcessTemplate(it).findFirst(constraint);
	}

	/**
	 * Find all the elements in the collection that match the specified
	 * constraint.
	 * 
	 * @param collection
	 * @param constraint
	 * @return The objects that match, or a empty collection if none match
	 */
	public Collection findAll(Collection collection, Constraint constraint) {
		return findAll(collection.iterator(), constraint);
	}

	/**
	 * Find all the elements in the collection that match the specified
	 * constraint.
	 * 
	 * @param collection
	 * @param constraint
	 * @return The objects that match, or a empty collection if none match
	 */
	public Collection findAll(Iterator it, Constraint constraint) {
		ProcessTemplate finder = new IteratorProcessTemplate(it).findAll(constraint);
		final Collection results = new ArrayList();
		finder.run(new Block() {
			protected void handle(Object element) {
				results.add(element);
			}
		});
		return results;
	}

	/**
	 * Execute the provided closure for each element in the collection.
	 * 
	 * @param collection
	 * @param closure
	 */
	public void forEach(Collection collection, Closure closure) {
		forEach(collection.iterator(), closure);
	}

	/**
	 * Execute the provided closure for each element in the collection.
	 * 
	 * @param collection
	 * @param closure
	 */
	public void forEach(Iterator it, Closure closure) {
		new IteratorProcessTemplate(it).run(closure);
	}
}
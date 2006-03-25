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

package org.springframework.web.servlet.tags.form;

import org.springframework.web.servlet.support.BindStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.core.enums.LabeledEnum;

import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.beans.PropertyEditor;

/**
 * @author Rob Harrop
 * @since 2.0
 */
final class SelectedValueComparator {

	private SelectedValueComparator() {
	}

	public static boolean isSelected(BindStatus bindStatus, Object candidateValue) {
		Assert.notNull(candidateValue, "'candidateValue' cannot be null.");
		Object boundValue = getBoundValue(bindStatus);

		if (boundValue == null) {
			return false;
		}

		if (boundValue instanceof Collection) {
			Collection boundCollection = (Collection) boundValue;
			if (boundCollection.contains(boundValue)) {
				return true;
			}
			else {
				return exhaustiveCollectionCompare(boundCollection, candidateValue, bindStatus.getEditor());
			}
		}
		else if (boundValue instanceof Map) {
			Map boundMap = (Map) boundValue;
			if (boundMap.containsKey(boundValue)) {
				return true;
			}
			else {
				return exhaustiveCollectionCompare(boundMap.keySet(), candidateValue, bindStatus.getEditor());
			}
		}
		else {
			if (ObjectUtils.nullSafeEquals(boundValue, candidateValue)) {
				return true;
			}
			else {
				return exhaustiveCompare(boundValue, candidateValue, bindStatus.getEditor());
			}
		}
	}

	private static Object getBoundValue(BindStatus bindStatus) {
		if(bindStatus.getEditor() != null) {
			Object editorValue = bindStatus.getEditor().getValue();
			if(editorValue != null) {
				return editorValue;
			}
		}
		return bindStatus.getValue();
	}

	private static boolean exhaustiveCollectionCompare(Collection collection, Object candidateValue, PropertyEditor propertyEditor) {
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object o = iterator.next();
			if (exhaustiveCompare(o, candidateValue, propertyEditor)) {
				return true;
			}
		}
		return false;
	}

	private static boolean exhaustiveCompare(Object value, Object candidate, PropertyEditor propertyEditor) {

		if (value instanceof LabeledEnum) {
			String enumCodeAtString = ObjectUtils.nullSafeToString(((LabeledEnum) value).getCode());
			if (enumCodeAtString.equals(ObjectUtils.nullSafeToString(candidate))) {
				return true;
			}
		}
		else if (ObjectUtils.nullSafeToString(value).equals(ObjectUtils.nullSafeToString(candidate))) {
			return true;
		}
		else if (propertyEditor != null && candidate instanceof String) {
			// try PE-based comparison (PE should *not* be allowed to escape creating thread)
			Object originalValue = propertyEditor.getValue();
			try {
				propertyEditor.setAsText((String)candidate);
				return ObjectUtils.nullSafeEquals(value, propertyEditor.getValue());
			}
			finally {
				propertyEditor.setValue(originalValue);
			}
		}
		return false;
	}
}

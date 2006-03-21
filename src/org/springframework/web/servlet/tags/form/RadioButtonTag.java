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

import org.springframework.util.ObjectUtils;

import javax.servlet.jsp.JspException;

/**
 * Databinding-aware JSP tag for rendering an HTML '<code>input</code>'
 * element with a '<code>type</code>' of '<code>radio</code>'.
 * <p/>
 * Rendered elements are marked as 'checked' if the configured
 * {@link #setValue(Object) value} matches the {@link #getValue bound value}.
 * <p/>
 * A typical usage pattern will involved multiple tag instances bound
 * to the same property but with different values.
 * 
 * @author Rob Harrop
 * @since 2.0
 */
public class RadioButtonTag extends AbstractHtmlInputElementTag {

	/**
	 * The value of the '<code>value</code>' attribute.
	 */
	private Object value;

	/**
	 * Sets the value of the '<code>value</code>' attribute.
	 * May be a runtime expression.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Renders the '<code>input(radio)</code>' element with the configured
	 * {@link #setValue(Object) value}. Marks the element as checked if the
	 * value matches the {@link #getValue bound value}.
	 */
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("input");
		writeDefaultAttributes(tagWriter);
		tagWriter.writeAttribute("type", "radio");

		Object resolvedValue = (this.value instanceof String ? evaluate("value", (String)this.value) : this.value);
		tagWriter.writeAttribute("value", ObjectUtils.nullSafeToString(resolvedValue));

		if (isActiveValue(resolvedValue)) {
			tagWriter.writeAttribute("checked", "true");
		}

		tagWriter.endTag();
		return EVAL_PAGE;
	}

}

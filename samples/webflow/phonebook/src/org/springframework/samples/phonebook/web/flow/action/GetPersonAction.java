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
package org.springframework.samples.phonebook.web.flow.action;

import org.springframework.samples.phonebook.domain.Person;
import org.springframework.samples.phonebook.domain.PhoneBook;
import org.springframework.web.flow.Event;
import org.springframework.web.flow.FlowExecutionContext;
import org.springframework.web.flow.action.AbstractAction;

public class GetPersonAction extends AbstractAction {

	private PhoneBook phoneBook;

	public void setPhoneBook(PhoneBook phoneBook) {
		this.phoneBook = phoneBook;
	}

	protected Event doExecuteAction(FlowExecutionContext context) throws Exception {
		Long id = (Long)context.getFlowScope().getRequiredAttribute("id", Long.class);
		Person person = phoneBook.getPerson(id);
		if (person != null) {
			context.getRequestScope().setAttribute("person", person);
			return success();
		}
		else {
			return error();
		}
	}
}
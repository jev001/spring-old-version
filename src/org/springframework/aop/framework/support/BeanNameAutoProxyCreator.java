package org.springframework.aop.framework.support;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Auto proxy creator that identifies beans to proxy via a list of names.
 * Checks for direct, "xxx*", and "*xxx" matches.
 * @author Juergen Hoeller
 * @since 10.10.2003
 * @see #setBeanNames
 * @see #isMatch
 */
public class BeanNameAutoProxyCreator extends AbstractAutoProxyCreator {

	private List beanNames;

	/**
	 * Set the names of the beans that should automatically get wrapped with proxies.
	 * A name can specify a prefix to match by ending with "*", e.g. "myBean,tx*"
	 * will match the bean named "myBean" and all beans whose name start with "tx".
	 */
	public void setBeanNames(String[] beanNames) {
		this.beanNames = Arrays.asList(beanNames);
	}

	/**
	 * Identify as bean to proxy if the bean name is in the configured list of names.
	 */
	protected Object[] getInterceptorsAndPointcutsForBean(Object bean, String beanName) {
		if (this.beanNames != null) {
			if (this.beanNames.contains(beanName)) {
				return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
			}
			for (Iterator it = this.beanNames.iterator(); it.hasNext();) {
				String mappedName = (String) it.next();
				if (isMatch(beanName, mappedName)) {
					return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
				}
			}
		}
		return DO_NOT_PROXY;
	}

	/**
	 * Return if the given bean name matches the mapped name.
	 * The default implementation checks for "xxx*" and "*xxx" matches.
	 * Can be overridden in subclasses.
	 * @param beanName the bean name to check
	 * @param mappedName the name in the configured list of names
	 * @return if the names match
	 */
	protected boolean isMatch(String beanName, String mappedName) {
		return (mappedName.endsWith("*") && beanName.startsWith(mappedName.substring(0, mappedName.length() - 1))) ||
				(mappedName.startsWith("*") && beanName.endsWith(mappedName.substring(1, mappedName.length())));
	}

}

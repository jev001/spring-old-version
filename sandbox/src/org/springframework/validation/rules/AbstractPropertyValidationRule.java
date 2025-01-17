/*
 * The Spring Framework is published under the terms of the Apache Software
 * License.
 */
package org.springframework.validation.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.ArrayUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.ToStringBuilder;
import org.springframework.validation.Errors;
import org.springframework.validation.PropertyValidationRule;

/**
 * Convenience super class for PropertyValidationRules. Rules are only required
 * to implement the <code>validate</code> method, which encapsulates the
 * business logic for enforcing the property value constraint.
 * 
 * @author Keith Donald
 */
public abstract class AbstractPropertyValidationRule
    implements PropertyValidationRule {
    protected static Log logger =
        LogFactory.getLog(PropertyValidationRule.class);

    private String TYPING_HINT_PREFIX = "typingHints.";
    private String ERROR_PREFIX = "errors.";

    public abstract boolean validate(Object context, Object value);

    public MessageSourceResolvable createTypingHint(String propertyNamePath) {
        return new DefaultMessageSourceResolvable(
            new String[] { getTypingHintCode()},
            getTypingHintArguments());
    }

    public MessageSourceResolvable createErrorMessage(String propertyNamePath) {
        if (logger.isDebugEnabled()) {
            logger.debug(
                "Creating resolvable error message for property '"
                    + propertyNamePath
                    + "'");
        }
        DefaultMessageSourceResolvable resolvable =
            new DefaultMessageSourceResolvable(
                new String[] { getErrorCode()},
                getErrorArguments(propertyNamePath));
        if (logger.isDebugEnabled()) {
            logger.debug("Resolvable error message is " + resolvable);
        }
        return resolvable;
    }

    public void invokeRejectValue(Errors errors, String nestedPath) {
        Assert.notNull(errors);
        Assert.notNull(nestedPath);
        String objectName = errors.getObjectName();
        errors.rejectValue(
            nestedPath,
            getErrorCode(),
            getErrorArguments(
                (objectName != null
                    ? objectName + '.' + nestedPath
                    : nestedPath)),
            null);
    }

    /**
     * Returns the rule type, used to lookup rule messages. By default, the
     * ruleType is the uncapitalized short rule class name. For example, the
     * rule <code>org.springframework.validation.rules.Required</code> rule
     * type would be <code>required</code>.
     * 
     * @return The rule type.
     */
    protected String getType() {
        return StringUtils.uncapitalize(ClassUtils.getShortName(getClass()));
    }

    /**
     * Returns the typing hint message arguments.
     * 
     * @return The typing hint arguments.
     */
    protected Object[] getTypingHintArguments() {
        return getErrorArguments();
    }

    /**
     * Returns the typing hint message code.
     * 
     * @return The typing hint message code.
     */
    protected String getTypingHintCode() {
        return TYPING_HINT_PREFIX + getType();
    }

    /**
     * Returns the error message code.
     * 
     * @return The error message code.
     */
    protected String getErrorCode() {
        return ERROR_PREFIX + getType();
    }

    /**
     * Returns the error message arguments. By default, every rule will use the
     * property name as the first {0} argument. For example, "Name is
     * required." Thus this method should only return rule specific arguments.
     * 
     * @return The rule specific error message arguments.
     */
    protected Object[] getErrorArguments() {
        return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }

    /**
     * Returns the full array of message arguments, where the first element in
     * the array is a resolvable property name message and the remaining
     * arguments are rule-specific message arguments.
     * 
     * @return The error message arguments.
     */
    protected Object[] getErrorArguments(String propertyNamePath) {
        Object[] ruleErrorArgs = getErrorArguments();
        Object[] errorArgs = new Object[ruleErrorArgs.length + 1];
        errorArgs[0] = buildPropertyMessageSourceResolvable(propertyNamePath);
        System.arraycopy(ruleErrorArgs, 0, errorArgs, 1, ruleErrorArgs.length);
        return errorArgs;
    }

    /**
     * Builds a resolvable property name message from a provided property name
     * path. The following message codes are tried:
     * 
     * <pre>
     *  [propertyNamePath]
     *  [parentBeanPrefix].[propertyName]
     *  [propertyName]
     * </pre>
     * 
     * 
     * 
     * For example, property name path <code>pet.name.lastName</code> would
     * correspond to codes:
     * 
     * <pre>
     *  pet.name.lastName
     *  name.lastName
     *  lastName
     * </pre>
     * 
     * 
     * 
     * As another example, the property name path <code>pet.name.lastName.junior</code>
     * would correspond to codes:
     * 
     * <pre>
     *  pet.name.lastName.junior lastName.junior junior
     * </pre>
     * 
     * @return The resolvable property name message.
     */
    protected MessageSourceResolvable buildPropertyMessageSourceResolvable(String propertyNamePath) {
        String[] propertyCodes;
        String propertyName;
        String[] tokens =
            StringUtils.delimitedListToStringArray(propertyNamePath, ".");
        if (tokens.length == 1) {
            propertyName = propertyNamePath;
            propertyCodes = new String[] { propertyName };
        } else {
            propertyName = tokens[tokens.length - 1];
            if (tokens.length > 2) {
                String beanPropertyPath = tokens[tokens.length - 2];
                propertyCodes =
                    new String[] {
                        propertyNamePath,
                        beanPropertyPath + '.' + propertyName,
                        propertyName };
            } else {
                propertyCodes = new String[] { propertyNamePath, propertyName };
            }
        }
        DefaultMessageSourceResolvable resolvable =
            new DefaultMessageSourceResolvable(propertyCodes, null, null);
        return resolvable;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("type", getType())
            .append("errorCode", getErrorCode())
            .append("errorArguments", getErrorArguments())
            .append("typingHintCode", getTypingHintCode())
            .append("typingHintArguments", getTypingHintArguments())
            .toString();
    }

}

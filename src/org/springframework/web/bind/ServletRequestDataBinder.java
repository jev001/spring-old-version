/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package org.springframework.web.bind;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Use this class to perform manual data binding from servlet request
 * parameters to JavaBeans, including support for multipart files.
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class ServletRequestDataBinder extends DataBinder {

	private boolean bindEmptyMultipartFiles = true;

	/**
	 * Create a new DataBinder instance.
	 * @param target target object to bind onto
	 * @param name name of the target object
	 */
	public ServletRequestDataBinder(Object target, String name) {
		super(target, name);
	}

	/**
	 * Set whether to bind empty MultipartFile parameters. Default is true.
	 * <p>Turn this off if you want to keep an already bound MultipartFile
	 * when the user resubmits the form without choosing a different file.
	 * Else, the already bound MultipartFile will be replaced by an empty
	 * MultipartFile holder.
	 */
	public void setBindEmptyMultipartFiles(boolean bindEmptyMultipartFiles) {
		this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
	}

	/**
	 * Bind the parameters of the given request to this binder's target,
	 * also binding multipart files in case of a multipart request.
	 * <p>This call can create field errors, representing basic binding
	 * errors like a required field (code "required"), or type mismatch
	 * between value and bean property (code "typeMismatch").
	 * <p>Multipart files are bound via their parameter name, just like normal
	 * HTTP parameters: i.e. "uploadedFile" to an "uploadedFile" bean property,
	 * invoking a "setUploadedFile" setter method.
	 * <p>The type of the target property for a multipart file can be MultipartFile,
	 * byte[], or String. The latter two receive the contents of the uploaded file;
	 * all metadata like original file name, content type, etc are lost in those cases.
	 * @param request request with parameters to bind (can be multipart)
	 * @see org.springframework.web.multipart.MultipartHttpServletRequest
	 * @see org.springframework.web.multipart.MultipartFile
	 */
	public void bind(ServletRequest request) {
		// bind normal HTTP parameters
		bind(new ServletRequestParameterPropertyValues(request));

		// bind multipart files
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map fileMap = multipartRequest.getFileMap();
			MutablePropertyValues pvs = new MutablePropertyValues();
			for (Iterator it = fileMap.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				MultipartFile value = (MultipartFile) fileMap.get(key);
				if (this.bindEmptyMultipartFiles || !value.isEmpty()) {
					pvs.addPropertyValue(key, value);
				}
			}
			bind(pvs);
		}
	}

	/**
	 * Treats errors as fatal. Use this method only if
	 * it's an error if the input isn't valid.
	 * This might be appropriate if all input is from dropdowns, for example.
	 * @throws ServletRequestBindingException subclass of ServletException on any binding problem
	 */
	public void closeNoCatch() throws ServletRequestBindingException {
		if (getErrors().hasErrors()) {
			throw new ServletRequestBindingException("Errors binding onto object '" + getErrors().getObjectName() + "'",
																							 getErrors());
		}
	}

}

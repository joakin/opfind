
package es.opfind.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.validator.*;

public class EmailValidator implements Validator{

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String stringValue = org.apache.commons.lang.StringUtils.defaultString((String) value);
		
		org.apache.commons.validator.EmailValidator emailValidator = org.apache.commons.validator.EmailValidator.getInstance();
		if ( ! emailValidator.isValid(stringValue))
			throw new ValidatorException(new FacesMessage("Debe escribir un email valido"));		
	}




		


}

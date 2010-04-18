package es.opfind.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import es.opfind.controller.civilJob.ListCivilJobs;
import es.opfind.util.StringUtils;

public class UniqueSuscriptionValidatior implements Validator {

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String stringValue = org.apache.commons.lang.StringUtils.defaultString((String) value);

		ValueBinding binding = context.getApplication().createValueBinding("#{listCivilJobs}");
		ListCivilJobs civilJobs = (ListCivilJobs) binding.getValue(context);

		if (stringValue != null && !stringValue.equals(""))
			if (civilJobs.getSearch() != null && !civilJobs.getSearch().isEmpty()) {
				binding = context.getApplication().createValueBinding("#{superSpringValidator}");
				SuperSpringValidator springValidator = (SuperSpringValidator) binding.getValue(context);
				Boolean validate = springValidator.validateUniqueSubscription(stringValue, StringUtils
						.buildLuceneAndQuery(civilJobs.getSearch()));
				if (!validate)
					throw new ValidatorException(new FacesMessage("Ya esta suscrito a esta busqueda"));
			} else {
				throw new ValidatorException(new FacesMessage("Se esta intentado suscribir a una busqueda vacia"));
			}
		else {
			throw new ValidatorException(new FacesMessage("El mail es requerido"));
		}
	}

}

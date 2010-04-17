package es.opfind.util;

import java.util.Locale; 
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class MessageBundle {

	
	public static String getMessageFromResourceBundle(String key, FacesContext facesContext) {
		ResourceBundle bundle = null;
		String bundleName = "i18n.LocalizationResources";
		String message = "";
		Locale locale = facesContext.getViewRoot()
				.getLocale();
		try {
			bundle = ResourceBundle.getBundle(bundleName,locale);
		} catch (MissingResourceException e) {
			// bundle with this name not found;
		}
		if (bundle == null)
			return null;
		try {
			message = bundle.getString(key);
		} catch (Exception e) {
		}
		return message;
	}	
}

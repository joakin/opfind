package es.opfind.validator;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import es.opfind.domain.OPNewsletter;
import es.opfind.service.OPNewsletterMgr;

@Controller  
@Scope("request")
public class SuperSpringValidator implements Serializable {

	@Resource
	private OPNewsletterMgr opNewsletterMgr; 
	
	public Boolean validateUniqueSubscription(String email, String search) {
		List<OPNewsletter> newsletters = opNewsletterMgr.getOPNewslettersByEmailAndSearchText(email, search);
		if(newsletters.equals(""))
			return true;
		else 
			return false;
	}

	
}

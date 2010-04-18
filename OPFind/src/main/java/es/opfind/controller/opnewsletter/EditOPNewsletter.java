package es.opfind.controller.opnewsletter;

import java.util.List;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import es.opfind.controller.civilJob.ListCivilJobs;
import es.opfind.domain.CivilJob;
import es.opfind.domain.OPNewsletter;
import es.opfind.service.CivilJobMgr;
import es.opfind.service.OPNewsletterMgr;
import es.opfind.util.StringUtils;

@Controller
@Scope("request")
public class EditOPNewsletter {

	@Resource
	private OPNewsletterMgr opNewsletterMgr;

	@Resource
	private CivilJobMgr civilJobMgr;

	// TODO: tenemos que validar este mail, como required, como bien formado y
	// como unico en base de datos
	private String mail;
	private Integer status = 1;

	
	public String getResetStatus(){
		status = 1;
		return "";
	}
	public void subscribe(ActionEvent event){

		ValueBinding binding = getFacesContext().getApplication().createValueBinding("#{listCivilJobs}");
		ListCivilJobs civilJobs = (ListCivilJobs) binding.getValue(getFacesContext());

		OPNewsletter opNewsletter = opNewsletterMgr.newOpNewsletter(mail, StringUtils.buildLuceneAndQuery(civilJobs
				.getSearch()));

		opNewsletterMgr.persist(opNewsletter);

		status = 3;
	}

	public String getUnSubscribe() {

		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();
        String searchText="";
		if (request.getParameter("uuid") != null && !request.getParameter("uuid").equals("")) {
			OPNewsletter opNewsletter = opNewsletterMgr.getOPNewsletterByUuid(request.getParameter("uuid"));
			if (opNewsletter != null) {
                searchText=opNewsletter.getSearchText();
				opNewsletterMgr.delete(opNewsletter);
			}
		}
		return searchText;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	private FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public void changeSecondStatus(ActionEvent event){
		status = 2;
	}
}

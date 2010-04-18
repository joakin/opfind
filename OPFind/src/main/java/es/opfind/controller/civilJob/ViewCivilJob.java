package es.opfind.controller.civilJob;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import es.opfind.domain.CivilJob;
import es.opfind.service.CivilJobMgr;

@Controller  
@Scope("session")
public class ViewCivilJob {

	@Resource
	private CivilJobMgr civilJobMgr;
	
	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String back(){
		FacesContext context = FacesContext.getCurrentInstance();
		ValueBinding binding = context.getApplication().createValueBinding("#{listCivilJobs}");
		ListCivilJobs civilJobs = (ListCivilJobs) binding.getValue(context);
		
		if ( civilJobs.getSearch()!= null && !civilJobs.getSearch().isEmpty())
			return "search";
		else
			return "index";
	}
	
	public void view(ActionEvent event){
		this.id = (Integer)event.getComponent().getAttributes().get("civilJob_id");
	 }
	
	public CivilJob getCivilJob(){
		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();
		
		if ( request.getParameter("num")!= null && !request.getParameter("num").equals("") ){
			CivilJob job = civilJobMgr.getCivilJobByNum(request.getParameter("num"));
			if ( job!=null)
				return job;
		}
		return civilJobMgr.load(id);
	}
	
	
	private FacesContext getFacesContext(){
		return FacesContext.getCurrentInstance();
	}
	
	
}

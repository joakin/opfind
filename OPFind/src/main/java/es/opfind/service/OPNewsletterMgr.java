package es.opfind.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import es.opfind.dao.Dao;
import es.opfind.domain.OPNewsletter;

@Service
public class OPNewsletterMgr implements Serializable {

	@Resource
	private Dao dao;

	public OPNewsletter newOpNewsletter(String email,Boolean search,String searchText){
		OPNewsletter opNewsletter = new OPNewsletter();
		opNewsletter.setEmail(email);
		opNewsletter.setSearch(search);
		opNewsletter.setSearchText(searchText);
		opNewsletter.setLastSendDate(new Date());
		
		return opNewsletter;
	}
	
	public OPNewsletter getOPNewsletterByEmail(String email){
		DetachedCriteria criteria = DetachedCriteria.forClass(OPNewsletter.class);
		 criteria.add(Restrictions.eq("email", email));
		 final List<OPNewsletter> list = dao.findByCriteria(criteria);
		 if (!list.isEmpty())
			 return list.get(0);
		 return null;
	}
	
	public void persist(OPNewsletter opNewsletter){
		dao.persist(opNewsletter);
	}
	
}

package es.opfind.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.eaio.uuid.UUID;

import es.opfind.dao.Dao;
import es.opfind.domain.Newsletter;
import es.opfind.domain.OPNewsletter;

@Service
public class OPNewsletterMgr implements Serializable {

	@Resource
	private Dao dao;

	public OPNewsletter newOpNewsletter(String email,String searchText){
		OPNewsletter opNewsletter = new OPNewsletter();
		opNewsletter.setEmail(email);
		opNewsletter.setSearchText(searchText);
		opNewsletter.setLastSendDate(new Date());
		opNewsletter.setUuid(new UUID().toString());
		
		return opNewsletter;
	}
	
	public List<OPNewsletter> getOPNewslettersByEmail(String email){
		DetachedCriteria criteria = DetachedCriteria.forClass(OPNewsletter.class);
		 criteria.add(Restrictions.eq("email", email));
		 final List<OPNewsletter> list = dao.findByCriteria(criteria);
		 return list;
	}
	
	public List<OPNewsletter> getOPNewslettersByEmailAndSearchText(String email,String searchText){
		DetachedCriteria criteria = DetachedCriteria.forClass(OPNewsletter.class);
		 criteria.add(Restrictions.eq("email", email));
		 criteria.add(Restrictions.eq("searchText", searchText));
		 final List<OPNewsletter> list = dao.findByCriteria(criteria);
		 return list;
	}
	
	public OPNewsletter getOPNewsletterByUuid(String uuid){
		DetachedCriteria criteria = DetachedCriteria.forClass(OPNewsletter.class);
		 criteria.add(Restrictions.eq("uuid", uuid));
		 final List<OPNewsletter> list = dao.findByCriteria(criteria);
		 if(!list.isEmpty())
			 return list.get(0);
		 return null;
	}
	
	public void persist(OPNewsletter opNewsletter){
		dao.persist(opNewsletter);
	}
	
	public void delete(OPNewsletter opNewsletter){
		dao.delete(opNewsletter);
	}

	public List<OPNewsletter> getOPNewsletters() {
		final List<OPNewsletter> list = dao.find(OPNewsletter.class);
		return list;
	}

	
}

package es.opfind.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import es.opfind.dao.Dao;
import es.opfind.domain.Newsletter;

@Service
public class NewsletterMgr implements Serializable {

	@Resource
	private Dao dao;

	public Newsletter newNewsletter(String num, Date bolDate, String url) {
		Newsletter newsletter = new Newsletter();
		newsletter.setNum(num);
		newsletter.setBolDate(bolDate);
		newsletter.setUrl(url);
		return newsletter;
	}

	public void persist(Newsletter newsletter) {
		if (newsletter.getCreated() == null)
			newsletter.setCreated(new Date());
		newsletter.setModified(new Date());
		dao.persist(newsletter);
	}

	public void persist(Newsletter newsletter[]) {
		dao.persist(newsletter);
	}

	public List<Newsletter> getNewsletters() {
		final List<Newsletter> list = dao.find(Newsletter.class);
		return list;
	}

	public List<Newsletter> getNewslettersOrderByBolDate() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Newsletter.class);
		criteria.addOrder(Order.desc("bolDate"));
		final List<Newsletter> list = dao.findByCriteria(criteria);
		return list;
	}
	
	public List<Newsletter> getNewslettersByNum(String num){
		DetachedCriteria criteria = DetachedCriteria.forClass(Newsletter.class);
		 criteria.add(Restrictions.eq("num", num));
		 final List<Newsletter> list = dao.findByCriteria(criteria);
		 return list;
	}

	
}

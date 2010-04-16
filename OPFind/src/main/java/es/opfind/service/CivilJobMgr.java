package es.opfind.service;

import java.util.Date;  
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import es.opfind.dao.Dao;
import es.opfind.domain.CivilJob;

@Service
public class CivilJobMgr {

	@Resource
	private Dao dao;

	public CivilJob newCivilJob(String description, String url, String fullHtml, String category,String num) {
		CivilJob job = new CivilJob();
		job.setDescription(description);
		job.setUrl(url);
		job.setFullHtml(fullHtml);
		job.setCategory(category);
		job.setNum(num);
		return job;
	}

	public void persist(CivilJob civilJob) {
		//TODO: En los tres Mgrs esta esto, hibernate no funciona con los campos automaticos, quitarlo
		if (civilJob.getCreated() == null)
			civilJob.setCreated(new Date());
		civilJob.setModified(new Date());
		dao.persist(civilJob);
	}

	public CivilJob load(Integer id){
		return dao.load(CivilJob.class, id);
	}
	public void persist(CivilJob civilJob[]) {
		dao.persist(civilJob);
	}

	public List<CivilJob> getCivilJobs() {
		final List<CivilJob> list = dao.find(CivilJob.class);
		return list;
	}

	public List<CivilJob> findByFullText(String textToFind) {
		final List<CivilJob> list = dao.findByFullText(CivilJob.class, CivilJob.FULL_TEXT_FIELDS,textToFind);
		return list;
	}
	
	public List<CivilJob> findByFullTextAndSortDate(String textToFind) {
		Sort sort = new Sort( new SortField( "bolDate", SortField.AUTO, true ) );
		final List<CivilJob> list = dao.findByFullTextAndSort(CivilJob.class, CivilJob.FULL_TEXT_FIELDS,textToFind,sort);
		return list;
	}
	
	public List<CivilJob> getCivilJobOrderByDate(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CivilJob.class);
		criteria.addOrder(Order.desc("newsletter.bolDate"));
		final List<CivilJob> list = dao.findByCriteria(criteria);
		return list;
	}
	
	public CivilJob getCivilJobByNum(String num){
		DetachedCriteria criteria = DetachedCriteria.forClass(CivilJob.class);
		 criteria.add(Restrictions.eq("num", num));
		 final List<CivilJob> list = dao.findByCriteria(criteria);
		 if ( !list.isEmpty() )
			 return list.get(0);
		 else
			 return null; 
	}
}

package es.opfind.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import es.opfind.dao.Dao;
import es.opfind.domain.Institution;
import es.opfind.domain.Newsletter;

@Service
public class InstitutionMgr implements Serializable {

	@Resource
	private Dao dao;

	public Institution newInstitution() {
		return new Institution();
	}

	public Institution newInstitution(String name) {
		Institution institution = new Institution();
		institution.setName(name);
		return institution;
	}

	public void persist(Institution institution) {
		if (institution.getCreated() == null)
			institution.setCreated(new Date());
		institution.setModified(new Date());
		dao.persist(institution);
	}

	public void persist(Institution institution[]) {
		dao.persist(institution);
	}

	public List<Institution> getInstitutions() {
		final List<Institution> list = dao.find(Institution.class);
		return list;
	}

	public Institution load(Integer id) {
		return dao.load(Institution.class, id);
	}

	public Institution get(Integer id) {
		return dao.get(Institution.class, id);
	}

	public List<Institution> getInstitutionByName(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Institution.class);
		criteria.add(Restrictions.eq("name", name));
		final List<Institution> list = dao.findByCriteria(criteria);
		return list;
	}

}

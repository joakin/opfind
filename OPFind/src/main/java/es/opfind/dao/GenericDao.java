package es.opfind.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Sort;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GenericDao extends HibernateDaoSupport implements Dao {

	private static final Log log = LogFactory.getLog(GenericDao.class);

	@Autowired
	public GenericDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> findByFullText(Class<T> entityClass, String[] entityFields, String textToFind) {
		final FullTextSession fullTextSession = Search.createFullTextSession(getSession());
		final MultiFieldQueryParser parser = new MultiFieldQueryParser(entityFields, new StandardAnalyzer());
		final org.apache.lucene.search.Query luceneQuery;
		try {
			luceneQuery = parser.parse(textToFind);
		} catch (ParseException e) {
			log.error("Cannot parse [" + textToFind + "] to a full text query", e);
			return new ArrayList<T>(0);
		}
		final Query query = fullTextSession.createFullTextQuery(luceneQuery, entityClass);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> findByFullTextAndSort(Class<T> entityClass, String[] entityFields, String textToFind,Sort sort) {
		final FullTextSession fullTextSession = Search.createFullTextSession(getSession());
		final MultiFieldQueryParser parser = new MultiFieldQueryParser(entityFields, new StandardAnalyzer());
		final org.apache.lucene.search.Query luceneQuery;
		try {
			luceneQuery = parser.parse(textToFind);
		} catch (ParseException e) {
			log.error("Cannot parse [" + textToFind + "] to a full text query", e);
			return new ArrayList<T>(0);
		}
		final Query query = fullTextSession.createFullTextQuery(luceneQuery, entityClass).setSort(sort);
		return query.list();
	}


	@Transactional
	public void persist(Object entity) {
		  getHibernateTemplate().saveOrUpdate(entity);
	}

	@Transactional
	public void persist(Object[] entities) {
		for (Object entity : entities) {
			persist(entity);
		}
	}

	@Transactional(readOnly = true)
	public <T> List<T> find(Class<T> entityClass) {
		final List<T> entities = getHibernateTemplate().loadAll(entityClass);
		return entities;
	}

	@Transactional(readOnly = true)
	public <T> T load(Class<T> entityClass, Serializable id) {
		final T entity = (T) getHibernateTemplate().load(entityClass, id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public <T> T get(Class<T> entityClass, Serializable id) {
		final T entity = (T) getHibernateTemplate().get(entityClass, id);
		return entity;
	}

	@Transactional(readOnly = true)
	public <T> List<T> find(String hql) {
		final List<T> entities = getHibernateTemplate().find(hql);
		return entities;
	}

	@Transactional(readOnly = true)
	public <T> List<T> findByCriteria(DetachedCriteria criteria) {
		final List<T> entities = getHibernateTemplate().findByCriteria(criteria);
		return entities;
	}
	
	
}

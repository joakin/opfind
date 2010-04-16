package es.opfind.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.lucene.search.Sort;
import org.hibernate.criterion.DetachedCriteria;

public interface Dao {

	public <T> List<T> findByFullText(Class<T> entityClass, String[] entityFields, String textToFind);
	
	public <T> List<T> findByFullTextAndSort(Class<T> entityClass, String[] entityFields, String textToFind,Sort sort);

	public void persist(Object entity);

	public void persist(Object[] entities);

	public <T> List<T> find(Class<T> entityClass);

	public <T> T get(Class<T> entityClass, Serializable id);
	
	public <T> T load(Class<T> entityClass, Serializable id);

	public <T> List<T> find(String hql);
	
	public <T> List<T> findByCriteria(DetachedCriteria criteria);
}
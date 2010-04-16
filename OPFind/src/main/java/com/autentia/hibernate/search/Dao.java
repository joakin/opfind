package com.autentia.hibernate.search;

import java.util.List;

public interface Dao {

	public <T> List<T> findByFullText(Class<T> entityClass, String[] entityFields, String textToFind);

	public void persist(Object entity);

	public void persist(Object[] entities);

}
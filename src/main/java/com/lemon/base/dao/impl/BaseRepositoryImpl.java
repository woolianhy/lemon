package com.lemon.base.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lemon.base.dao.BaseRepository;

public class BaseRepositoryImpl<T, K extends Serializable> extends HibernateDaoSupport implements
		BaseRepository<T, K> {

	protected Log log = LogFactory.getLog(BaseRepositoryImpl.class);

	public void save(T t) {
		this.getHibernateTemplate().saveOrUpdate(t);
	}

	public T get(K id) {
		return this.getHibernateTemplate().get(resolveClass(), id);
	}

	public void remove(T t) {
		this.getHibernateTemplate().delete(t);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String hql, Object... params) {
		return (List<T>) this.getHibernateTemplate().find(hql, params);
	}

	@SuppressWarnings("unchecked")
	private Class<T> resolveClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}
}
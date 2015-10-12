package com.lemon.base.dao.impl;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class BaseDAOMySQL<T, K extends Serializable> extends BaseRepositoryImpl<T, K>{
	
	/**
	 * 注入sessionFactory
	 * @param sessionFactory
	 */
	@Autowired(required=false) @Qualifier("mySqlSessionFactory")
	public void initSessionFactory(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}

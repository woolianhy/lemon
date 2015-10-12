package com.lemon.base.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<T, K extends Serializable> {

	void save(T t);

	T get(K id);

	void remove(T t);

	List<T> find(String hql, Object... params);
}
package com.laptrinhjavaweb.repository;

import java.util.List;

public interface JpaRepository<T, K> {
	List<T> findAll();

	void deleteById(K k);

	void query(String sql, Object... objects);

	List<T> queryForList(String sql, Object... objects);

	void update(T t);

	void insert(T t);
	
	void save(Object o);
	
	T findById(K k);
}

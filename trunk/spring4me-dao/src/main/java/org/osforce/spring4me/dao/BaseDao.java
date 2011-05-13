package org.osforce.spring4me.dao;

import org.osforce.spring4me.entity.IdAdapter;

public interface BaseDao<T extends IdAdapter> {

	<PK> T get(PK id);

	void save(T entity);

	void update(T entity);

	void delete(T entity);

	<PK> void delete(PK id);

}

package org.osforce.spring4me.dao;

import org.osforce.spring4me.entity.IdEntity;

public interface BaseDao<T extends IdEntity> {

	<PK> T get(PK id);

	void save(T entity);

	void update(T entity);

	void delete(T entity);

	<PK> void delete(PK id);

}

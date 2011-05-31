package org.osforce.spring4me.dao;

import org.osforce.spring4me.entity.IdEntity;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:51:25 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface BaseDao<T extends IdEntity> {

	<PK> T get(PK id);

	void save(T entity);

	void update(T entity);

	void delete(T entity);

	<PK> void delete(PK id);

}

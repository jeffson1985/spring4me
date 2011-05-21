package org.osforce.spring4me.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.entity.IdEntity;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:50:49 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public abstract class AbstractDao<E extends IdEntity>
	implements BaseDao<E> {

	protected EntityManager entityManager;
	protected Class<E> entityClass;

	public AbstractDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public <PK> E get(PK id) {
		return (E) entityManager.getReference(entityClass, id);
	}

	public void save(E entity) {
		entityManager.persist(entity);
	}

	public void update(E entity) {
		entityManager.merge(entity);
	}

	public void delete(E entity) {
		entityManager.remove(entity);
	}

	public <PK> void delete(PK id) {
		E entity = get(id);
		entityManager.remove(entity);
	}

	protected Long count(String qlString, Object... values) {
		TypedQuery<Long> countQuery = entityManager.createQuery(qlString, Long.class);
		setParametersToQuery(countQuery, values);
		return countQuery.getSingleResult();
	}

	protected Long count(String qlString, Map<String, Object> paramValues) {
		TypedQuery<Long> countQuery = entityManager.createQuery(qlString, Long.class);
		setParametersToQuery(countQuery, paramValues);
		return countQuery.getSingleResult();
	}
	
	protected E findOne(String qlString, Object... values) {
		TypedQuery<E> query = entityManager.createQuery(qlString, entityClass);
		setParametersToQuery(query, values);
		try {
		 return query.getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}
	
	protected E findOne(String qlString, Map<String, Object> paramValues) {
		TypedQuery<E> query = entityManager.createQuery(qlString, entityClass);
		setParametersToQuery(query, paramValues);
		return query.getSingleResult();
	}
	
	protected List<E> findList(String qlString, Object... values) {
		TypedQuery<E> query = entityManager.createQuery(qlString, entityClass);
		setParametersToQuery(query, values);
		return query.getResultList();
	}
	
	protected List<E> findList(String qlString, Map<String, Object> paramValues) {
		TypedQuery<E> query = entityManager.createQuery(qlString, entityClass);
		setParametersToQuery(query, paramValues);
		return query.getResultList();
	}
	
	protected <X> List<X> findList(String qlString, Class<X> retType, Object... values) {
		TypedQuery<X> query = entityManager.createQuery(qlString, retType);
		setParametersToQuery(query, values);
		return query.getResultList();
	}
	
	protected <X> List<X> findList(String qlString, Class<X> retType, Map<String, Object> paramValues) {
		TypedQuery<X> query = entityManager.createQuery(qlString, retType);
		setParametersToQuery(query, paramValues);
		return query.getResultList();
	}

	protected Page<E> findPage(Page<E> page, String qlString, Object... values) {
		TypedQuery<E> query = entityManager.createQuery(qlString, entityClass);
		setParametersToQuery(query, values);
		return findPage(page, query);
	}

	protected Page<E> findPage(Page<E> page, String qlString, Map<String, Object> paramValues) {
		TypedQuery<E> query = entityManager.createQuery(qlString, entityClass);
		setParametersToQuery(query, paramValues);
		return findPage(page, query);
	}

	protected Page<E> findPage(Page<E> page, CriteriaQuery<E> criteriaQuery) {
		TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
		return findPage(page, query);
	}

	protected Page<E> findPage(Page<E> page, TypedQuery<E> query) {
		org.hibernate.Query nativeQuery = query.unwrap(org.hibernate.Query.class);
		String countString = getCountQuery(nativeQuery.getQueryString());
		Map<String, Object> paramValues = getParameterValues(query);
		if(page.getAutoCount()) {
			Long totalCount = count(countString, paramValues);
			page.setTotalCount(totalCount);
		}
		query.setFirstResult(page.getFirst()-1);
		query.setMaxResults(page.getPageSize());
		return page.setResult(query.getResultList());
	}

	private Map<String, Object> getParameterValues(TypedQuery<?> query) {
		Map<String, Object> paramValues = new HashMap<String, Object>();
		for(Parameter<?> param : query.getParameters()) {
			paramValues.put(param.getName(), query.getParameterValue(param));
		}
		return paramValues;
	}

	private String getCountQuery(String queryStr) {
		String countQueryStr = queryStr;
		// clean order by or group by
		countQueryStr = "FROM " + StringUtils.substringAfter(countQueryStr, "FROM");
		countQueryStr = StringUtils.substringBefore(countQueryStr, "ORDER BY");
		countQueryStr = StringUtils.substringBefore(countQueryStr,"GROUP BY");
		countQueryStr = "SELECT COUNT(*) " + countQueryStr;
		return countQueryStr;
	}

	protected void setParametersToQuery(Query query, Object...values) {
		// set parameters to query
		for(int i=1; i<=values.length; i++) {
			query.setParameter(i, values[i-1]);
		}
	}

	protected void setParametersToQuery(Query query, Map<String, Object> paramValues) {
		Iterator<Entry<String, Object>> iter = paramValues.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

}

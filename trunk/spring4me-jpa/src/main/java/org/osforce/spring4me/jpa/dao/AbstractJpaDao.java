/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.jpa.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.osforce.spring4me.dao.BaseDao;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;


/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:50:49 PM
 */
public class AbstractJpaDao<E> implements BaseDao<E> {

	private EntityManager entityManager;
	private Class<E> entityClass;

	public AbstractJpaDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
	
	public Class<E> getEntityClass() {
		return entityClass;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public E load(Long id) {
		return (E) entityManager.getReference(entityClass, id);
	}

	public void persist(E model) {
		entityManager.persist(model);
	}

	public void merge(E model) {
		entityManager.merge(model);
	}

	public void purge(E model) {
		entityManager.remove(model);
	}
	
	public void purge(Long id) {
		E model = load(id);
		entityManager.remove(model);
	}

	public List<E> fetchList(E model) {
		CriteriaQuery<E> cq = getCriteriaBuilder().createQuery(entityClass);
		Root<E> root = cq.from(entityClass);
		//
		Map<String, Object> params = getBeanParameters(model);
		for(Iterator<String> keyIter=params.keySet().iterator(); keyIter.hasNext(); ) {
			String name = keyIter.next();
			Object value = params.get(name);
			//
			cq.where(getCriteriaBuilder().equal(root.get(name), value));
		}
		//
		return findList(cq, params);
	}
	
	protected CriteriaQuery<E> buildCriteriaQueryByModel(E model) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(entityClass);
		Root<E> root = cq.from(entityClass);
		//
		Map<String, Object> beanParameters = getBeanParameters(model);
		for(Iterator<String> keyIter=beanParameters.keySet().iterator(); keyIter.hasNext(); ) {
			String name = keyIter.next();
			Object value = beanParameters.get(name);
			//
			cq.where(cb.equal(root.get(name), value));
		}
		//
		return cq;
	}
	
	protected Map<String, Object> getBeanParameters(E model) {
		Map<String, Object> beanParameters = new HashMap<String, Object>();
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(model);
		//
		Metamodel metamodel = getEntityManager().getMetamodel();
		EntityType<E> entityType = metamodel.entity(getEntityClass());
		for(@SuppressWarnings("rawtypes") Attribute attribute : entityType.getAttributes()) {
			String name = attribute.getName();
			Object value = beanWrapper.getPropertyValue(name);
			//
			if(!"class".equals(name) && value!=null) {
				beanParameters.put(name, value);
			}
		}
		//
		return beanParameters;
	}

	public E fetchOne(E model) {
		CriteriaQuery<E> cq = buildCriteriaQueryByModel(model);
		//
		return findOne(cq);
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
		return findOne(query);
	}

	protected E findOne(CriteriaQuery<E> criteriaQuery) {
		TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
		return findOne(query);
	}
	
	private E findOne(TypedQuery<E> typedQuery) {
		try {
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	protected List<E> findList(String qlString, Object... values) {
		TypedQuery<E> query = entityManager.createQuery(qlString, entityClass);
		setParametersToQuery(query, values);
		return query.getResultList();
	}

	protected List<E> findList(CriteriaQuery<E> criteriaQuery, Map<String, Object> params) {
		
		//
		TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	protected <X> List<X> findList(String qlString, Class<X> retType, Object... values) {
		TypedQuery<X> query = entityManager.createQuery(qlString, retType);
		setParametersToQuery(query, values);
		return query.getResultList();
	}

	protected void setParametersToQuery(Query query, Object... values) {
		// set parameters to query
		try {
			for (int i = 1; i <= values.length; i++) {
				query.setParameter(i, values[i - 1]);
			}
		} catch (IllegalArgumentException e){
			for (int i = 0; i <values.length; i++) {
				query.setParameter("param"+i, values[i]);
			}
		}
	}

}

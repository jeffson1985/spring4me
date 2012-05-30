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

package org.osforce.spring4me.jpa.dao.pagination;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.osforce.spring4me.dao.pagination.Page;
import org.osforce.spring4me.dao.pagination.PageDao;
import org.osforce.spring4me.jpa.dao.AbstractJpaDao;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-24 - 上午10:36:59
 */
public class AbstractJpaPageDao<E> extends AbstractJpaDao<E> 
	implements PageDao<E> {

	public AbstractJpaPageDao(Class<E> entityClass) {
		super(entityClass);
	}

	public Page<E> fetchPage(Page<E> page) {
		CriteriaQuery<E> cq = getCriteriaBuilder().createQuery(getEntityClass());
		cq.from(getEntityClass());
		return findPage(page, cq);
	}

	public Page<E> fetchPage(Page<E> page, E model) {
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(getEntityClass());
		Root<E> r = cq.from(getEntityClass());
		//
		int index = 0;
		Map<String, Object> params = getBeanParameters(model);
		Object[] values = new Object[params.size()];
		Predicate condition = null;
		for(Iterator<String> keyIter=params.keySet().iterator(); keyIter.hasNext(); ) {
			String name = keyIter.next();
			Object value = params.get(name);
			values[index++] = value;
			//
			if(condition==null) {
				condition = cb.equal(r.get(name), value);
			} else {
				condition = cb.and(condition, cb.equal(r.get(name), value));
			}
		}
		//
		cq.where(condition);
		//
		return findPage(page, cq, values);
	}
	
	protected Page<E> findPage(Page<E> page, CriteriaQuery<E> criteriaQuery, Object... values) {
		TypedQuery<E> query = getEntityManager().createQuery(criteriaQuery);
		// TODO here depends hibernate api directly
		org.hibernate.Query nativeQuery = query.unwrap(org.hibernate.Query.class);
		String qlString = nativeQuery.getQueryString();
		return findPage(page, qlString, values);
	}
	
	@SuppressWarnings("unchecked")
	protected Page<E> findPage(Page<E> page, String qlString, Object... values) {
		String countString = getCountQuery(qlString);
		if (page.getAutoCount()) {
			Long totalCount = count(countString, values);
			page.setTotalCount(totalCount);
		}
		qlString = appendOrders(page, qlString);
		Query query = getEntityManager().createQuery(qlString);
		setParametersToQuery(query, values);
		query.setFirstResult(page.getFirst() - 1);
		query.setMaxResults(page.getPageSize());
		List<Object> resultList = query.getResultList();
		List<E> tmp = new ArrayList<E>();
		for (Object result : resultList) {
			if (result != null && result instanceof Object[]) {
				for (Object value : (Object[]) result) {
					if (value != null && value.getClass().isAssignableFrom(getEntityClass())) {
						tmp.add((E) value);
					}
				}
			} else {
				tmp.add((E) result);
			}
		}
		return page.setResult(tmp);
	}
	
	private static String getCountQuery(String queryString) {
		for(int i=0, len=queryString.length(); i<len; i++) {
			//
			if(i+5>=len) {
				break;
			}
			//
			Character fbc = queryString.charAt(i);
			Character fec = queryString.charAt(i+5);
			//
			if(Character.isWhitespace(fbc) && Character.isWhitespace(fec)) {
				String word = queryString.substring(i+1, i+5);
				if("from".equals(word.toLowerCase())) {
					return "select count(*) from" + queryString.substring(i+5, len);
				}
			}
		}
		//
		throw new RuntimeException(queryString);
	}
	
	private String appendOrders(Page<E> page, String qlString) {
		if (!page.getOrderList().isEmpty()) {
			StringBuffer buffer = new StringBuffer(qlString);
			if (!qlString.contains("order by")) {
				buffer.append(" order by");
			} else {
				buffer.append(",");
			}
			List<String> orderList = page.getOrderList();
			for (int i = 0; i < orderList.size(); i++) {
				buffer.append(" " + orderList.get(i));
				if (i + 1 < orderList.size()) {
					buffer.append(",");
				}
			}
			return buffer.toString();
		}
		return qlString;
	}

}

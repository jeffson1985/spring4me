package org.osforce.spring4me.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.osforce.spring4me.commons.collection.CollectionUtil;

public class AbstractSearchDao<E> implements BaseSearchDao {

	private Class<E> indexedClass;
	private EntityManager entityManager;

	protected AbstractSearchDao(Class<E> indexedClass) {
		this.indexedClass = indexedClass;
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	protected FullTextEntityManager getFullTextEntityManager() {
		return Search.getFullTextEntityManager(entityManager);
	}
	
	protected SearchFactory getSearchFactory() {
		return getFullTextEntityManager().getSearchFactory();
	}

	protected QueryBuilder getQueryBuilder() {
		return getFullTextEntityManager().getSearchFactory().buildQueryBuilder()
					.forEntity(indexedClass).get();
	}

	@SuppressWarnings("unchecked")
	protected Page<E> searchPage(Page<E> page, Query query) {
		FullTextQuery fullTextQuery = getFullTextEntityManager()
				.createFullTextQuery(query, indexedClass);
		appendOrders(page, fullTextQuery);
		if (page.getAutoCount()) {
			page.setTotalCount((long) fullTextQuery.getResultSize());
			page.setAutoCount(false);
		}
		//
		fullTextQuery.setFirstResult(page.getFirst() - 1);
		fullTextQuery.setMaxResults(page.getPageSize());
		return page.setResult(fullTextQuery.getResultList());
	}
	
	protected Integer count(Query query) {
		FullTextQuery fullTextQuery = getFullTextEntityManager().createFullTextQuery(query, indexedClass);
		return fullTextQuery.getResultSize();
	}
	
	private void appendOrders(Page<E> page, FullTextQuery fullTextQuery) {
		List<SortField> sortFields = CollectionUtil.newArrayList();
		if(!page.getAscOrderList().isEmpty()) {
			for(String field : page.getAscOrderList()) {
				sortFields.add(new SortField(field, SortField.STRING, false));
			}
		}
		//
		if(!page.getDescOrderList().isEmpty()) {
			for(String field : page.getDescOrderList()) {
				sortFields.add(new SortField(field, SortField.STRING, true));
			}
		}
		//
		if(!sortFields.isEmpty()) {
			fullTextQuery.setSort(new Sort(sortFields.toArray(new SortField[sortFields.size()])));
		}
	}
	
}

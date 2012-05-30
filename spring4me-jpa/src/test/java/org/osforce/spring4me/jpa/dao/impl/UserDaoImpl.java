package org.osforce.spring4me.jpa.dao.impl;

import javax.persistence.TypedQuery;

import org.osforce.spring4me.jpa.dao.UserDao;
import org.osforce.spring4me.jpa.dao.pagination.AbstractJpaPageDao;
import org.osforce.spring4me.jpa.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends AbstractJpaPageDao<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

	public User fetchByUsername(String username) {
		TypedQuery<User> tq = getEntityManager().createNamedQuery("fetchByUsername", getEntityClass());
		//
		return tq.getSingleResult();
	}
	
}

package org.osforce.spring4me.jdbc.dao;

import org.osforce.spring4me.jdbc.dao.pagination.AbstractPageDao;
import org.osforce.spring4me.jdbc.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends AbstractPageDao<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

}

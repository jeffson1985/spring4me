package org.osforce.spring4me.jpa.dao;

import org.osforce.spring4me.dao.pagination.PageDao;
import org.osforce.spring4me.jpa.entity.User;

public interface UserDao extends PageDao<User> {

	User fetchByUsername(String username);
	
}

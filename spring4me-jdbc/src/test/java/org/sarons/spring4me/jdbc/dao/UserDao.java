package org.sarons.spring4me.jdbc.dao;

import java.util.List;

import org.sarons.spring4me.jdbc.annotation.NamedSQL;
import org.sarons.spring4me.jdbc.annotation.SQLParam;
import org.sarons.spring4me.jdbc.model.User;
import org.sarons.spring4me.jdbc.pagination.Page;



public interface UserDao {
	
	@NamedSQL("findByUsernameAndPassword")
	User findByUsernameAndPassword(@SQLParam("username") String username,
			@SQLParam("password") String password);

	@NamedSQL("findByAge")
	List<User> findByAge(@SQLParam("beginAge") int beginAge, 
			@SQLParam("endAge") int endAge);
	
	@NamedSQL("findPage")
	Page<User> findPage(Page<User> page);
	
}

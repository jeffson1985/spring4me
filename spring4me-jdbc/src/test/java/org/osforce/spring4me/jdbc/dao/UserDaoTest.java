package org.osforce.spring4me.jdbc.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.dao.pagination.Page;
import org.osforce.spring4me.jdbc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-jdbc.xml")
public class UserDaoTest {

	@Autowired
	private UserDao userDao;
	
	@Test
	public void testPersist() {
		User user = new User();
		user.setUsername("Gavin Hu");
		user.setPassword("123456");
		userDao.persist(user);
		//
		Assert.assertNotNull(user.getId());
	}
	
	@Test
	public void testLoad() {
		User user = userDao.load(0L);
		Assert.assertEquals("Gavin Hu", user.getUsername());
	}
	
	@Test
	public void testMerge() {
		User user = userDao.load(0L);
		user.setUsername("Aimy Wang");
		user.setPassword("654321");
		userDao.merge(user);
		//
		user = userDao.load(0L);
		Assert.assertEquals("Aimy Wang", user.getUsername());
		Assert.assertEquals("654321", user.getPassword());
	}
	
	@Test
	public void testPurge() {
		User user = userDao.load(0L);
		userDao.purge(user);
	}
	
	@Test
	public void testFind() {
		User user = new User();
		user.setUsername("Gavin Hu");
		user.setPassword("123456");
		userDao.persist(user);
		//
		user.setUsername("HaozhongHu");
		userDao.persist(user);
		//
		User condition = new User();
		condition.setUsername("HaozhongHu");
		condition.setPassword("123456");
		List<User> users = userDao.fetchList(condition);
		Assert.assertEquals(1, users.size());
	}
	
	@Test 
	public void testFindPage() {
		User user = new User();
		user.setUsername("Gavin Hu");
		user.setPassword("123456");
		userDao.persist(user);
		//
		user.setUsername("HaozhongHu");
		userDao.persist(user);
		//
		Page<User> page = new Page<User>(1);
		page = userDao.fetchPage(page);
		Assert.assertEquals(1, page.getResult().size());
		//
		page.setPageNo(2);
		page = userDao.fetchPage(page);
		Assert.assertEquals(1, page.getResult().size());
	}
	
}

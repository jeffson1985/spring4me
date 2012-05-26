package org.osforce.spring4me.jpa.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.dao.pagination.Page;
import org.osforce.spring4me.jpa.dao.UserDao;
import org.osforce.spring4me.jpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-jpa.xml")
@TransactionConfiguration(transactionManager="transactionManager")
@Transactional
public class UserDaoTest {

	@Autowired
	private UserDao userDao;
	
	@Test 
	public void testPersist() {
		Assert.assertNotNull(userDao);
		//
		User user = new User("gavin", "123456", "haozhonghu@hotmail.com");
		userDao.persist(user);
		//
		Assert.assertNotNull(user.getId());
	}
	
	@Test
	public void testMerge() {
		User user = new User("aimy", "123123", "haozhonghu@hotmail.com");
		userDao.persist(user);
		//
		user.setPassword("123456");
		userDao.merge(user);
		//
		user = userDao.load(user.getId());
		Assert.assertEquals("123456", user.getPassword());
	}
	
	@Test
	public void testFetchList() {
		User gavin = new User("gavin", "123456", "haozhonghu@hotmail.com");
		User aimy = new User("aimy", "123123", "haozhonghu@hotmail.com");
		userDao.persist(gavin);
		userDao.persist(aimy);
		//
		User template = new User();
		template.setUsername("gavin");
		//
		List<User> users = userDao.fetchList(template);
		Assert.assertEquals(1, users.size());
	}
	
	@Test
	public void testFetchPage() {
		User gavin = new User("gavin", "123456", "haozhonghu@hotmail.com");
		User aimy = new User("aimy", "123123", "haozhonghu@hotmail.com");
		userDao.persist(gavin);
		userDao.persist(aimy);
		//
		Page<User> page = new Page<User>(10);
		userDao.fetchPage(page);
		Assert.assertEquals(2, page.getResults().size());
		//
		User template = new User();
		template.setPassword("123456");
		template.setUsername("gavin");
		userDao.fetchPage(page, template);
		Assert.assertEquals(1, page.getResults().size());
	}
	
}

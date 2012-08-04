package org.sarons.spring4me.jdbc.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sarons.spring4me.jdbc.model.User;
import org.sarons.spring4me.jdbc.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-dao.xml")
public class UserDaoTest {
	
	private StopWatch watch = new StopWatch();
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testFindByUsernameAndPassword() {
		//
		watch.start();
		User user = userDao.findByUsernameAndPassword("gavin", "123456");
		System.out.println(user.getUsername());
		watch.stop();
		System.out.println(watch.shortSummary());
	}
	
	@Test
	public void testFindPage() {
		Page<User> page = new Page<User>(1);
		page = userDao.findPage(page);
		Assert.assertTrue(page.getResult().size()==1);
	}

}

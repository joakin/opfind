package es.opfind.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.cfg.CreateKeySecondPass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.opfind.domain.Newsletter;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class NewsletterMgrTest {

	@Resource
	private NewsletterMgr newsletterMgr;
	
	@Before
	public void setUp() throws Exception {
	
	}
	
	@BeforeClass
	public static void setUpBeforeClass () throws Exception {
		
	}

	/*
	@Test
	public void testPersistNewsletter() {
		createSomeNewsletter();
		assertTrue(newsletterMgr.getNewleNewsletters().size() < 0);
	}
	*/
	
	@Test
	public void testGetNewslettersOrderByBolDate(){
		createSomeNewsletter();
		List<Newsletter> newsletters =  newsletterMgr.getNewslettersOrderByBolDate();
		assertTrue(newsletters.get(0).getUrl().equals("http://www.bing.com"));
	}
	
	@Test
	public void testGetNewslettersByNum(){
		assertTrue(newsletterMgr.getNewslettersByNum("BOE-A-432-2224").get(0) != null);
		assertTrue(newsletterMgr.getNewslettersByNum("BOE-A-432-9999").equals(""));
	}
	
	public void createSomeNewsletter(){
		final Calendar calendar = Calendar.getInstance();
		final Newsletter[] newsletters = new Newsletter[3];
		
		calendar.set(2008, Calendar.FEBRUARY, 15);
		newsletters[0] = newsletterMgr.newNewsletter("BOE-A-432-2224",calendar.getTime(),"http://www.google.com");
		
		calendar.set(2005, Calendar.FEBRUARY, 4);
		
		newsletters[1] = newsletterMgr.newNewsletter("BOE-A-752-2554",calendar.getTime(),"http://www.yahoo.com");
		
		calendar.set(2010, Calendar.FEBRUARY, 28);
		
		newsletters[2] = newsletterMgr.newNewsletter("BOE-A-987-1111",calendar.getTime(),"http://www.bing.com");
		
		newsletterMgr.persist(newsletters);
	}
	
	

}

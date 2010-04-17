package es.opfind.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.opfind.domain.Institution;
import es.opfind.domain.Newsletter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class InstitutionMgrTest {

	@Resource
	private InstitutionMgr institutionMgr;

	@Resource
	private NewsletterMgr newsletterMgr;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testPersistInstitution() {
		Institution institution = institutionMgr.newInstitution("nombre");
		institutionMgr.persist(institution);
		List<Institution> institutions = institutionMgr.getInstitutions();
		assertEquals(1,institutions.size());
		
		Institution firstInstitution = institutions.get(0); 
		assertTrue(firstInstitution.getName().equals("nombre"));
		
	}
	
}

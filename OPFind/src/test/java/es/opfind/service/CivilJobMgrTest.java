package es.opfind.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.opfind.domain.CivilJob;
import es.opfind.domain.Institution;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class CivilJobMgrTest {

	@Resource
	private CivilJobMgr civilJobMgr;

	@Before
	public void setUp() throws Exception {
	}

	/*
	@Test
	public void testPersistCivilJob() {
		CivilJob civilJob = civilJobMgr.newCivilJob("description", "http://www.google.es", "Sociis tristique nec vut, porttitor proin, porta ridiculus? Ut aliquam elit aenean augue augue, sagittis, porta, lacus ac tempor? Mauris? Nascetur vel dis elit lorem ut ac adipiscing, nisi, et, nascetur et! Magna magna ultricies. Phasellus, elementum penatibus, tortor pid sed auctor, eros velit nunc integer aliquet elementum. Enim, turpis tortor urna, sit porta! Urna a porta magna tempor placerat rhoncus egestas a? Arcu sagittis enim! Ut a sit nascetur etiam hac sed. Ac tincidunt. Elementum odio in? Sed porta integer et. Sociis aliquam! Vel tortor augue cras dis porttitor non, odio pulvinar, sociis auctor, lacus aenean quis montes sed? Ultricies lundium dapibus hac, aenean phasellus urna pellentesque elementum sit. Porttitor, nunc? Nascetur integer magna in aenean a, ac tortor.", "Universidades","BOE-A-332-3242");
		civilJobMgr.persist(civilJob);
		
		assertEquals(1,civilJobMgr.getCivilJobs().size());
		assertTrue(civilJobMgr.getCivilJobs().get(0).getDescription().equals("description"));
	}*/
	
	@Test
	public void testFindFullTest(){
		List<CivilJob> civilJobs = civilJobMgr.findByFullText(" \"auxiliar administrativo\" AND HUESCA");
		civilJobs.size();
	}
	
}

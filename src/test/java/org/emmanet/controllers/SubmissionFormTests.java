package org.emmanet.controllers;

/*
 * #%L
 * InfraFrontier
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2015 EMBL-European Bioinformatics Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.emmanet.jobs.WebRequests;
import org.emmanet.model.BackgroundDAO;
import org.emmanet.model.BackgroundManager;
import org.emmanet.model.MutationsDAO;
import org.emmanet.model.MutationsManager;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.mvc.Controller;

// Spring TestContext Framework not compatible with JUnit 4.5 - needs 4.4

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations={ 
		"file:/Users/koscieln/Documents/workspace/EMMA interfaces/web/WEB-INF/applicationContext.xml" 
		,
		"file:/Users/koscieln/Documents/workspace/EMMA interfaces/web/WEB-INF/dispatcher-servlet.xml" 
})
public class SubmissionFormTests {

	//@Inject 
	private ApplicationContext applicationContext; 

	@Autowired
	private SubmissionFormController submissionFormController;
	
	@Autowired
	private SubmissionMutationsController submissionMutationsController;
	
	private WebRequests wr = new WebRequests();
	
	private BackgroundManager bm = new BackgroundManager();
	
	private StrainsManager smg = new StrainsManager();
	
	private MutationsManager mutationManager = new MutationsManager();
	

/*	@Test
	public void testNbPages() {

		assertEquals(submissionFormController.getPages().length,12);

	}*/

	/*	@Test
	public void testGetForm() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		//controller.setCommandClass(SearchCriteria.class);

		request.setMethod("GET");
		ModelAndView mav = null;
		try {
			mav = submissionFormController.handleRequest(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
		System.out.println(mav.getViewName());
		assertEquals("/publicSubmission/submissionForm", mav.getViewName());
	}*/

	@Test
	public void testPostPage1() throws Exception {
		
		// list curated backgrounds
		List backgrounds = bm.getCuratedBackgrounds();
		
		// list countries
		List countries = wr.isoCountries();
		int countriesSize =  countries.size();
		Random randomGenerator = new Random();
	    int randomInt = 0;
	    randomGenerator.nextInt(100);
		
		// Page numbering starts with 0
		System.err.println("GET STEP 0");
		HttpSession session = performWizardRequest(submissionFormController, null, null, 0, null); // "currentPage");
		
		// step 0
		System.err.println("POST STEP 0 => STEP 1");
		Properties params = new Properties();
		params.setProperty("_", "on");
		params.setProperty(AbstractWizardFormController.PARAM_PAGE, "0");
		params.setProperty(AbstractWizardFormController.PARAM_TARGET + "1", "value");
		performWizardRequest(submissionFormController, session, params, 0, null);
		
		// step 1
		System.err.println("POST STEP 1 => STEP 2");
		params.clear();
		params.setProperty("submitter_email", "koscieln@ebi.ac.uk");
		params.setProperty(AbstractWizardFormController.PARAM_PAGE, "1");
		params.setProperty(AbstractWizardFormController.PARAM_TARGET + "2", "value");
		performWizardRequest(submissionFormController, session, params, 1, null);
		
		// step 2
		System.err.println("POST STEP 2 => STEP 3");
		
		params.clear();
		
		randomInt = randomGenerator.nextInt(countriesSize);
		
		params.setProperty("submitter_email", "koscieln@ebi.ac.uk");
		params.setProperty("submitter_title", "Mr"); // Mrs, Ms, Prof, 
		params.setProperty("submitter_firstname", "Gautier");
		params.setProperty("submitter_lastname", "Koscielny");
		params.setProperty("submitter_tel", "+44 234234234");
		params.setProperty("submitter_fax", "+44 234234234");
		params.setProperty("submitter_inst", "Hinxton");
		params.setProperty("submitter_dept", "Mouse informatics");
		params.setProperty("submitter_addr_1", "The Wellcome Trust Genome Campus");
		params.setProperty("submitter_addr_2", "");
		params.setProperty("submitter_city", "Hinxton");
		params.setProperty("submitter_county", "Cambridgeshire");
		params.setProperty("submitter_postcode", "CB10 1SD");
		params.setProperty("submitter_country", (String) countries.get(randomInt));
		params.setProperty("per_id_per_sub", "0");
		params.setProperty("submitter_authority", "");
		
		params.setProperty(AbstractWizardFormController.PARAM_PAGE, "2");
		params.setProperty(AbstractWizardFormController.PARAM_TARGET + "3", "value");
		performWizardRequest(submissionFormController, session, params, 2, null);
		
		// step 3
		System.err.println("POST STEP 3 => STEP 4");
		
		params.clear();
		
		params.setProperty("producer_email", "koscieln@ebi.ac.uk");
		params.setProperty("producer_title", "Mr"); // Mrs, Ms, Prof, 
		params.setProperty("producer_firstname", "Gautier");
		params.setProperty("producer_lastname", "Koscielny");
		params.setProperty("producer_tel", "+44 234234234");
		params.setProperty("producer_fax", "+44 234234234");
		params.setProperty("producer_inst", "Hinxton");
		params.setProperty("producer_dept", "Mouse informatics");
		params.setProperty("producer_addr_1", "The Wellcome Trust Genome Campus");
		params.setProperty("producer_addr_2", "");
		params.setProperty("producer_city", "Hinxton");
		params.setProperty("producer_county", "Cambridgeshire");
		params.setProperty("producer_postcode", "CB10 1SD");
		params.setProperty("producer_country", (String) countries.get(randomInt));
		params.setProperty("per_id_per", "0");
		params.setProperty("producer_ilar", "Wtsi");
		
		params.setProperty(AbstractWizardFormController.PARAM_PAGE, "3");
		params.setProperty(AbstractWizardFormController.PARAM_TARGET + "4", "value");
		performWizardRequest(submissionFormController, session, params, 3, null);
		
		// step 4
		System.err.println("POST STEP 4 => STEP 5");
		
		params.clear();
		
		// get a country randomly
		
		params.setProperty("shipper_email", "koscieln@ebi.ac.uk");
		params.setProperty("shipper_title", "Mr"); // Mrs, Ms, Prof, 
		params.setProperty("shipper_firstname", "Gautier");
		params.setProperty("shipper_lastname", "Koscielny");
		params.setProperty("shipper_tel", "+44 234234234");
		params.setProperty("shipper_fax", "+44 234234234");
		params.setProperty("shipper_inst", "Hinxton");
		params.setProperty("shipper_dept", "Mouse informatics");
		params.setProperty("shipper_addr_1", "The Wellcome Trust Genome Campus");
		params.setProperty("shipper_addr_2", "");
		params.setProperty("shipper_city", "Hinxton");
		params.setProperty("shipper_county", "Cambridgeshire");
		params.setProperty("shipper_postcode", "CB10 1SD");
		params.setProperty("shipper_country", (String) countries.get(randomInt));
		params.setProperty("per_id_per_contact", "0");
		
		params.setProperty(AbstractWizardFormController.PARAM_PAGE, "4");
		params.setProperty(AbstractWizardFormController.PARAM_TARGET + "5", "value");
		performWizardRequest(submissionFormController, session, params, 4, null);
		
		// step 5 Genotype
		System.err.println("POST STEP 5 => STEP 6");
		
		// select randomly a genetic background
		StrainsDAO strain = smg.getStrainByID(6189);
		
		BackgroundDAO background = strain.getBackgroundDAO();
		
		String geneticDescription = (strain.getCharact_gen() != null) ? strain.getCharact_gen() : "TESTGENETICDESCRIPTION";
		Object[] currentBackground = (Object[]) backgrounds.get(randomGenerator.nextInt(backgrounds.size()));
		String backcrosses = (strain.getGeneration() != null) ? strain.getGeneration() : "20";
		String breedingHistory = (strain.getMaintenance() != null) ? strain.getMaintenance() : "TESTBREEDINGHISTORY";
		
		System.out.println("background " + background.getSymbol());
		
		// play with mutations
		int nbMutations = randomGenerator.nextInt(10);
		for (int i=0; i <= nbMutations; i++) {
			
			String[] mutationTypes = { "CH", "GT", "IN", "SP", "TG", "TM", "XX" };
			String[] mutationSubtypes = { "INS","INV","DEL","DUP","TRL","TRP"};
			
			params.clear();
			// "CH" Chromosomal Anomaly
			//"GT Gene-trap
			//"IN" Induced
            // "SP" Spontaneous
            //"TG" Transgenic
		    // "TM" Targeted
            //"XX" Undefined
			
			/*
			 * Choose randomly a mutation
			 */
			int mutationIndex = randomGenerator.nextInt(7);
			String mutationType = mutationTypes[mutationIndex];
			
			List<MutationsDAO> mutations = mutationManager.getMutationByMainType(mutationType);
			int index = randomGenerator.nextInt(mutations.size());
			MutationsDAO mutation = mutations.get(index);
			
			params.clear();
			params.setProperty("mutation_type", mutationType);
			
			
			switch (mutationIndex) {
			
			case 0: // CH
				params.setProperty("mutation_subtypeCH", mutation.getSub_type());
				break;
			}
			
		}
		
		
		
		params.clear();
		
		params.setProperty("strain_name", strain.getName());
		params.setProperty("genetic_descr", geneticDescription);
		params.setProperty("current_backg", ""+ currentBackground[0]);
		
		params.setProperty("backcrosses", backcrosses);
		params.setProperty("sibmatings", "TOBESTORED");
		params.setProperty("breeding_history", breedingHistory);
		
		params.setProperty(AbstractWizardFormController.PARAM_PAGE, "5");
		params.setProperty(AbstractWizardFormController.PARAM_TARGET + "6", "value");
		performWizardRequest(submissionFormController, session, params, 5, null);
	}


	private HttpSession performMutationRequest(
			Controller controller, 
			HttpSession session, 
			Properties params,
			int target,
			String pageAttr) throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest((params != null ? "POST" : "GET"), "/publicSubmission/submissionForm");
		
		if (params != null) {
			for (Iterator it = params.keySet().iterator(); it.hasNext();) {
				String param = (String) it.next();
				request.addParameter(param, params.getProperty(param));
			}
		}
		
		request.setSession(session);
		request.setAttribute("target", new Integer(target));
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mv = controller.handleRequest(request, response);
		
		if (target >= 0) {
			
			if (params != null) {
				assertTrue("Page " + target + " returned for view named " + mv.getViewName(), ("/publicSubmission/submissionForm" + (target+1)).equals(mv.getViewName()));
				
			} else {
				assertTrue("Page " + target + " returned for view named " + mv.getViewName(), ("/publicSubmission/submissionForm").equals(mv.getViewName()));
			}
			
			Map model = mv.getModel();
			for (Object key: model.keySet()) {
				System.err.println(key + "\t=>\t" + model.get(key));
			}
			if (pageAttr != null) {
				assertTrue("Page attribute set", (new Integer(target)).equals(mv.getModel().get(pageAttr)));
				assertTrue("Correct model size", mv.getModel().size() == 3);
			}
			else {
				assertTrue("Correct model size", mv.getModel().size() == 2);
			}
		}
		else if (target == -1) {
			assertTrue("Success target returned", "success".equals(mv.getViewName()));
			assertTrue("Correct model size", mv.getModel().size() == 1);
		}
		else if (target == -2) {
			assertTrue("Abort view returned", "abort".equals(mv.getViewName()));
			assertTrue("Correct model size", mv.getModel().size() == 1);
		}
		
		/**
		 * replace with your bean
		 */
		//TestBean tb = (TestBean) mv.getModel().get("tb");
		//assertTrue("Has model", tb != null);
		//assertTrue("Name is " + name, (tb.getName() == name || name.equals(tb.getName())));
		//assertTrue("Age is " + age, tb.getAge() == age);
		return request.getSession(false);
	}
	
	private HttpSession performWizardRequest(
			Controller wizard, 
			HttpSession session, 
			Properties params,
			int target,
			String pageAttr) throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest((params != null ? "POST" : "GET"), "/publicSubmission/submissionForm");
		
		if (params != null) {
			for (Iterator it = params.keySet().iterator(); it.hasNext();) {
				String param = (String) it.next();
				request.addParameter(param, params.getProperty(param));
			}
		}
		
		request.setSession(session);
		request.setAttribute("target", new Integer(target));
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mv = wizard.handleRequest(request, response);
		
		if (target >= 0) {
			
			if (params != null) {
				assertTrue("Page " + target + " returned for view named " + mv.getViewName(), ("/publicSubmission/submissionForm" + (target+1)).equals(mv.getViewName()));
				
			} else {
				assertTrue("Page " + target + " returned for view named " + mv.getViewName(), ("/publicSubmission/submissionForm").equals(mv.getViewName()));
			}
			
			Map model = mv.getModel();
			for (Object key: model.keySet()) {
				System.err.println(key + "\t=>\t" + model.get(key));
			}
			if (pageAttr != null) {
				assertTrue("Page attribute set", (new Integer(target)).equals(mv.getModel().get(pageAttr)));
				assertTrue("Correct model size", mv.getModel().size() == 3);
			}
			else {
				assertTrue("Correct model size", mv.getModel().size() == 2);
			}
		}
		else if (target == -1) {
			assertTrue("Success target returned", "success".equals(mv.getViewName()));
			assertTrue("Correct model size", mv.getModel().size() == 1);
		}
		else if (target == -2) {
			assertTrue("Abort view returned", "abort".equals(mv.getViewName()));
			assertTrue("Correct model size", mv.getModel().size() == 1);
		}
		
		/**
		 * replace with your bean
		 */
		//TestBean tb = (TestBean) mv.getModel().get("tb");
		//assertTrue("Has model", tb != null);
		//assertTrue("Name is " + name, (tb.getName() == name || name.equals(tb.getName())));
		//assertTrue("Age is " + age, tb.getAge() == age);
		return request.getSession(false);
	}
}

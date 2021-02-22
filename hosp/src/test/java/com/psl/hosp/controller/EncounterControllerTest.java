package com.psl.hosp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.psl.hosp.service.EncounterService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EncounterControllerTest {
	@Mock
	EncounterService encounterService;
	
	@InjectMocks
	EncounterController encounterController;
	
	
	@Test
	void testAddEncounter() {
		Map<String, Object> requestMap1 = new HashMap<String, Object>();
		requestMap1.put("patientId" , 1);
		requestMap1.put("dateOfEncounter" , "2021-02-04");
		requestMap1.put("timeOfEncounter" , "10:00:00");
		requestMap1.put("triggerIssue" , "headache");
		requestMap1.put("diagnosis" , "acidity");
		requestMap1.put("medicines" , "some medicine");
		requestMap1.put("billingAmount" , 500.00);

		Map<String, Object> returnMap1 = new HashMap<String, Object>();
		returnMap1.put("statusCode", HttpStatus.CREATED);
		
		
		//mock
		when(encounterService.addEncounter(requestMap1)).thenReturn(returnMap1);
		
		//test
		assertNotNull(requestMap1);
		assertEquals(HttpStatus.CREATED, encounterController.addEncounter(requestMap1).get("statusCode"));
		verify(encounterService).addEncounter(requestMap1);
		
	}
	
	@Test
	void testGetEncounterbyId() {
		Map<String, Object> returnMap1 = new HashMap<String, Object>();
		returnMap1.put("statusCode", HttpStatus.OK);
		
		when(encounterService.getEncounterbyId(1)).thenReturn(returnMap1);
		
		assertEquals(HttpStatus.OK, encounterController.getEncounterbyId(1).get("statusCode"));
		verify(encounterService).getEncounterbyId(1);

	}
	
	@Test
	void testGetEncounterHistory() {
		Map<String, Object> returnMap1 = new HashMap<String, Object>();
		returnMap1.put("statusCode", HttpStatus.OK);
		
		Map<String, Object> returnMap2 = new HashMap<String, Object>();
		returnMap2.put("statusCode", HttpStatus.NOT_FOUND);
		when(encounterService.getEncounterHistorybyPatient(1)).thenReturn(returnMap1);
		when(encounterService.getEncounterHistorybyPatient(2)).thenReturn(returnMap2);
		
		assertEquals(HttpStatus.OK, encounterController.getEncounterHistorybyPatient(1).get("statusCode"));
		verify(encounterService).getEncounterHistorybyPatient(1);
		
		assertEquals(HttpStatus.NOT_FOUND, encounterController.getEncounterHistorybyPatient(2).get("statusCode"));
		verify(encounterService).getEncounterHistorybyPatient(2);
		
	}
	
	@Test
	void testUpdateEncounter() {
		
		Map<String, Object> requestMap1 = new HashMap<String, Object>();
		requestMap1.put("patientId" , 1);
		requestMap1.put("triggerIssue" , "headache");
		requestMap1.put("diagnosis" , "acidity");
		requestMap1.put("medicines" , "some medicine");
		requestMap1.put("billingAmount" , 500.00);
		
		Map<String, Object> returnMap1 = new HashMap<String, Object>();
		returnMap1.put("statusCode", HttpStatus.OK);
		
		when(encounterService.updateEncounter(requestMap1)).thenReturn(returnMap1);
		
		Map<String, Object> requestMap2 = new HashMap<String, Object>();
		requestMap2.put("patientId" , 10);
		requestMap2.put("triggerIssue" , "headache");
		requestMap2.put("diagnosis" , "acidity");
		requestMap2.put("medicines" , "some medicine");
		requestMap2.put("billingAmount" , 500.00);
		
		Map<String, Object> returnMap2 = new HashMap<String, Object>();
		returnMap2.put("statusCode", HttpStatus.NOT_FOUND);
		
		when(encounterService.updateEncounter(requestMap2)).thenReturn(returnMap2);
		
		assertEquals(HttpStatus.OK, encounterController.updateEncounter(requestMap1).get("statusCode"));
		assertEquals(HttpStatus.NOT_FOUND, encounterController.updateEncounter(requestMap2).get("statusCode"));

		
	}
	
	@Test
	void testDeleteEncounter() {
		Map<String, Object> returnMap1 = new HashMap<String, Object>();
		returnMap1.put("statusCode", HttpStatus.OK);
		
		when(encounterService.deleteEncounter(1)).thenReturn(returnMap1);
		
		Map<String, Object> returnMap2 = new HashMap<String, Object>();
		returnMap2.put("statusCode", HttpStatus.NOT_FOUND);
		
		when(encounterService.deleteEncounter(1)).thenReturn(returnMap1);
		when(encounterService.deleteEncounter(2)).thenReturn(returnMap2);

		assertEquals(HttpStatus.OK, encounterController.deleteEncounter(1).get("statusCode"));
		assertEquals(HttpStatus.NOT_FOUND, encounterController.deleteEncounter(2).get("statusCode"));

		
	}
}

package com.psl.hosp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.psl.hosp.service.PatientService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PatientControllerTest {
	@Mock
	PatientService patientService;
	
	@InjectMocks
	PatientController patientController;
	
	@Test
	void testAddPatient()  {
		
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statusCode", HttpStatus.CREATED);
        
        Map<String, Object> requestMap1 = new HashMap<String, Object>();
        requestMap1.put("firstName", "firstName1");
        requestMap1.put("lastName" , "lastName1");
        requestMap1.put("bloodGroup" , "O+ve");
        requestMap1.put("contactNumber" , "+91 9191919191");
        requestMap1.put("mailId" , "firstname1_lastname1@gmail.com");
        requestMap1.put("maritalStatus" , "Single");
        requestMap1.put("occupation" , "IT professional");
        requestMap1.put("address" , "9/11 Near Ganesh Temple");
        requestMap1.put("city" ,  "Karveer");
        requestMap1.put("district" , "Kolhapur");
        requestMap1.put("state" , "Maharashtra");
        requestMap1.put("dateOfBirth" , "1990-02-03");
        requestMap1.put("pinCode" , 416110);
        //mock part
        when(patientService.addPatient(requestMap1)).thenReturn(map);
		
        //test part
        assertNotNull(requestMap1);
		assertEquals(HttpStatus.CREATED, patientController.addPatient(requestMap1).get("statusCode"));
		verify(patientService).addPatient(requestMap1);
		
	}
	
	

	@Test
	void testGetPatientById()  {
		
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("statusCode", HttpStatus.OK);
       
        //mock part
        when(patientService.getPatientById(1)).thenReturn(map1);
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("statusCode", HttpStatus.NOT_FOUND);
		
		//mock part
        when(patientService.getPatientById(2)).thenReturn(map2);
		
        //test part
		assertEquals(HttpStatus.OK, patientController.getPatientById(1).get("statusCode"));
		verify(patientService).getPatientById(1);
		assertEquals(HttpStatus.NOT_FOUND, patientController.getPatientById(2).get("statusCode"));
		verify(patientService).getPatientById(2);
		
	}
	
	@Test
	void testGetAllPatients() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("statusCode", HttpStatus.OK);
		
		//mock part
		when(patientService.getAllPatients()).thenReturn(returnMap);
		
		//test
		assertEquals(HttpStatus.OK, patientController.getAllPatients().get("statusCode"));
		verify(patientService).getAllPatients();
	}
	
	@Test
	void testDeletePatient() {
		Map<String, Object> returnMap1 = new HashMap<String, Object>();
		returnMap1.put("statusCode", HttpStatus.OK);
		Map<String, Object> returnMap2 = new HashMap<String, Object>();
		returnMap2.put("statusCode", HttpStatus.NOT_FOUND);
		//mock
		when(patientService.deletePatient(1)).thenReturn(returnMap1);
		when(patientService.deletePatient(2)).thenReturn(returnMap2);	
		//test
		assertEquals(HttpStatus.OK, patientController.deletePatient(1).get("statusCode"));
		assertEquals(HttpStatus.NOT_FOUND, patientController.deletePatient(2).get("statusCode"));
		verify(patientService).deletePatient(1);
		verify(patientService).deletePatient(2);
	}
	
	@Test 
	void testUpdatePatient(){
		Map<String, Object> requestMap1 = new HashMap<String, Object>();
		requestMap1.put("patientId", 1);
        requestMap1.put("firstName", "firstName1");
        requestMap1.put("lastName" , "lastName1");
        requestMap1.put("bloodGroup" , "O+ve");
        requestMap1.put("contactNumber" , "+91 9191919191");
        requestMap1.put("mailId" , "firstname1_lastname1@gmail.com");
        requestMap1.put("maritalStatus" , "Single");
        requestMap1.put("occupation" , "IT professional");
        requestMap1.put("address" , "9/11 Near Ganesh Temple");
        requestMap1.put("city" ,  "Karveer");
        requestMap1.put("district" , "Kolhapur");
        requestMap1.put("state" , "Maharashtra");
        requestMap1.put("dateOfBirth" , "1990-02-03");
        requestMap1.put("pinCode" , 416110);
		Map<String, Object> returnMap1 = new HashMap<String, Object>();
		returnMap1.put("statusCode", HttpStatus.OK);
		
		Map<String, Object> requestMap2 = new HashMap<String, Object>();
		requestMap2.put("patientId", 2);
		requestMap2.put("firstName", "firstName1");
		requestMap2.put("lastName" , "lastName1");
		requestMap2.put("bloodGroup" , "O+ve");
		requestMap2.put("contactNumber" , "+91 9191919191");
		requestMap2.put("mailId" , "firstname2_lastname2@gmail.com");
		requestMap2.put("maritalStatus" , "Single");
		requestMap2.put("occupation" , "IT professional");
		requestMap2.put("address" , "9/11 Near Ganesh Temple");
		requestMap2.put("city" ,  "Karveer");
		requestMap2.put("district" , "Kolhapur");
		requestMap2.put("state" , "Maharashtra");
		requestMap2.put("dateOfBirth" , "1990-02-03");
		requestMap2.put("pinCode" , 416110);
		Map<String, Object> returnMap2 = new HashMap<String, Object>();
		returnMap2.put("statusCode", HttpStatus.NOT_FOUND);
		

		Map<String, Object> requestMap3 = new HashMap<String, Object>();
		requestMap3.put("patientId", 3);
		requestMap3.put("firstName", "firstName1");
		requestMap3.put("lastName" , "lastName1");
		requestMap3.put("bloodGroup" , "O+ve");
		requestMap3.put("contactNumber" , "+91 9191919191");
		requestMap3.put("mailId" , "firstname3_lastname11@gmail.com");
		requestMap3.put("maritalStatus" , "Single");
		requestMap3.put("occupation" , "IT professional");
		requestMap3.put("address" , "9/11 Near Ganesh Temple");
		requestMap3.put("city" ,  "Karveer");
		requestMap3.put("district" , "Kolhapur");
		requestMap3.put("state" , "Maharashtra");
		requestMap3.put("dateOfBirth" , "1990-02-03");
		requestMap3.put("pinCode" , 416110);
		Map<String, Object> returnMap3 = new HashMap<String, Object>();
		returnMap3.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
		
		//mock
		when(patientService.updatePatient(requestMap1)).thenReturn(returnMap1);
		when(patientService.updatePatient(requestMap2)).thenReturn(returnMap2);
		when(patientService.updatePatient(requestMap3)).thenReturn(returnMap3);
		
		
		//test
		assertEquals(HttpStatus.OK, patientController.updatePatient(requestMap1).get("statusCode"));
		verify(patientService).updatePatient(requestMap1);
		assertEquals(HttpStatus.NOT_FOUND, patientController.updatePatient(requestMap2).get("statusCode"));
		verify(patientService).updatePatient(requestMap2);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, patientController.updatePatient(requestMap3).get("statusCode"));
		verify(patientService).updatePatient(requestMap3);
	}
}

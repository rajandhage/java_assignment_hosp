package com.psl.hosp.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.psl.hosp.dao.PatientDao;
import com.psl.hosp.model.Patient;
import com.psl.hosp.service.PatientService;


@ExtendWith(MockitoExtension.class)
class PatientControllerTest {
	@Mock
	PatientService patientService;
	
	@InjectMocks
	PatientController patientController;
	
	
	
	@Test
	void testAdd()  {
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statusCode", HttpStatus.CREATED);
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("firstName", "firstName1");
        requestMap.put("lastName" , "lastName1");
        requestMap.put("bloodGroup" , "O+ve");
        requestMap.put("contactNumber" , "+91 9191919191");
        requestMap.put("mailId" , "firstname1_lastname1@gmail.com");
        requestMap.put("maritalStatus" , "Single");
        requestMap.put("occupation" , "IT professional");
        requestMap.put("address" , "9/11 Near Ganesh Temple");
        requestMap.put("city" ,  "Karveer");
        requestMap.put("district" , "Kolhapur");
        requestMap.put("state" , "Maharashtra");
        requestMap.put("dateOfBirth" , "1990-02-03");
        requestMap.put("pinCode" , 416110);
        //mock part
        when(patientService.addPatient(requestMap)).thenReturn(map);
		
        //test part
        assertNotNull(requestMap);
		assertEquals(map, patientController.addPatient(requestMap));
		verify(patientService).addPatient(requestMap);
		
	}
	
	
}

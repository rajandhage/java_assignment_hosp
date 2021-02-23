package com.psl.hosp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.psl.hosp.dao.PatientDao;
import com.psl.hosp.helper.PatientServiceHelper;
import com.psl.hosp.model.Patient;

import liquibase.exception.DatabaseException;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
	
	
	@Mock
	PatientServiceHelper patientServiceHelper;
	
	@Mock
	PatientDao patientDao;
	
	
	@InjectMocks
	PatientService patientService;
	
	@Test
	void testGetAllPatients() {
		Iterable<Patient> patients;
		Patient patient1 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname1_lastname1@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		Patient patient2 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname2_lastname2@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		Patient patient3 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname3_lastname3@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		List<Patient> patientsList = new ArrayList<Patient>();
		patientsList.add(patient1);
		patientsList.add(patient2);
		patientsList.add(patient3);
		patients = patientsList;
		//test of succesful scenario
		when(patientDao.findAll()).thenReturn(patients);
		Map<String, Object> returnValue = patientService.getAllPatients();
		@SuppressWarnings("unchecked")
		List<Patient> listOfPatients = (List<Patient>) returnValue.get("ListOfPatients");
		assertEquals(3, listOfPatients.size());
		verify(patientDao).findAll();
	}
	
	@Test
	void testAddPatient() {
		Patient returnPatient1 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname1_lastname1@gmail.com", "Single", "Software Engineer", "address1", "city1", "district1", "state1", 415415);
		returnPatient1.setPatientId(1);
		Optional<Patient> optionalPatient = Optional.empty();
        Map<String, Object> requestMap1 = new HashMap<String, Object>();
        requestMap1.put("firstName", "firstName1");
        requestMap1.put("lastName" , "lastName1");
        requestMap1.put("bloodGroup" , "b+ve");
        requestMap1.put("contactNumber" , "+91 8380997285");
        requestMap1.put("mailId" , "firstname1_lastname1@gmail.com");
        requestMap1.put("maritalStatus" , "Single");
        requestMap1.put("occupation" , "Software Engineer");
        requestMap1.put("address" , "address1");
        requestMap1.put("city" ,  "city1");
        requestMap1.put("district" , "district1");
        requestMap1.put("state" , "state1");
        requestMap1.put("dateOfBirth" , "2000-12-12");
        requestMap1.put("pinCode" , 415415);
        
        //when patient is added successfuly
        when(patientServiceHelper.checkValidityOfRequestForAdd(requestMap1)).thenReturn(true);
        when(patientDao.findByMailId(any(String.class))).thenReturn(optionalPatient);
		when(patientDao.save(any(Patient.class))).thenReturn(returnPatient1);
		assertEquals(HttpStatus.CREATED, patientService.addPatient(requestMap1).get("statusCode"));
		verify(patientDao).save(any(Patient.class));
		
        //when patient is added fails due to already presence of probably same patient data
		optionalPatient = Optional.of(returnPatient1);
		when(patientServiceHelper.checkValidityOfRequestForAdd(requestMap1)).thenReturn(true);
        when(patientDao.findByMailId(any(String.class))).thenReturn(optionalPatient);
		assertEquals(HttpStatus.NOT_FOUND, patientService.addPatient(requestMap1).get("statusCode"));
		verify(patientDao).save(any(Patient.class));
		
		//when patient added fails due to bad request
		optionalPatient = Optional.empty();
		when(patientServiceHelper.checkValidityOfRequestForAdd(requestMap1)).thenReturn(false);
        assertEquals(HttpStatus.BAD_REQUEST, patientService.addPatient(requestMap1).get("statusCode"));
		verify(patientDao).save(any(Patient.class));
		
		//when patient added fails due to database error
		when(patientServiceHelper.checkValidityOfRequestForAdd(requestMap1)).thenReturn(true);
        when(patientDao.findByMailId(any(String.class))).thenReturn(optionalPatient);
		when(patientDao.save(any(Patient.class))).thenThrow(JDBCConnectionException.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, patientService.addPatient(requestMap1).get("statusCode"));
		
	}
	
	@Test
	void testGetPatientById() {
		Patient patient1 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname1_lastname1@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		patient1.setPatientId(1);
		Optional<Patient> patient = Optional.of(patient1);
		when(patientDao.findById(1)).thenReturn(patient);
		assertEquals(HttpStatus.OK, patientService.getPatientById(1).get("statusCode"));
	}

}

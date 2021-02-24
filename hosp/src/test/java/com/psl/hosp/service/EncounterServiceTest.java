package com.psl.hosp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.psl.hosp.dao.EncounterDao;
import com.psl.hosp.dao.PatientDao;
import com.psl.hosp.helper.EncounterServiceHelper;
import com.psl.hosp.model.Encounter;
import com.psl.hosp.model.Patient;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EncounterServiceTest {
	@Mock
	EncounterDao encounterDao;
	
	@Mock
	EncounterServiceHelper encounterServiceHelper;
	
	@Mock
	PatientDao patientDao;
	
	@InjectMocks
	EncounterService encounterService;

	@Test
	void testGetEncounterHistorybyPatient() {
		Patient patient1 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname1_lastname1@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		patient1.setPatientId(1);
		Encounter encounter1 = new Encounter(LocalDate.parse("2021-01-01"), LocalTime.parse("10:00:00"), "triggerIssue1", "diagnosis1", "medicines1", 500.00, patient1);
		Encounter encounter2 = new Encounter(LocalDate.parse("2021-01-02"), LocalTime.parse("11:00:00"), "triggerIssue2", "diagnosis1", "medicines2", 500.00, patient1);
		
		List<Encounter> encounters = new ArrayList<Encounter>();
		encounters.add(encounter1);
		encounters.add(encounter2);
		//when there are encounters present. (Even if no encounter is present empty list is returned)
		when(encounterDao.findByPatientPatientId(any(Integer.class))).thenReturn(encounters);
		assertEquals(2, encounterService.getEncounterHistorybyPatient(1).size());
		verify(encounterDao).findByPatientPatientId(1);
		
	}
	
	@Test
	void testGetEncounterbyId() {
		Patient patient1 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname1_lastname1@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		patient1.setPatientId(1);
		Encounter encounter1 = new Encounter(LocalDate.parse("2021-01-01"), LocalTime.parse("10:00:00"), "triggerIssue1", "diagnosis1", "medicines1", 500.00, patient1);
		//when encounter for given id is present
		Optional<Encounter> encounterOptional = Optional.of(encounter1);
		when(encounterDao.findById(any(Integer.class))).thenReturn(encounterOptional);
		assertEquals(HttpStatus.OK, encounterService.getEncounterbyId(1).get("statusCode"));
		
		
		//when encounter for given id is not present
		encounterOptional = Optional.empty();
		when(encounterDao.findById(any(Integer.class))).thenReturn(encounterOptional);
		assertEquals(HttpStatus.NOT_FOUND, encounterService.getEncounterbyId(2).get("statusCode"));
		
		//when database error occures
		when(encounterDao.findById(any(Integer.class))).thenThrow(JDBCConnectionException.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, encounterService.getEncounterbyId(3).get("statusCode"));
		
		verify(encounterDao, times(3)).findById(any(Integer.class));
	}
	
	@Test
	void testAddEncounter() {
		Patient patient1 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname1_lastname1@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		patient1.setPatientId(1);
		Encounter encounter1 = new Encounter(LocalDate.parse("2021-01-01"), LocalTime.parse("10:00:00"), "triggerIssue1", "diagnosis1", "medicines1", 500.00, patient1);
		
		Map<String, Object> addRequest = new HashMap<String, Object>();
		addRequest.put("patientId" , 1);
		addRequest.put("dateOfEncounter" , "2021-02-04");
		addRequest.put("timeOfEncounter" , "10:00:00");
		addRequest.put("triggerIssue" , "headache");
		addRequest.put("diagnosis" , "acidity");
		addRequest.put("medicines" , "some medicine");
		addRequest.put("billingAmount" , 500.00);
		
		//when all conditions are satisfied and encounter is added
		when(encounterServiceHelper.checkValidityOfRequestForAdd(addRequest)).thenReturn(true);
		when(patientDao.findById(any(Integer.class))).thenReturn(Optional.of(patient1));
		when(encounterDao.findByDateOfEncounterAndTimeOfEncounterAndPatientPatientId(any(LocalDate.class),  any(LocalTime.class), any(Integer.class))).thenReturn(Optional.empty());
		when(encounterDao.save(any(Encounter.class))).thenReturn(encounter1);
		assertEquals(HttpStatus.CREATED, encounterService.addEncounter(addRequest).get("statusCode"));
		
		
		//when not all keys are present in request
		when(encounterServiceHelper.checkValidityOfRequestForAdd(addRequest)).thenReturn(false);
		assertEquals(HttpStatus.BAD_REQUEST, encounterService.addEncounter(addRequest).get("statusCode"));
		
		//when patient for given patientId is not present
		when(encounterServiceHelper.checkValidityOfRequestForAdd(addRequest)).thenReturn(true);
		when(patientDao.findById(any(Integer.class))).thenReturn(Optional.empty());
		assertEquals(HttpStatus.NOT_FOUND, encounterService.addEncounter(addRequest).get("statusCode"));
		
		//when same encounter is already present
		when(encounterServiceHelper.checkValidityOfRequestForAdd(addRequest)).thenReturn(true);
		when(patientDao.findById(any(Integer.class))).thenReturn(Optional.of(patient1));
		when(encounterDao.findByDateOfEncounterAndTimeOfEncounterAndPatientPatientId(any(LocalDate.class),  any(LocalTime.class), any(Integer.class))).thenReturn(Optional.of(encounter1));
		assertEquals(HttpStatus.FOUND, encounterService.addEncounter(addRequest).get("statusCode"));
		
		//database Error
		when(encounterServiceHelper.checkValidityOfRequestForAdd(addRequest)).thenReturn(true);
		when(patientDao.findById(any(Integer.class))).thenReturn(Optional.of(patient1));
		when(encounterDao.findByDateOfEncounterAndTimeOfEncounterAndPatientPatientId(any(LocalDate.class),  any(LocalTime.class), any(Integer.class))).thenReturn(Optional.empty());
		when(encounterDao.save(any(Encounter.class))).thenThrow(JDBCConnectionException.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, encounterService.addEncounter(addRequest).get("statusCode"));

		verify(patientDao, times(4)).findById(any(Integer.class));
		verify(encounterServiceHelper, times(5)).checkValidityOfRequestForAdd(addRequest);
		verify(encounterDao, times(3)).findByDateOfEncounterAndTimeOfEncounterAndPatientPatientId(any(LocalDate.class),  any(LocalTime.class), any(Integer.class));
		verify(encounterDao, times(2)).save(any(Encounter.class));
	}
	
	@Test
	void testDeleteEncounter() {
		//when there is no object of for given encounterId
		when(encounterDao.findById(any(Integer.class))).thenReturn(Optional.empty());
		assertEquals(HttpStatus.NOT_FOUND, encounterService.deleteEncounter(1).get("statusCode"));
		verify(encounterDao).findById(any(Integer.class));
		
	}
	
	@Test
	void testUpdateEncounter() {
		Patient patient1 = new Patient("firstName1", "lastName1", LocalDate.parse("2000-12-12"), "b+ve", "+91 8380997285", "firstname1_lastname1@gmail.com", "Single", "Software Engineer", "address1", "city1", "district", "state", 415415);
		patient1.setPatientId(1);
		Encounter encounter1 = new Encounter(LocalDate.parse("2021-01-01"), LocalTime.parse("10:00:00"), "oldTriggerIssue", "oldDiagnosis", "oldMedicines", 500.00, patient1);
		
		Map<String, Object> updateRequest = new HashMap<String, Object>();
		updateRequest.put("encounterId" , 1);
		updateRequest.put("triggerIssue" , "headache");
		updateRequest.put("diagnosis" , "acidity");
		updateRequest.put("medicines" , "some medicine");
		updateRequest.put("billingAmount" , 500.00);
		
		//when everything is good for update
		when(encounterServiceHelper.checkValidityOfRequestForUpdate(updateRequest)).thenReturn(true);
		when(encounterDao.findById(any(Integer.class))).thenReturn(Optional.of(encounter1));
		when(encounterDao.save(any(Encounter.class))).thenReturn(encounter1);
		assertEquals(HttpStatus.OK, encounterService.updateEncounter(updateRequest).get("statusCode"));
		
		//when update request do not contains all keys
		when(encounterServiceHelper.checkValidityOfRequestForUpdate(updateRequest)).thenReturn(false);
		assertEquals(HttpStatus.BAD_REQUEST, encounterService.updateEncounter(updateRequest).get("statusCode"));
		
		//when Encounter to update is not present
		when(encounterServiceHelper.checkValidityOfRequestForUpdate(updateRequest)).thenReturn(true);
		when(encounterDao.findById(any(Integer.class))).thenReturn(Optional.empty());
		assertEquals(HttpStatus.NOT_FOUND, encounterService.updateEncounter(updateRequest).get("statusCode"));
		
		//database error
		when(encounterServiceHelper.checkValidityOfRequestForUpdate(updateRequest)).thenReturn(true);
		when(encounterDao.findById(any(Integer.class))).thenReturn(Optional.of(encounter1));
		when(encounterDao.save(any(Encounter.class))).thenThrow(JDBCConnectionException.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, encounterService.updateEncounter(updateRequest).get("statusCode"));
	
		verify(encounterDao, times(3)).findById(any(Integer.class));
		verify(encounterServiceHelper, times(4)).checkValidityOfRequestForUpdate(updateRequest);
		verify(encounterDao, times(2)).save(any(Encounter.class));
	
	}
}

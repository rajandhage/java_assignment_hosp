package com.psl.hosp.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.psl.hosp.dao.EncounterDao;
import com.psl.hosp.dao.PatientDao;
import com.psl.hosp.helper.EncounterServiceHelper;
import com.psl.hosp.model.Encounter;
import com.psl.hosp.model.Patient;

@Service
public class EncounterService {
	
	@Autowired
	private EncounterDao encounterDao;
	
	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private EncounterServiceHelper encounterServiceHelper;
	
	public Map<String, Object> getEncounterHistorybyPatient(int patientId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			returnMap.put("statusCode", HttpStatus.OK);
			returnMap.put("EncounterHistory" , encounterDao.findByPatientPatientId(patientId));
			return returnMap;
		} catch (Exception e) {
			returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
			return returnMap;
			// TODO: handle exception
		}
		
		
	}

	public Map<String, Object> getEncounterbyId(int encounterId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			Optional<Encounter> encounter = encounterDao.findById(encounterId);
			if(!encounter.isPresent()) {
				returnMap.put("statusCode", HttpStatus.NOT_FOUND);
				return returnMap;
			}
			returnMap.put("statusCode", HttpStatus.OK);
			returnMap.put("Encounter" ,encounter.get());
			return returnMap;
		} catch (Exception e) {
			returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
			return returnMap;
			// TODO: handle exception
		}
		
		
		
	}

	public Map<String, Object> addEncounter(Map<String, Object> request) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(!encounterServiceHelper.checkValidityOfRequestForAdd(request)) {
			returnMap.put("statusCode", HttpStatus.BAD_REQUEST);
			returnMap.put("message", "Check Request format for all expectations . Check console for error");
			return returnMap;
		}
		
		int patientId = Integer.parseInt(request.get("patientId").toString().trim());
		Optional<Patient> patientData = patientDao.findById(patientId);
		if(!patientData.isPresent()) {
			returnMap.put("statusCode", HttpStatus.NOT_FOUND);
			returnMap.put("message", "No patient is present for patientId : " + patientId);
			return returnMap;
		}
		LocalDate dateOfEncounter = LocalDate.parse(request.get("dateOfEncounter").toString().trim());
		LocalTime timeOfEncounter = LocalTime.parse(request.get("timeOfEncounter").toString().trim());
		Optional<Encounter> encouterData = encounterDao.findByDateOfEncounterAndTimeOfEncounterAndPatientPatientId(dateOfEncounter, timeOfEncounter, patientId);
		if(encouterData.isPresent()) {
			returnMap.put("statusCode", HttpStatus.FOUND);
			returnMap.put("message", "Encounter of this time for patientId " + patientId + " already present");
			return returnMap;
		}
		Encounter encounter = new Encounter(
				dateOfEncounter, 
				timeOfEncounter,
				request.get("triggerIssue").toString().trim(),
				request.get("diagnosis").toString().trim(),
				request.get("medicines").toString().trim(),
				Double.parseDouble(request.get("billingAmount").toString()),
				patientData.get());
		
		synchronized (encounter) {
			try {
				Encounter encounter2 = encounterDao.save(encounter);
				returnMap.put("message", "Encounter added to database with encounterId : " + encounter2.getEncounterId() + " for patient with patientId : " + patientId);
				returnMap.put("statusCode", HttpStatus.CREATED);
				return returnMap;
			} catch (Exception e) {
				returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
				returnMap.put("message", "Add Encounter failed due to database operation failure");
				return returnMap;
			}
		}
	}



	public Map<String, Object> deleteEncounter(int encounterId) {
		Optional<Encounter> encounter = encounterDao.findById(encounterId);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if(encounter.isPresent()) {
			synchronized (encounter) {
				encounterDao.deleteById(encounterId);
			}
			returnMap.put("statusCode", HttpStatus.OK);
			returnMap.put("message", "Encounter with id : " + encounterId + " Deleted");
			return returnMap;
		}else {
			returnMap.put("statusCode", HttpStatus.NOT_FOUND);
			returnMap.put("message", "No Encounter with encounterId " + encounterId +" is present");
			return returnMap;
		}
	}



	public Map<String, Object> updateEncounter(Map<String, Object> request) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(!encounterServiceHelper.checkValidityOfRequestForUpdate(request)) {
			returnMap.put("statusCode", HttpStatus.BAD_REQUEST);
			returnMap.put("message", "Check Request format for all expectations. Check console for error");
			return returnMap;
		}
		int encounterId = Integer.parseInt(request.get("encounterId").toString());
		Optional<Encounter> encounterData = encounterDao.findById(encounterId);
		if(!encounterData.isPresent()) {
			returnMap.put("statusCode", HttpStatus.NOT_FOUND);
			returnMap.put("message", "No Encounter Record present for encounterId : " + Integer.parseInt(request.get("encounterId").toString()));
			return returnMap;
		}
		Encounter encounter = encounterData.get();
		encounter.setBillingAmount(Double.parseDouble(request.get("billingAmount").toString()));
		encounter.setMedicines(request.get("medicines").toString().trim());
		encounter.setDiagnosis(request.get("diagnosis").toString().trim());
		encounter.setTriggerIssue(request.get("triggerIssue").toString().trim());
		
		synchronized (encounter) {
			try {
				Encounter encounter2 = encounterDao.save(encounter);
				returnMap.put("statusCode", HttpStatus.OK);
				returnMap.put("message", "Encounter updated in database with encounterId : " + encounter2.getEncounterId() + " for patient with patientId : " + encounter2.getPatient().getPatientId());
				return  returnMap;
			} catch (Exception e) {
				returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
				returnMap.put("message", "Update Encounter failed due to database operation failure");
				return returnMap;
			}
		}
	}



	
	
}

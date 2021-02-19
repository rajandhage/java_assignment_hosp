package com.psl.hosp.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psl.hosp.dao.EncounterDao;
import com.psl.hosp.dao.PatientDao;
import com.psl.hosp.model.Encounter;
import com.psl.hosp.model.Patient;
import com.psl.hosp.utiliy.Utility;

@Service
public class EncounterService {
	
	@Autowired
	private EncounterDao encounterDao;
	
	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private Utility utility;
	
	public List<Encounter> getEncounterHistorybyPatient(int patientId) {
		return encounterDao.findByPatientPatientId(patientId);
	}

	public Encounter getEncounterbyId(int encounterId) {
		Optional<Encounter> encounter = encounterDao.findById(encounterId);
		if(!encounter.isPresent()) {
			return null;
		}
		return encounter.get();
	}

	public Map<String, Object> addEncounter(Map<String, Object> request) throws Exception {
		utility.checkKeys(request, "dateOfEncounter", "timeOfEncounter", "triggerIssue", "diagnosis", "medicines", "billingAmount", "patientId");
		utility.checkIfStringAndNonEmpty(request, "triggerIssue");
		utility.checkIfString(request, "diagnosis", "medicines");
		utility.checkIfDateValid(request.get("dateOfEncounter").toString().trim());
		utility.checkIfTimeValid(request.get("timeOfEncounter").toString().trim());
		utility.checkIfFloat(request, "billingAmount");
		utility.checkIfInteger(request, "patientId");
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int patientId = Integer.parseInt(request.get("patientId").toString().trim());
		Optional<Patient> patientData = patientDao.findById(patientId);
		if(!patientData.isPresent()) {
			returnMap.put("message", "No patient is present for patientId : " + patientId);
			return returnMap;
		}
		
		Encounter encounter = new Encounter(
				LocalDate.parse(request.get("dateOfEncounter").toString().trim()), 
				LocalTime.parse(request.get("timeOfEncounter").toString().trim()), 
				request.get("triggerIssue").toString().trim(),
				request.get("diagnosis").toString().trim(),
				request.get("medicines").toString().trim(),
				Double.parseDouble(request.get("billingAmount").toString()),
				patientData.get());
		
		synchronized (encounter) {
			try {
				Encounter encounter2 = encounterDao.save(encounter);
				returnMap.put("message", "Encounter added to database with encounterId : " + encounter2.getEncounterId() + "for patient with patientId : " + patientId);
				return returnMap;
			} catch (Exception e) {
				// TODO: handle exception
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
			returnMap.put("message", "Encounter with id : " + encounterId + " Deleted");
			return returnMap;
		}else {
			returnMap.put("message", "No Encounter with encounterId " + encounterId +" is present");
			return returnMap;
		}
	}



	public Map<String, Object> updateEncounter(Map<String, Object> request) throws Exception {
		utility.checkKeys(request, "encounterId", "triggerIssue", "diagnosis", "medicines", "billingAmount");
		utility.checkIfStringAndNonEmpty(request, "triggerIssue");
		utility.checkIfString(request, "diagnosis", "medicines");
		utility.checkIfFloat(request, "billingAmount");
		utility.checkIfInteger(request, "encounterId");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		int encounterId = Integer.parseInt(request.get("encounterId").toString());
		Optional<Encounter> encounterData = encounterDao.findById(encounterId);
		if(!encounterData.isPresent()) {
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
				returnMap.put("message", "Encounter updated in database with encounterId : " + encounter2.getEncounterId() + "for patient with patientId : " + encounter2.getPatient().getPatientId());
				return  returnMap;
			} catch (Exception e) {
				returnMap.put("message", "Update Encounter failed due to database operation failure");
				return returnMap;
			}
		}
	}



	
	
}

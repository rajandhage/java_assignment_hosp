package com.psl.hosp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psl.hosp.dao.PatientDao;
import com.psl.hosp.model.Patient;
import com.psl.hosp.utiliy.Utility;

@Service
public class PatientService {
	
	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private Utility utility;
	
	public List<Patient> getAllPatients() {
		
		List<Patient> patientList = new ArrayList<Patient>();
		System.out.println(patientDao.findAll().getClass());
		patientDao.findAll().forEach(patientList::add);
		return patientList;
	}

	public Map<String, Object> addPatient(Map<String, Object> request) throws Exception {
		utility.checkKeys(request, "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state", "dateOfBirth", "pinCode");
		utility.checkIfStringAndNonEmpty(request, "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state");
		utility.checkIfInteger(request, "pinCode");
		utility.checkIfDateValid(request.get("dateOfBirth").toString().trim());
		String mailId = request.get("mailId").toString().trim();
		Map<String, Object> map = new HashMap<String, Object>();
		Optional<Patient> existingPatient = patientDao.findByMailId(mailId);
		if(existingPatient.isPresent()) {
			map.put("message", "Patient having same mailId already exists");
			map.put("PatientId", existingPatient.get().getPatientId());
			return map;
		}
		
		Patient patient = new Patient(request.get("firstName").toString().trim(), 
				request.get("lastName").toString().trim(), 
				LocalDate.parse(request.get("dateOfBirth").toString().trim()), 
				request.get("bloodGroup").toString().trim(), 
				request.get("contactNumber").toString().trim(), 
				mailId, 
				request.get("maritalStatus").toString().trim(), 
				request.get("occupation").toString().trim(), 
				request.get("address").toString().trim(),
				request.get("city").toString().trim(), 
				request.get("district").toString().trim(), 
				request.get("state").toString().trim(), 
				Integer.parseInt(request.get("pinCode").toString().trim())
				);
		
		synchronized (patient) {
			try {
				
				map.put("PatientId", patientDao.save(patient).getPatientId());
				return map;
			}catch (Exception e) {
				System.out.println("Error while adding patient in database");
				throw e;
				}
		}
		
	}

	public Patient getPatientById(int patientid){
		Optional<Patient> patient = patientDao.findById(patientid);
		if(patient.isPresent()) {
			return patient.get();
		}else {
			return null;
		}
	}

	public Map<String, Object> deletePatient(int patientId) {
		Optional<Patient> patient = patientDao.findById(patientId);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if(patient.isPresent()) {
			synchronized (patient) {
				patientDao.deleteById(patientId);
			}
			returnMap.put("message", "Deleted");
			return returnMap;
		}else {
			returnMap.put("message", "No patient with patientId " + patientId +" is present");
			return returnMap;
		}
		
	}

	public Map<String, Object> updatePatient(Map<String, Object> request) throws Exception {
		utility.checkKeys(request, "patientId", "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state", "dateOfBirth", "pinCode");
		utility.checkIfStringAndNonEmpty(request, "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state");
		utility.checkIfInteger(request, "pinCode", "patientId");
		utility.checkIfDateValid(request.get("dateOfBirth").toString().trim());
		int patientId = Integer.parseInt(request.get("patientId").toString().trim());
		Optional<Patient> patientData = patientDao.findById(patientId);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(!patientData.isPresent()) {
			returnMap.put("message", "No patient data is present for patientId : " + patientId);
			return returnMap;
		}
		Patient patient = patientData.get();
		patient.setFirstName(request.get("firstName").toString().trim());
		patient.setLastName(request.get("lastName").toString().trim());
		patient.setBloodGroup(request.get("bloodGroup").toString().trim());
		patient.setContactNumber(request.get("contactNumber").toString().trim());
		patient.setMailId(request.get("mailId").toString().trim());
		patient.setMaritalStatus(request.get("maritalStatus").toString().trim());
		patient.setOccupation(request.get("occupation").toString().trim());
		patient.setAddress(request.get("address").toString().trim());
		patient.setCity(request.get("city").toString().trim());
		patient.setDistrict(request.get("district").toString().trim());
		patient.setState(request.get("state").toString().trim());
		patient.setDateOfBirth(LocalDate.parse(request.get("dateOfBirth").toString().trim()));
		patient.setPinCode(Integer.parseInt(request.get("pinCode").toString().trim()));
		
		synchronized (patient) {
			try {
				patientDao.save(patient);
				returnMap.put("message", "updated successfully");
				return returnMap;
			} catch (Exception e) {
				returnMap.put("message", "update failed due to database error");
				return returnMap;
			}
			
		}
	}

	
}

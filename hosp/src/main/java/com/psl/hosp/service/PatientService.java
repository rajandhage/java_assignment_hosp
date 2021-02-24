package com.psl.hosp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.psl.hosp.dao.PatientDao;
import com.psl.hosp.helper.PatientServiceHelper;
import com.psl.hosp.model.Patient;

@Service
public class PatientService {
	
	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private PatientServiceHelper patientServiceHelper;
	
	public Map<String, Object> getAllPatients() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Patient> patientList = new ArrayList<Patient>();
		try {
		patientDao.findAll().forEach(patientList::add);
		returnMap.put("statusCode", HttpStatus.OK);
		returnMap.put("ListOfPatients", patientList);
		return returnMap;
		
		} catch (Exception e) {
			returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
			returnMap.put("ListOfPatients", patientList);
			return returnMap;
		}
		
	}

	public Map<String, Object> addPatient(Map<String, Object> request){
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(!patientServiceHelper.checkValidityOfRequestForAdd(request)) {
			map.put("statusCode", HttpStatus.BAD_REQUEST);
			map.put("message", "Check Request format for all expectations. Check console for error");
			return map;
		}
		String mailId = request.get("mailId").toString().trim();
		
		Optional<Patient> existingPatient = patientDao.findByMailId(mailId);
		if(existingPatient.isPresent()) {
			map.put("statusCode", HttpStatus.NOT_FOUND);
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
				map.put("statusCode", HttpStatus.CREATED);
				map.put("message", "Patient added with PatientId : " + patientDao.save(patient).getPatientId());
				return map;
			}catch (Exception e) {
				System.out.println(e);
				map.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
				map.put("message", "Error while adding patient in database");
				return map;
			}
		}
		
	}
	
	

	public Map<String, Object> getPatientById(int patientid){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			Optional<Patient> patient = patientDao.findById(patientid);
			if(patient.isPresent()) {
				returnMap.put("statusCode", HttpStatus.OK);
				returnMap.put("Patient" ,patient.get());
				return returnMap;
			}else {
				returnMap.put("statusCode", HttpStatus.NOT_FOUND);
				return returnMap;
			}
			
		} catch (Exception e) {
			returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
			return returnMap;
		}
		
	}

	public Map<String, Object> deletePatient(int patientId) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			Optional<Patient> patient = patientDao.findById(patientId);
			if(patient.isPresent()) {
				synchronized (patient) {
					patientDao.deleteById(patientId);
				}
				returnMap.put("statusCode", HttpStatus.OK);
				returnMap.put("message", "Deleted");
				return returnMap;
			}else {
				returnMap.put("statusCode", HttpStatus.NOT_FOUND);
				returnMap.put("message", "No patient with patientId " + patientId +" is present");
				return returnMap;
			}
			
		} catch (Exception e) {
			returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
			return returnMap;
		}
		
		
	}

	public Map<String, Object> updatePatient(Map<String, Object> request)  {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(!patientServiceHelper.checkValidityOfRequestForUpdate(request)) {
			returnMap.put("statusCode", HttpStatus.BAD_REQUEST);
			returnMap.put("message", "Check Request format for all expectations. Check console for error");
			return returnMap;
		}
		int patientId = Integer.parseInt(request.get("patientId").toString().trim());
		Optional<Patient> patientData = patientDao.findById(patientId);
		if(!patientData.isPresent()) {
			returnMap.put("statusCode", HttpStatus.NOT_FOUND);
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
				returnMap.put("statusCode", HttpStatus.OK);
				returnMap.put("message", "updated successfully");
				return returnMap;
			} catch (Exception e) {
				returnMap.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
				returnMap.put("message", "update failed due to database error");
				return returnMap;
			}
		}
	}

	
}

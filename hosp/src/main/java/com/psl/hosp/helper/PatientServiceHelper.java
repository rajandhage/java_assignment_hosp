package com.psl.hosp.helper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.psl.hosp.utiliy.Utility;

@Component
public class PatientServiceHelper {
	@Autowired
	private Utility utility;
	
	public boolean checkValidityOfRequestForUpdate(Map<String, Object> request) {
		return
		utility.checkKeys(request, "patientId", "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state", "dateOfBirth", "pinCode")
		&& utility.checkIfStringAndNonEmpty(request, "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state")
		&& utility.checkIfInteger(request, "pinCode", "patientId")
		&& utility.checkIfDateValid(request.get("dateOfBirth").toString().trim());
	}

	public boolean checkValidityOfRequestForAdd(Map<String, Object> request) {
		return
		utility.checkKeys(request, "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state", "dateOfBirth", "pinCode")
		&& utility.checkIfStringAndNonEmpty(request, "firstName", "lastName", "bloodGroup", "contactNumber", "mailId", "maritalStatus", "occupation", "address", "city", "district", "state")
		&& utility.checkIfInteger(request, "pinCode")
		&& utility.checkIfDateValid(request.get("dateOfBirth").toString().trim());
	}
	
	
}

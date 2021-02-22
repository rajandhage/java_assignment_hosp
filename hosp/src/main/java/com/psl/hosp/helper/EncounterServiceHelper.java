package com.psl.hosp.helper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.psl.hosp.utiliy.Utility;


@Component
public class EncounterServiceHelper {

	@Autowired
	private Utility utility;
	
	public boolean checkValidityOfRequestForAdd(Map<String, Object> request)  {
		return utility.checkKeys(request, "dateOfEncounter", "timeOfEncounter", "triggerIssue", "diagnosis", "medicines", "billingAmount", "patientId")
		&& utility.checkIfStringAndNonEmpty(request, "triggerIssue")
		&& utility.checkIfString(request, "diagnosis", "medicines")
		&& utility.checkIfDateValid(request.get("dateOfEncounter").toString().trim())
		&& utility.checkIfTimeValid(request.get("timeOfEncounter").toString().trim())
		&& utility.checkIfFloat(request, "billingAmount")
		&& utility.checkIfInteger(request, "patientId");
		
	}

	public boolean checkValidityOfRequestForUpdate(Map<String, Object> request) {
		return utility.checkKeys(request, "encounterId", "triggerIssue", "diagnosis", "medicines", "billingAmount")
		&& utility.checkIfStringAndNonEmpty(request, "triggerIssue")
		&& utility.checkIfString(request, "diagnosis", "medicines")
		&& utility.checkIfFloat(request, "billingAmount")
		&& utility.checkIfInteger(request, "encounterId");
	}
}

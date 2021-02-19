package com.psl.hosp.utiliy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Utility {
	
	public void checkKeys(Map<String, Object> request, String... keys) throws Exception {
		for(String key : keys) 
			if(!request.containsKey(key))
				throw new Exception("post keys not found");
	}
	
	public void checkIfStringAndNonEmpty(Map<String, Object> request, String... keys) throws Exception {
		for (String key : keys)
			if( !(request.get(key) instanceof String) || ((String) request.get(key)).trim().isEmpty() ) 
				throw new Exception("enter valid info" + " for " + key);
	}

	public void checkIfPositiveValues(Map<String, Object> request, String... keys) throws Exception {
		for (String key : keys)
			if ((Integer) request.get(key) <= 0)
				throw new Exception("enter valid info" + " for " + key);
	}

	public void checkIfInteger(Map<String, Object> request, String... keys) throws Exception {
		for (String key : keys)
			if (!(request.get(key) instanceof Integer))
				throw new Exception("not an integer" + " for " + key);
	}

	public void checkIfDateValid(String date) throws Exception {
		try {
				LocalDate.parse(date);
		} catch (Exception e) {
			throw new Exception("invalid date");
		}
	}
				
	public void checkIfTimeValid(String time) throws Exception {
		try {
				LocalTime.parse(time);
		} catch (Exception e) {
			throw new Exception("invalid Time");
		}
	}

	public void checkIfString(Map<String, Object> request, String... keys) throws Exception {
		// TODO Auto-generated method stub
		for (String key : keys)
			if( !(request.get(key) instanceof String)) 
				throw new Exception("enter valid info" + " for " + key);
		
	}

	public void checkIfFloat(Map<String, Object> request, String... keys) throws Exception {
		// TODO Auto-generated method stub
		for (String key : keys)
			if (!((Double)request.get(key) instanceof Double))
				throw new Exception("not an float" + " for " + key);
	}

}

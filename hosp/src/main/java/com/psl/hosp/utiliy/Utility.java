package com.psl.hosp.utiliy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Utility {
	
	public boolean checkKeys(Map<String, Object> request, String... keys) throws Exception {
		for(String key : keys) 
			if(!request.containsKey(key)) {
				//throw new Exception("post keys not found");
				System.out.println("post keys not found");
				return false;
			}
		return true;
	}
	
	public boolean checkIfStringAndNonEmpty(Map<String, Object> request, String... keys) throws Exception {
		for (String key : keys)
			if( !(request.get(key) instanceof String) || ((String) request.get(key)).trim().isEmpty() ) {
				//throw new Exception("enter valid info" + " for " + key);
				System.out.println("enter valid info" + " for " + key);
				return false;
			}
		return true;
	}

	public boolean checkIfPositiveValues(Map<String, Object> request, String... keys) throws Exception {
		for (String key : keys) {
			if ((Integer) request.get(key) <= 0) {
//				throw new Exception("enter valid info" + " for " + key);
				System.out.println("enter valid info" + " for " + key);
				return false;
			}
		}
		return true;
	}

	public boolean checkIfInteger(Map<String, Object> request, String... keys) throws Exception {
		for (String key : keys) {
			if (!(request.get(key) instanceof Integer)) {
				//throw new Exception("not an integer" + " for " + key);
				System.out.println("not an integer" + " for " + key);
				return false;
			}
		}
		return true;
	}

	public boolean checkIfDateValid(String date) throws Exception {
		try {
				LocalDate.parse(date);
				return true;
		} catch (Exception e) {
			System.out.println("invalid date");
			return false;
			//throw new Exception("invalid date");
		}
	}
				
	public boolean checkIfTimeValid(String time) throws Exception {
		try {
				LocalTime.parse(time);
				return true;
		} catch (Exception e) {
			System.out.println("invalid Time");
			return false;
		}
	}

	public boolean checkIfString(Map<String, Object> request, String... keys) throws Exception {
		// TODO Auto-generated method stub
		for (String key : keys)
			if( !(request.get(key) instanceof String)) {
				//throw new Exception("enter valid info" + " for " + key);
				System.out.println("enter valid info" + " for " + key);
				return false;
			}
		return true;
		
	}

	public boolean checkIfFloat(Map<String, Object> request, String... keys) throws Exception {
		// TODO Auto-generated method stub
		for (String key : keys)
			if (!((Double)request.get(key) instanceof Double)) {
				//throw new Exception("not an float" + " for " + key);
				System.out.println("not an float" + " for " + key);
				return false;
			}
		return true;
	}

}

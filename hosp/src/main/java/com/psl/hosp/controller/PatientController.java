package com.psl.hosp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psl.hosp.service.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	@GetMapping("/getAll")
	public Map<String, Object> getAllPatients(){
		return patientService.getAllPatients();
	}
	
	@GetMapping("/getById/{patientId}")
	public Map<String, Object> getPatientById(@PathVariable int patientId){
		return patientService.getPatientById(patientId);
	}
	
	@PostMapping("/add")
	public Map<String, Object> addPatient(@RequestBody Map<String, Object> request) {
		return patientService.addPatient(request);
	}
	
	@PutMapping("/update")
	public Map<String, Object> updatePatient(@RequestBody Map<String, Object> request) throws Exception {
		return patientService.updatePatient(request);
	}
	
	@DeleteMapping("/delete/{patientId}")
	public Map<String, Object> deletePatient(@PathVariable int patientId) {
		return patientService.deletePatient(patientId);
	}

}

package com.psl.hosp.controller;

import java.util.List;
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

import com.psl.hosp.model.Encounter;
import com.psl.hosp.service.EncounterService;


@RestController
@RequestMapping("/encounter")
public class EncounterController {
	
	@Autowired
	private EncounterService encounterService;
	
	@GetMapping("/patientId/{patientId}")
	public List<Encounter> getEncounterHistorybyPatient(@PathVariable("patientId") int patientId){
		return encounterService.getEncounterHistorybyPatient(patientId);
	}
	
	@GetMapping("/encounterId/{encounterId}")
	public Encounter getEncounterbyId(@PathVariable("encounterId") int encounterId){
		return encounterService.getEncounterbyId(encounterId);
	}
	
	
	@PostMapping("/addEncounter")
	public Map<String, Object> addEncounter(@RequestBody Map<String, Object> request) throws Exception{
		return encounterService.addEncounter(request);
	}
	
	@PutMapping("/updateEncounter")
	public Map<String, Object> updateEncounter(@RequestBody Map<String, Object> request) throws Exception{
		return encounterService.updateEncounter(request);
	}
	
	@DeleteMapping("/deleteEncounter/{encounterId}")
	public Map<String, Object> deleteEncounter(@PathVariable("encounterId") int encounterId){
		return encounterService.deleteEncounter(encounterId);
	}
}

package com.psl.hosp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.psl.hosp.model.Encounter;
import com.psl.hosp.model.Patient;

@Repository
public interface EncounterDao extends CrudRepository<Encounter, Integer>{
	public List<Encounter> findByPatientPatientId(int patientId);
}

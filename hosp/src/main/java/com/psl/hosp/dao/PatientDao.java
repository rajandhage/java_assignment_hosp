package com.psl.hosp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.psl.hosp.model.Patient;

@Repository
public interface PatientDao extends CrudRepository<Patient, Integer>{
	Optional<Patient> findByMailId(String mailId);
}

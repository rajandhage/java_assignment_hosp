package com.psl.hosp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="encounter")
public class Encounter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6537247897594951405L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="encounter_id")
	private int encounterId;
	
	@Column(name="date_of_encounter")
	private LocalDate dateOfEncounter;
	
	@Column(name="time_of_encounter")
	private LocalTime timeOfEncounter;
	
	@Column(name="trigger_issue")
	private String triggerIssue;
	
	@Column(name="diagnosis")
	private String diagnosis;
	
	@Column(name="medicines")
	private String medicines;
	
	@Column(name="billing_amount")
	private Double billingAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="patient_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Patient patient;
	
	public Encounter(LocalDate dateOfEncounter, LocalTime timeOfEncounter, String triggerIssue, String diagnosis,
			String medicines, Double billingAmount, Patient patient) {
		super();
		this.dateOfEncounter = dateOfEncounter;
		this.timeOfEncounter = timeOfEncounter;
		this.triggerIssue = triggerIssue;
		this.diagnosis = diagnosis;
		this.medicines = medicines;
		this.billingAmount = billingAmount;
		this.patient = patient;
	}

	public Encounter() {
		super();
	}

	public int getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(int encounterId) {
		this.encounterId = encounterId;
	}

	public LocalTime getTimeOfEncounter() {
		return timeOfEncounter;
	}

	public void setTimeOfEncounter(LocalTime timeOfEncounter) {
		this.timeOfEncounter = timeOfEncounter;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public LocalDate getDateOfEncounter() {
		return dateOfEncounter;
	}

	public void setDateOfEncounter(LocalDate dateOfEncounter) {
		this.dateOfEncounter = dateOfEncounter;
	}

	public String getTriggerIssue() {
		return triggerIssue;
	}

	public void setTriggerIssue(String triggerIssue) {
		this.triggerIssue = triggerIssue;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getMedicines() {
		return medicines;
	}

	public void setMedicines(String medicines) {
		this.medicines = medicines;
	}

	public Double getBillingAmount() {
		return billingAmount;
	}

	public void setBillingAmount(Double billingAmount) {
		this.billingAmount = billingAmount;
	}

	@Override
	public String toString() {
		return "Encounter [encounterId=" + encounterId + ", dateOfEncounter=" + dateOfEncounter + ", timeOfEncounter="
				+ timeOfEncounter + ", triggerIssue=" + triggerIssue + ", diagnosis=" + diagnosis + ", medicines="
				+ medicines + ", billingAmount=" + billingAmount + ", patient=" + patient + "]";
	}
	
	
}

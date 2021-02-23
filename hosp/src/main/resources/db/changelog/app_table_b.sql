CREATE TABLE encounter (
encounter_id integer not null auto_increment, 
billing_amount double precision,
date_of_encounter date,
diagnosis VARCHAR(255),
medicines VARCHAR(255), 
time_of_encounter time, 
trigger_issue VARCHAR(255),
patient_id integer, 
primary key (encounter_id),
FOREIGN KEY (patient_id) REFERENCES patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE
);

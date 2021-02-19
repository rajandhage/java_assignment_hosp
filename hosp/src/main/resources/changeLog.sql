CREATE TABLE patient(
patient_id integer not null auto_increment,
address VARCHAR(255),
blood_group VARCHAR(255), 
city VARCHAR(255), 
contact_number VARCHAR(255), 
date_of_birth date,
district VARCHAR(255),
first_name VARCHAR(255),
last_name VARCHAR(255),
mail_id VARCHAR(255),
marital_status VARCHAR(255),
occupation VARCHAR(255),
pin_code integer,
state VARCHAR(255),
primary key (patient_id)
);
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
FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
);

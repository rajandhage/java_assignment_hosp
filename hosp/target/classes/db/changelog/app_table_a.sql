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
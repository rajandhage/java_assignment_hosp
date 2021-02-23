create table patient_encounters(
patient_patient_id int not null ,
encounters_encounter_id int not null unique,
primary key(encounters_encounter_id),
foreign key (encounters_encounter_id) references encounter (encounter_id),
foreign key (patient_patient_id) references patient (patient_id)
);
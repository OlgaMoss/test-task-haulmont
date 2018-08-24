CREATE TABLE Patient (
   patient_id NUMERIC IDENTITY PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   lastname VARCHAR(50) NOT NULL,
   patronymic VARCHAR(50) NOT NULL,
   phone_number VARCHAR(15)
);

CREATE TABLE Doctor (
   doctor_id NUMERIC IDENTITY PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   lastname VARCHAR(50) NOT NULL,
   patronymic VARCHAR(50) NOT NULL,
   specialty VARCHAR(60) NOT NULL
);

CREATE TABLE Priority (
  priority_id NUMERIC IDENTITY PRIMARY KEY,
  name VARCHAR(6) NOT NULL
);

INSERT INTO Priority (name) VALUES ('Normal');
INSERT INTO Priority (name) VALUES ('Cito');
INSERT INTO Priority (name) VALUES ('Statim');

CREATE TABLE Recipe (
   recipe_id NUMERIC IDENTITY PRIMARY KEY,
   description VARCHAR(255) NOT NULL,
   doctor_id NUMERIC NOT NULL,
   patient_id NUMERIC NOT NULL,
   creation_date DATE NOT NULL,
   duration INTEGER,
   priority_id NUMERIC,
   FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id),
   FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
   FOREIGN KEY (priority_id) REFERENCES Priority(priority_id)
);
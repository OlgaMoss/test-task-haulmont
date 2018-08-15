CREATE TABLE patients_tbl (
   id_patient NUMERIC IDENTITY PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   lastname VARCHAR(50) NOT NULL,
   patronymic VARCHAR(50) NOT NULL,
   phone_number VARCHAR(15)
);

CREATE TABLE doctors_tbl (
   id_doctor NUMERIC IDENTITY PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   lastname VARCHAR(50) NOT NULL,
   patronymic VARCHAR(50) NOT NULL,
   specialty VARCHAR(60) NOT NULL
);

CREATE TABLE priority_tbl (
  id_priority NUMERIC IDENTITY PRIMARY KEY,
  priority VARCHAR(6) NOT NULL
);

INSERT INTO priority_tbl (priority) VALUES ('Normal');
INSERT INTO priority_tbl (priority) VALUES ('Cito');
INSERT INTO priority_tbl (priority) VALUES ('Statim');

CREATE TABLE recipes_tbl (
   id_recipe NUMERIC IDENTITY PRIMARY KEY,
   description VARCHAR(255) NOT NULL,
   id_doctor NUMERIC NOT NULL,
   id_patient NUMERIC NOT NULL,
   creation_date DATE NOT NULL,
   duration DATE,
   id_priority NUMERIC,
   FOREIGN KEY (id_doctor) REFERENCES doctors_tbl(id_doctor),
   FOREIGN KEY (id_patient) REFERENCES patients_tbl(id_patient),
   FOREIGN KEY (id_priority) REFERENCES priority_tbl(id_priority)
);
CREATE TABLE IF NOT EXISTS public.department
(
    departmentId INT NOT NULL,
    name VARCHAR (50) UNIQUE NOT NULL,
    CONSTRAINT department_pkey PRIMARY KEY (departmentId)
);

CREATE TABLE IF NOT EXISTS public.address
(
    addressId SERIAL NOT NULL,
    address VARCHAR (255) NOT NULL,
    city VARCHAR (255) NOT NULL,
    country VARCHAR (255) NOT NULL,
    CONSTRAINT address_pkey PRIMARY KEY (addressId)
);
CREATE TABLE IF NOT EXISTS public.employee
(
    employeeId SERIAL NOT NULL,
    lastName VARCHAR (50) NOT NULL,
    firstName VARCHAR (50) NOT NULL,
    dateOfBirth Date,
    nationality VARCHAR (50),
    primaryEmail VARCHAR (50) UNIQUE NOT NULL,
    mobilePhone VARCHAR (50) UNIQUE NOT NULL,
    skype VARCHAR (50) UNIQUE NOT NULL,
    emergencyContact VARCHAR (50),
    department INT,
    address INT,
    CONSTRAINT employee_pkey PRIMARY KEY (employeeId),
    CONSTRAINT fk_employee_department FOREIGN KEY (department) REFERENCES department (departmentId),
    CONSTRAINT fk_employee_address FOREIGN KEY (address) REFERENCES address (addressId)
);
CREATE TABLE IF NOT EXISTS public.certificate
(
    certificateId SERIAL NOT NULL,
    name VARCHAR (50),
    type VARCHAR (50),
    year INT,
    employeeid integer,
    CONSTRAINT certificate_pkey PRIMARY KEY (certificateId),
    CONSTRAINT fk_employee_certificate FOREIGN KEY (employeeid) REFERENCES employee (employeeId)
);
CREATE TABLE IF NOT EXISTS public.contact
(
    contactId SERIAL NOT NULL,
    type VARCHAR (50),
    value VARCHAR (50),
    employeeid integer,
    CONSTRAINT contact_pkey PRIMARY KEY (contactId),
    CONSTRAINT fk_employee_contact FOREIGN KEY (employeeid) REFERENCES employee (employeeId)
);

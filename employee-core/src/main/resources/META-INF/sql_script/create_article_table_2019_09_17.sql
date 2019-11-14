CREATE TABLE IF NOT EXISTS public.article
(
	articleId SERIAL NOT NULL,
	fileName VARCHAR (100) NOT NULL,
	employeeid integer,
	CONSTRAINT article_pkey PRIMARY KEY (articleId),
	CONSTRAINT fk_employee_article FOREIGN KEY (employeeid) REFERENCES employee(employeeId)
);


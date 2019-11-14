
CREATE SEQUENCE public.department_deptid
    INCREMENT 1
    START 6
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
   
    
ALTER TABLE department ALTER COLUMN departmentId SET DEFAULT
nextval('department_deptid'::regclass);


ALTER TABLE public.department  DROP CONSTRAINT department_name_key;
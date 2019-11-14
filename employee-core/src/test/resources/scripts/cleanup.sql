delete from public.contact;
alter SEQUENCE 	public.contact_contactid_seq restart with 1;

delete from public.certificate;
alter SEQUENCE 	public.certificate_certificateid_seq restart with 1;

delete from public.employee;
alter SEQUENCE 	public.employee_employeeid_seq restart with 1;

delete from public.address;
alter SEQUENCE 	public.address_addressid_seq restart with 1;

delete from public.department;
alter SEQUENCE public.department_deptid restart with 1;
alter SEQUENCE 	public.department_departmentid_seq restart with 1;

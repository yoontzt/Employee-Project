insert into department(departmentId,name) values (1,'ICT');
insert into department(departmentId,name) values (2,'HR');
insert into department(departmentId,name) values (3,'Testing');
insert into department(departmentId,name) values (4,'Marketing');
insert into department(departmentId,name) values (5,'Development');

insert into address(address,city,country) values('Hai Au Building 10th Floor,39B Truong Son street,4,Tan Binh District','Ho chi Minh','Vietnam');
insert into address(address,city,country) values('No.155,Ma Soe Yin Kyaung street,9 Miles,Mayangone Township','Yangon','Myanmar');
insert into address(address,city,country) values('Schlössli Schönegg,Wilhelmshöhe,6003,luzern','luzern state','Switzerland');
insert into address(address,city,country) values('5/16,Tastentanzenstrasse,1234,Zuerich','Zuerich state','Switzerland');
insert into address(address,city,country) values('No.5,Pyay Road,Hlaing RC2 Campus,Hlaing Township','Yangon','Myanmar');
insert into address(address,city,country) values('imperial An Phu 10th Floor,Vu Tong Phan street,An Phu,2 District','Ho chi Minh','Vietnam');
insert into address(address,city,country) values('No.100,Insein Road,Hledan,Kamayut Township','Yangon','Myanmar');
insert into address(address,city,country) values('No.5,Pyay Road,Hlaing RC2 Campus,Hlaing Township','Yangon','Myanmar');
insert into address(address,city,country) values('No.5,Pyay Road,Hlaing RC2 Campus,Hlaing Township','Yangon','Myanmar');

insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Htet','Aung','19-Jan-1998','Myanmar','aung1@gmail.com','+95-996-555-9150','aung.htet123','Mother : +95-991-555-123',1,1);
insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Trung','Bui','19-Jan-1998','Myanmar','bui1@gmail.com','+95-996-555-60','trung.bui123','Father : +95-991-555-9568',2,2);
insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Land','Candy','19-Jan-1998','Myanmar','candy1@gmail.com','+95-943-555-4854','candy.land123','Uncle : +95-973-555-954',3,3);

insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Trung','Dung','19-Jan-1998','Switzerland','dung1@gmail.com','+41-785-553-382','dung.trung123','Mother : +41-755-553-387',4,4);
insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Thinzar','Yoon','19-Aug-1998','Switzerland','yoon1@gmail.com','+41-785-559-490','yoon.tzt123','Mother : +41-765-553-713',5,5);
insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('kumari','Sindhu','19-July-1998','Switzerland','kumari1@gmail.com','+41-755-552-177','sindhu.kmi123','Mother : +41-785-555-650',6,1);

insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Nguyen','Thanh','19-Feb-1998','Vietnamese','thanh.nguyen@gmail.com','+84-355-505-187','thanh123','Father : +84-555-517-160',7,2);
insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Nguyen','Trung','19-Jan-1998','Vietnamese','trung.nguyen@gmail.com','+84-555-532-788','phoo.pwint123','Father : +84-355-500-932',8,3);
insert into employee(lastName,firstName,dateOfBirth,nationality,primaryEmail,mobilePhone,skype,emergencyContact,address,department) values('Tran','Hung','5-Jan-1998','Vietnamese','thin1@gmail.com','+84-355-525-412','thin.123','Father : +84-755-545-385',9,4);

insert into contact(type,value,employeeId) values('email','aung2@gmail.com',1);
insert into contact(type,value,employeeId) values('skype','aung.htet2',1);
insert into contact(type,value,employeeId) values('homephone','001111111',1);
insert into contact(type,value,employeeId) values('email','bui2@gmail.com',2);
insert into contact(type,value,employeeId) values('email','trung2@gmail.com',2);
insert into contact(type,value,employeeId) values('email','cand2@gmail.com',3);
insert into contact(type,value,employeeId) values('skype','skypiTTN',3);
insert into contact(type,value,employeeId) values('homephone','987654321',4);
insert into contact(type,value,employeeId) values('email','dung2@gmail.com',4);
insert into contact(type,value,employeeId) values('email','yoon@yahoo.com',5);
insert into contact(type,value,employeeId) values('skype','sindhu@gmail.com',6);
insert into contact(type,value,employeeId) values('homephone','254374692',7);
insert into contact(type,value,employeeId) values('email','phoo@outlook.com',8);
insert into contact(type,value,employeeId) values('homephone','786944478',9);
insert into contact(type,value,employeeId) values('email','thin@outlook.com',9);

insert into certificate(name,type,year,employeeId) values('JLPT','Language',1998,1);
insert into certificate(name,type,year,employeeId) values('TOFEL','Language',1998,2);
insert into certificate(name,type,year,employeeId) values('java','Programming',1998,2);
insert into certificate(name,type,year,employeeId) values('JLPT','Language',1998,3);
insert into certificate(name,type,year,employeeId) values('CCNA','Networking',1998,3);
insert into certificate(name,type,year,employeeId) values('MySQL','DB',1998,4);
insert into certificate(name,type,year,employeeId) values('IELTS','Language',1998,5);
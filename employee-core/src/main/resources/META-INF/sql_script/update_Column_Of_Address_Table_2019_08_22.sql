ALTER TABLE address DROP building ;
ALTER TABLE address DROP street;
ALTER TABLE address DROP ward;
ALTER TABLE address DROP district;
ALTER TABLE address ADD address VARCHAR (255) not null;
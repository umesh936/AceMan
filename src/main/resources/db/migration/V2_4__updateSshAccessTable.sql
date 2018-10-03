-- ---------------------------------
-- table for HOP machines
-- ---------------------------------

Alter table `aceman`.`ssh_access_detail` add column vpc_id varchar(100) DEFAULT NULL;
Alter table `aceman`.`vpc_hop` add column url_path varchar(500) DEFAULT NULL;
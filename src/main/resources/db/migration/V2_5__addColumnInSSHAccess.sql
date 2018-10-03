
-- -------------------------------------
-- SSH Access Table
-- --------------------------------------

Alter table `aceman`.`ssh_access_detail` add column expiry_date date DEFAULT NULL ;
Alter table `aceman`.`ssh_access_detail` add column is_expired bit(1) DEFAULT NULL ;
-- ---------------------------------
-- table for HOP machines
-- ---------------------------------

CREATE TABLE `aceman`.`vpc_hop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aws_account_id` int(11)  NOT NULL,
  `vpc_id` varchar(500)  NOT NULL,
  `added_on` datetime DEFAULT NULL,
  `added_by` varchar(100) DEFAULT NULL,
  `ip` varchar(500)  NOT NULL,
  `keypair_id` int(11),
  CONSTRAINT `keypair_id` FOREIGN KEY (`keypair_id`) REFERENCES `key_pair` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  KEY `idx_search` (`aws_account_id` ASC, `vpc_id` ASC), PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

Alter table `aceman`.`whitelist_vpc` add column vpc_name varchar(100) DEFAULT NULL;
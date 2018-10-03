-- ---------------------------------
-- Whitelist Table 
-- ---------------------------------

CREATE TABLE `aceman`.`whitelist_vpc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aws_account_id` int(11)  NOT NULL,
  `vpc_id` varchar(500)  NOT NULL,
  `added_on` datetime DEFAULT NULL,
  `added_by` varchar(100) DEFAULT NULL,
  KEY `idx_search` (`aws_account_id` ASC, `vpc_id` ASC),
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
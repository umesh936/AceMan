-- -------------------------------------
-- Audit Log
-- --------------------------------------

CREATE TABLE `audit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user` varchar(100) DEFAULT NULL,
  `access_type` varchar(45) DEFAULT NULL,
  `to_user` varchar(100) DEFAULT NULL,
  `event_date` date DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `is_success` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

-- -------------------------------------
-- AWS Account
-- --------------------------------------
CREATE TABLE `aws_account` (
  `id` int(11) NOT NULL,
  `access_key` varchar(500) DEFAULT NULL,
  `secret_key` varchar(500) DEFAULT NULL,
  `logical_name` varchar(45) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `added_on` datetime DEFAULT NULL,
  `added_by` varchar(100) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_idx` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- -------------------------------------
-- KeyPair
-- --------------------------------------
CREATE TABLE `key_pair` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keypair_name` varchar(45) DEFAULT NULL,
  `key` varchar(2000) DEFAULT NULL,
  `aws_account_id` int(11) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- -------------------------------------
-- permissions
-- --------------------------------------
CREATE TABLE `permissions` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- -------------------------------------
-- plugin_role  << no need for this
-- --------------------------------------

CREATE TABLE `plugin_role` (
  `p_role_id` int(11) NOT NULL,
  `p_role_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`p_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- -------------------------------------
-- resource  << no need for this as if now
-- --------------------------------------
CREATE TABLE `resource` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- -------------------------------------
-- team
-- --------------------------------------
CREATE TABLE `team` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_name` varchar(45) DEFAULT NULL,
  `manager_uid` int(11) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- -------------------------------------
-- user  
-- --------------------------------------
CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(45) DEFAULT NULL,
  `password` varchar(500) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `user_type` varchar(500) DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL,
  `created_on` date DEFAULT NULL,
  `google_uid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `team_id_idx` (`team_id`),
  CONSTRAINT `team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- -------------------------------------
-- user_role  
-- --------------------------------------
CREATE TABLE `user_role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- -------------------------------------
-- team - user Role
-- --------------------------------------
CREATE TABLE `team_user_role` (
  `id` int(11) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `role_id_idx` (`role_id`),
  KEY `uid_idx` (`uid`),
  CONSTRAINT `role_id` FOREIGN KEY (`role_id`) REFERENCES `plugin_role` (`p_role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- -------------------------------------
-- ssh_keys
-- --------------------------------------
CREATE TABLE `ssh_keys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `logical_name` varchar(45) DEFAULT NULL,
  `ssh_key` varchar(2000) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `uid_idx` (`uid`),
  CONSTRAINT `id` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- -------------------------------------
-- ssh_access_Details
-- --------------------------------------
CREATE TABLE `ssh_access_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `aws_account_id` int(11) DEFAULT NULL,
  `instance_id` varchar(45) DEFAULT NULL,
  `instance_ip` varchar(100) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `given_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
-- -------------------------------------
-- role_permission_map  << no need for this
-- --------------------------------------
CREATE TABLE `role_permission_map` (
  `id` int(11) NOT NULL,
  `resource_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `resource_id_idx` (`resource_id`),
  KEY `role_id_idx` (`role_id`),
  KEY `permission_id_idx` (`permission_id`),
  CONSTRAINT `permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `resource_id` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `role_idx` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

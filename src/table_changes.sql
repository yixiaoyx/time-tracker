ALTER TABLE Tasks ADD estimated_time_string varchar(8) null;
ALTER TABLE Tasks ADD estimated_time mediumtext null;
ALTER TABLE Tasks ADD completed_goal tinyint(1) default '0' null;
ALTER TABLE Tasks ADD due_date datetime null;

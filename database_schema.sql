DROP TABLE IF EXISTS Category;
CREATE TABLE Category (
ID int NOT NULL AUTO_INCREMENT,
category_name VARCHAR(50) not NULL,
lft int not NULL,
rght int not NULL,
subCategory int not NULL,
parent_category VARCHAR (50) not NULL,
PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS Tasks;
CREATE TABLE Tasks (
ID int NOT NULL AUTO_INCREMENT,
task_name VARCHAR(50) not NULL,
category_ID INT,
duration_string VARCHAR(8) not NULL,
duration LONG not NULL,
date_of_task_start datetime NULL,
date_of_task_finish datetime NULL,

PRIMARY KEY (ID),
FOREIGN KEY (category_ID) REFERENCES Category(ID)
);


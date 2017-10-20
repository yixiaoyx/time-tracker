DROP TABLE IF EXISTS Category;
Create table Category
(
  ID int auto_increment
    primary key,
  category_name varchar(50) not null,
  lft int null,
  rght int null,
  subCategory tinyint(1) default '1' null,
  parent_category varchar(50) null
)
;
DROP TABLE IF EXISTS Tasks;
create table Tasks
(
  ID int auto_increment
    primary key,
  task_name varchar(50) not null,
  category_ID int null,
  duration_string varchar(8) null,
  duration mediumtext not null,
  foreign key (category_ID) references trackerdb.category (ID)
)
;

create index category_ID
  on Tasks (category_ID)
;
DROP TABLE IF EXISTS task_durations;
create table task_durations
(
  ID int auto_increment,
  task_id int not null,
  task_name varchar(50) not null,
  task_duration mediumtext null,
  task_duration_string varchar(10) null,
  date_task_start datetime null,
  date_task_finish datetime null,
  constraint task_durations_ID_uindex
  unique (ID)
)
;


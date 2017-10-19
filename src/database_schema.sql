DROP table if EXISTS Category;
create table Category
(
  ID int auto_increment
    primary key,
  category_name varchar(50) not null,
  lft int null,
  rght int null,
  subCategory tinyint(1) default '1' null,
  parent_category varchar(50) null
);
drop table if exists Tasks;
create table Tasks (
  ID int auto_increment primary key,
  task_name varchar(50) not null,
  category_ID int null,
  duration_string varchar(8) null,
  duration mediumtext not null,
  estimated_time_string varchar(8) null,
  estimated_time mediumtext null,
  completed_goal tinyint(1) default '0' null,
  due_date datetime null,
  foreign key (category_ID) references Category (ID)
);
DROP table if EXISTS task_durations;
create table task_durations
(
  ID int auto_increment PRIMARY KEY,
  task_id int not null,
  task_name varchar(50) not null,
  task_duration mediumtext null,
  task_duration_string varchar(10) null,
  date_task_start datetime null,
  date_task_finish datetime null,

  foreign key (task_id) references Tasks (ID)

);


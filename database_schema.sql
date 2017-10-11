create table Category
(
	ID int auto_increment primary key,
	category_name varchar(50) not null,
	lft int null,
	rght int null,
	subCategory tinyint(1) default '1' null,
	parent_category varchar(50) null
);

create table Tasks
(
	ID int auto_increment
		primary key,
	task_name varchar(50) not null,
	category_ID int null,
	duration_string varchar(8) null,
	duration mediumtext not null,
	date_of_task_start datetime null,
	date_of_task_finish datetime null,
	constraint tasks_ibfk_1
	oreign key (category_ID) references trackerdb.Category (ID)
)
;

create index category_ID
	on Tasks (category_ID);


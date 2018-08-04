#!/bin/bash

read -p "Enter your MySQL root username (default is 'root'): " sqlname
if [ "$sqlname" = "" ]
then
    sqlname="root"
fi
read -s -p "Enter your MySQL root password: " sqlpsw
echo

mysql -u $sqlname -p"$sqlpsw" <src/trackattack.sql
mysql -u $sqlname -p"$sqlpsw" TrackerDB <src/database_schema.sql
mysql -u $sqlname -p"$sqlpsw" TrackerDB <src/TrackerDB_Category.sql
mysql -u $sqlname -p"$sqlpsw" TrackerDB <src/TrackerDB_Tasks.sql

set /p sqlname="Enter your MySQL root username: "
set /p sqlpsw="Enter your MySQL root password: "
echo.

mysql -u %sqlname% -p%sqlpsw% <src\trackattack.sql
mysql -u %sqlname% -p%sqlpsw% TrackerDB <src\database_schema.sql
mysql -u %sqlname% -p%sqlpsw% TrackerDB <src\TrackerDB_Category.sql
mysql -u %sqlname% -p%sqlpsw% TrackerDB <src\TrackerDB_Tasks.sql

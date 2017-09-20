package model;

import java.util.*;
import java.sql.*;
/**
 download the mysql-connector zip file (i've placed it in the src folder) + download mysql if you don't have it already  https://dev.mysql.com/downloads/mysql/
 run with with jar file -> java -cp .:mysql-connector-java-5.1.44-bin.jar DatabaseDriver on the commandline
 OR if you're using an IDE add the jar file to the classpath 
 **/
public class DatabaseDriver {

    private Connection c;
    private Statement s;
    String db;
    String user;
    String pw;

    public DatabaseDriver() {
        db = "jdbc:mysql://localhost/";
        user = "root"; //default user
        pw = "4920"; // to set password in mysql, run msql then run the query: SET PASSWORD FOR 'root'@'localhost' = PASSWORD('4920');

        try {
            Class.forName("com.mysql.jdbc.Controller");
            System.out.println("Connecting..");
            c = DriverManager.getConnection(db, user,pw);
            System.out.println("connected");
        }
        catch (Exception e) {
            System.out.println("Couldnt connect to database");
        }

    }
    //create a database if it doesn't exist.
    public void createDB () {

        try {
            s = c.createStatement();
            String createDB = "CREATE DATABASE IF NOT EXISTS trackerDB";
            s.executeUpdate(createDB);
            System.out.println("Created database ");
            c.close();
            s.close();

            //reconnect with database created - port 3306 is default for mysql
            db = "jdbc:mysql://localhost:3306/trackerDB?autoReconnect=true&useSSL=false";
            c = DriverManager.getConnection(db, user,pw);
            s = c.createStatement();

            System.out.println("reconnected ");
            String createTask = "CREATE TABLE IF NOT EXISTS Tasks"+
                    "(taskName VARCHAR(15) not NULL," +
                    "primary key (taskName));";
            s.executeUpdate(createTask);
            System.out.println("Created task table ");
            String createCategory = "CREATE TABLE IF NOT EXISTS Category"+
                    "(categoryName VARCHAR(15) not NULL,"
                    +" primary key (categoryName));";
            s.executeUpdate(createCategory);
            System.out.println("Created category table ");

        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    //check if database exists
    public boolean databaseExists () {
        try {
            ResultSet resultSet = c.getMetaData().getCatalogs();

            while (resultSet.next()) {
                if(resultSet.getString(1).equals("trackerDB")) {
                    return true;
                }
            }
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public void saveTasks(String task) {
        try {
            //create DB if it doesn't exist
            if(databaseExists()== false) {
                createDB();
            }
            else {
                c.close();
                db = "jdbc:mysql://localhost:3306/trackerDB?autoReconnect=true&useSSL=false";
                c = DriverManager.getConnection(db, user,pw);
                s = c.createStatement();

            }
            String sql = "INSERT INTO Tasks VALUE ('"+task+"')";
            ResultSet record = s.executeQuery("select * from Tasks");
            boolean check = false;
            //check if record exists
            while(record.next()) {
                if(record.getString("taskName").equals(task)) {

                    check = true;
                    break;
                }
            }
            if(check == false) {
                s.executeUpdate(sql);
                System.out.println("inserted new task");
            }



        } catch(SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    public List<Task> restoreTasks() {
        List<Task> tasks = new ArrayList<Task>();
        try {
            //grab all the tasks from table
            ResultSet results2 = s.executeQuery("select * from Tasks");
            System.out.println("All tasks stored: ");
            //print saved tasks
            while (results2.next()) {
                Task t = new Task(results2.getString("taskName"));
                tasks.add(t);
                System.out.println(results2.getString("taskName"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return tasks;
    }

    //testing database
    public static void main (String [] args) throws Exception{
        Connection c = null;
        Statement s = null;

        //add tasks
        DatabaseDriver db = new DatabaseDriver();
        db.saveTasks("Assignment 1");
        db.saveTasks("Assignment 2");
        db.saveTasks("Assignment 3");
        //get saved tasks
        List<Task> getTasks = db.restoreTasks();
    }

}

package model;

import java.util.*;
import java.sql.*;
/**
 download the mysql-connector zip file (i've placed it in the src folder) + download mysql if you don't have it already  https://dev.mysql.com/downloads/mysql/
 run with with jar file -> java -cp .:mysql-connector-java-5.1.44-bin.jar DatabaseDriver on the commandline
 or add the jar file to the classpath in your IDE
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
            String createCategory = "CREATE TABLE IF NOT EXISTS Category"+
                    "(ID int NOT NULL AUTO_INCREMENT, "
                    +"category_name VARCHAR(15) not NULL,"
                    +"primary key (ID));";
            s.executeUpdate(createCategory);
            System.out.println("Created category table ");
            String createTask = "CREATE TABLE IF NOT EXISTS Tasks"+
                    "(ID int NOT NULL AUTO_INCREMENT," +
                    "task_name VARCHAR(15) not NULL," +
                    "category_ID INT references category(ID)," +
                    "primary key (ID));";
            s.executeUpdate(createTask);
            System.out.println("Created task table ");


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

    public void saveTasks(String task, String category) {
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
            int categoryID = getCategoryID(category);
            String sql = "INSERT INTO Tasks VALUES (NULL, '"+task+"', '"+categoryID+"')";
            ResultSet record = s.executeQuery("select * from Tasks");
            boolean check = false;
            //check if record exists

            while(record.next()) {
                if(record.getString("task_name").equals(task)) {

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
                Task t = new Task(results2.getString("task_name"));
                tasks.add(t);
                System.out.println(results2.getString("task_name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return tasks;
    }
    //given a categoryName, return the categoryID
    public int getCategoryID(String category) {
        int ID = 0;
        try {
            s = c.createStatement();
            String getID = "SELECT ID FROM Category WHERE category_name = '"+category+"'";
            ResultSet r = s.executeQuery(getID);
            while(r.next()) {

               ID = Integer.parseInt(r.getString("ID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return ID;
    }

    public void saveCategory(String category) {
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
            String sql = "INSERT INTO Category VALUES (NULL,'"+category+"')";
            ResultSet record = s.executeQuery("select  * from Category");

            boolean check = false;
            //check if record exists
            while(record.next()) {
                if(record.getString("category_name").equals(category)) {

                    check = true;
                    break;
                }
            }
            if(check == false) {
                s.executeUpdate(sql);
                System.out.println("inserted new category");
            }



        } catch(SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    public List<Task> restoreCategories() {
        List<Task> tasks = new ArrayList<Task>();
        try {
            //grab all the tasks from table
            ResultSet results2 = s.executeQuery("select * from Category");
            System.out.println("All Categories stored: ");
            //print saved tasks
            while (results2.next()) {
                Task t = new Task(results2.getString("category_name"));
                System.out.println(results2.getString("category_name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return tasks;
    }
/*
    //testing database
    public static void main (String [] args) throws Exception{
        Connection c = null;
        Statement s = null;

        //add tasks
        DatabaseDriver db = new DatabaseDriver();
        db.saveCategory("Assignments");
        db.saveCategory("Projects");
        db.saveCategory("Homework");
        //get saved categories

        List<Task> getCategories = db.restoreCategories();
        //insert tasks and the category corresponding
        db.saveTasks("Assignment 1", "Assignments");
        db.saveTasks("Assignment 2", "Projects");
        //db.saveTasks("Assignment 3");
        //get saved tasks
        List<Task> getTasks = db.restoreTasks();


    }
    */
}

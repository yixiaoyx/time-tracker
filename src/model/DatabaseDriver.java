package model;

import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.Date;


public class DatabaseDriver {

    //Variables for connection to the database
    private Connection c;
    private Statement s;
    String db;
    String user;
    String pw;
    SimpleDateFormat sdf;

    public DatabaseDriver() {

        db = "jdbc:mysql://localhost:3306/TrackerDB?autoReconnect=true&useSSL=false";
        user = "root"; //default user
        pw = "4920"; // to set password in mysql, run msql then run the query: SET PASSWORD FOR 'root'@'localhost' = PASSWORD('4920');

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting..");
            c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
        } catch (Exception e) {
            System.out.println("Couldnt connect to database");
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    //saves a task to the task table
    public void saveTasks(String task, String category, String durationString, long duration, Date startTime, Date finishTime) {

        try {
            int categoryID = getCategoryID(category);
            String sql = "INSERT INTO Tasks VALUES (NULL, '" + task + "', '" + categoryID + "', '" + durationString + "', '" + duration + "', '" + startTime + "' + '" + finishTime + "')";

            ResultSet record = s.executeQuery("select * from Tasks");
            boolean check = false;
            //check if record exists

            while (record.next()) {
                if (record.getString("task_name").equals(task)) {

                    check = true;
                    break;
                }
            }
            if (check == false) {
                s.executeUpdate(sql);
                System.out.println("inserted new task");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    public List<Task> restoreTasks() {
        List<Task> tasks = new ArrayList<Task>();
        try {
            //grab all the tasks from table
            s = c.createStatement();
            ResultSet results = s.executeQuery("select * from Tasks");
            System.out.println("All tasks stored: ");
            //print saved tasks
            while (results.next()) {
                Task t = new Task(results.getString("task_name"));
                tasks.add(t);
                System.out.println(results.getString("task_name"));
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
            String getID = "SELECT ID FROM Category WHERE category_name = '" + category + "'";
            ResultSet r = s.executeQuery(getID);
            while (r.next()) {

                ID = Integer.parseInt(r.getString("ID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return ID;
    }


    //Add a new parent node category/sub category given a category name + the parent category above
    public void saveCategory(String category, String parent) {

        try {
            s = c.createStatement();
            String query1 = "SELECT @idCol := id from category ORDER BY id DESC LIMIT 1";
            String query2 = "SELECT @value := rght from category ORDER BY rght DESC LIMIT 1;";


            ResultSet record = s.executeQuery("select  * from Category");

            boolean check = false;
            //check if category exists already
            while (record.next()) {
                if (record.getString("category_name").equals(category)) {
                    check = true;
                    break;
                }
            }
            s = c.createStatement();
            ResultSet root = s.executeQuery(query2);
            s = c.createStatement();
            ResultSet id = s.executeQuery(query1);
            int rootVal = 0;
            int idCol = 0;
            if (root.next()) {
                rootVal = Integer.parseInt(root.getString("@value := rght"));
            }
            if (id.next()) {
                idCol = Integer.parseInt(id.getString("@idCol := id"));
            }
            String query3 = "UPDATE category SET rght = rght + 2 WHERE id = 1";
            String query4 = "INSERT INTO category(id, category_name, lft, rght, subCategory) VALUES('" + idCol + "'+1,'" + category + "','" + rootVal + "', '" + rootVal + "'+1, 0);";

            s = c.createStatement();
            s.addBatch(query3);
            s.addBatch(query4);


            if (check == false) {
                s.executeBatch();
                //  s.executeUpdate(query2);
                System.out.println("inserted new parent category");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    //save a sub category within a parent category
    public void saveSubCategory(String category, String parent) {
        try {
            s = c.createStatement();
            String query1 = "SELECT @idCol := id from category ORDER BY id DESC LIMIT 1;  ";
            String query2 = "SELECT @Left := lft FROM category WHERE category_name = '" + parent + "' ";


            ResultSet record = s.executeQuery("select  * from Category where subCategory = 1");
            s = c.createStatement();
            ResultSet id = s.executeQuery(query1);
            s = c.createStatement();
            ResultSet left = s.executeQuery(query2);
            int idCol = 0;
            int leftVal = 0;

            if (id.next()) {
                idCol = Integer.parseInt(id.getString("@idCol := id"));
            }
            if (left.next()) {
                leftVal = Integer.parseInt(left.getString("@Left := lft"));

            }
            System.out.println("id col = " + idCol + " left val = " + leftVal);


            boolean check = false;
            //check if sub category exists already
            while (record.next()) {
                if (record.getString("category_name").equals(category)) {
                    check = true; //sub category already exists
                    break;

                }
            }
            String query3 = "UPDATE category SET rght = rght + 2 WHERE rght > '" + leftVal + "';";
            String query4 = "UPDATE category SET lft = lft + 2 WHERE lft >  '" + leftVal + "';";
            String query5 = "INSERT INTO category(id, category_name, lft, rght, subCategory) VALUES( '" + idCol + "'+1,'" + category + "', '" + leftVal + "' + 1,  '" + leftVal + "' + 2, 1); ";


            s.addBatch(query3);
            s.addBatch(query4);
            s.addBatch(query5);

            if (check == false) {
                s.executeBatch();
                System.out.println("inserted new category");
            }


        } catch (SQLException e) {
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


    //get the full path of parent categories and sub categories of the current task.
    public List<String> restoreCategoryPath(String taskname) {
        List<String> path = new ArrayList<String>();
        try {
            //get the path of categories of the current task
            String getCategoryID = "SELECT category_ID from Tasks where task_name = '" + taskname + "'";
            ResultSet results = s.executeQuery(getCategoryID);
            int categoryID = 0;
            if (results.next()) {
                categoryID = Integer.parseInt(results.getString("category_ID"));
            }

            String query = "SELECT parent.category_name FROM category AS node, category AS parent where node.lft BETWEEN parent.lft AND parent.rght AND node.id = '" + categoryID + "' ORDER by node.lft;";
            ResultSet results2 = s.executeQuery(query);
           // System.out.println("Path : ");
            //print saved tasks
            while (results2.next()) {
                path.add(results2.getString("category_name"));
                System.out.println(results2.getString("category_name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return path;
    }


    public void changeCategory(String newCategoryName, String oldCategoryName) {

        try {
            s = c.createStatement();
            String query = "UPDATE Category SET category_name ='" + newCategoryName + "' WHERE category_name = '" + oldCategoryName + "' ";
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void changeTaskname(String newTaskName, String oldTaskName) {
        try {
            s = c.createStatement();
            String query = "UPDATE Tasks SET task_name ='" + newTaskName + "' WHERE task_name ='" + oldTaskName + "' ";
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //deletes a category and its sub categories + tasks within
    public void deleteCategory(String categoryname) {
        try {
            s = c.createStatement();
            String query1 = "SELECT @left := lft, @right := rght, @width := rght - lft + 1 FROM category WHERE category_name = '" + categoryname + "'";


            ResultSet getVals = s.executeQuery(query1);

            int leftVal = 0, rightVal = 0, widthVal = 0;
            if (getVals.next()) {
                leftVal = Integer.parseInt(getVals.getString("@left := lft"));
                rightVal = Integer.parseInt(getVals.getString("@right := rght"));
                widthVal = Integer.parseInt(getVals.getString("@width := rght - lft + 1"));

            }

            String query3 = "DELETE Tasks FROM Tasks join Category on (Tasks.category_id = Category.id) WHERE lft BETWEEN '" + leftVal + "' AND  '" + rightVal + "'";
            String query4 = "DELETE FROM Category WHERE lft BETWEEN '" + leftVal + "' AND '" + rightVal + "'";
            String query5 = "UPDATE Category SET rght = rght - '" + widthVal + "' WHERE rght >  '" + rightVal + "'";

            s.addBatch(query3);
            s.addBatch(query4);
            s.addBatch(query5);

            s.executeBatch();


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public void deleteTask(String taskname) {
        try {
            s = c.createStatement();
            String query = "DELETE FROM Tasks WHERE category_name ='" + taskname + "'";
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public String getTask (String taskname) {
        String duration = "";
        try {
            s = c.createStatement();
            String query = "SELECT * from Tasks where task_name = '"+taskname+"'";
            ResultSet r = s.executeQuery(query);


            while(r.next()) {
                duration = r.getString("duration_string");
            }
        }
        catch (Exception e) {
            e.getMessage();
        }


        return duration;
    }
    //gets all tasks within a given category name
    public List<Task> getTasksFromCategory (String category) {
        List<Task> tasks = new ArrayList<Task>();

        try {
            s = c.createStatement();
            String query1 = "SELECT ID from Category where category_name = '"+category+"' ";
            ResultSet getCategoryID = s.executeQuery(query1);

            int cat_id = 0;
            if(getCategoryID.next()) {

                cat_id = Integer.parseInt(getCategoryID.getString("ID"));

            }

            String query2 = "SELECT * from Tasks where category_ID = '"+cat_id+"'";
            s = c.createStatement();
            ResultSet r = s.executeQuery(query2);



            while(r.next()) {
                //store name, duration, start + finish times of all tasks within category
                Task newTask = new Task(r.getString("task_name"));
                newTask.setDurationString(     r.getString("duration_string"));
                Date start = sdf.parse(r.getString("date_of_task_start"));
                newTask.setStartTime(start);
                Date finish = sdf.parse(r.getString("date_of_task_finish"));
                newTask.setStartTime(finish);

              //  System.out.println("added task => " + newTask.getName());
                tasks.add(newTask);
            }
        }
        catch (Exception e) {
            e.getMessage();
        }

        return tasks;

    }

    /*  get tasks within periodA and period B
        Precondition: PeriodA comes before periodB
    */
    public List<Task> getTasksInPeriod(Date periodA, Date periodB) {
        List<Task> tasks = new ArrayList<Task>();
        try {
            s = c.createStatement();
            sdf.format(periodA);
            sdf.format(periodB);
            String query1 = "SELECT * from Tasks where date_of_task_start >= '"+ sdf.format(periodA)+"' AND date_of_task_finish <= '"+ sdf.format(periodB)+"' ";
            ResultSet getTasks = s.executeQuery(query1);

            while(getTasks.next()) {
                //store name, duration, start + finish times of all tasks within category
                Task newTask = new Task(getTasks.getString("task_name"));
                newTask.setDurationString(getTasks.getString("duration_string"));
                Date start = sdf.parse(getTasks.getString("date_of_task_start"));
                newTask.setStartTime(start);
                Date finish = sdf.parse(getTasks.getString("date_of_task_finish"));
                newTask.setStartTime(finish);
                tasks.add(newTask);

            }
        }
        catch (Exception e) {
            e.getMessage();
        }

        return tasks;

    }


    public static void main(String[] args) throws Exception {

        //add tasks
        DatabaseDriver db = new DatabaseDriver();
      /*  db.saveCategory("Assignments", "main");
        db.saveCategory("Projects", "main");
        db.saveCategory("Homework", "main");
        //get saved categories

        List<Task> getCategories = db.restoreCategories();
        //insert tasks and the category corresponding
        //insert tasks and the category corresponding
        db.saveTasks("Assignment 1", "Assignments", "00:00:2", 2);
        db.saveTasks("Assignment 2", "Projects", "00:00:9", 2);

        //db.saveTasks("Assignment 3");
        //get saved tasks
        */

        List<Task> getTasks = db.restoreTasks();
        List<String> getPath = db.restoreCategoryPath("Question3");
      //  db.saveSubCategory("COMP1917_W9", "Tutorial");
     //   db.saveCategory("Labs", "All");
        db.deleteCategory("Labs");

        //add a sub category
        /*
        db.saveSubCategory("COMP1917_W7", "Tutorial");
        //add a parent category
        db.saveCategory("Seminars ", "All");
        db.saveSubCategory("COMP1917_W8", "Tutorial");
        db.saveSubCategory("COMP1917_W8Lab", "COMP1917_W8");
        */


    }
}

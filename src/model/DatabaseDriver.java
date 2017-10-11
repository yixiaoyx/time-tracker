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

            s = c.createStatement();
            ResultSet record = s.executeQuery("select * from Tasks");
            boolean check = false;
            //check if record exists


            while (record.next()) {
                if (record.getString("task_name").equals(task)) {
                    System.out.println("Task already exists in the database");
                    check = true;
                    break;
                }
            }
            int categoryID = getCategoryID(category);
            // System.out.println("categ id = " + categoryID);
            String sql = "INSERT INTO Tasks VALUES (NULL, '"+task +"', '"+categoryID+"', '"+durationString+"', '" + duration + "', " + startTime + ", " + finishTime + ")";

            System.out.println("Task does not exist");
            if (check == false) {
                s = c.createStatement();
                s.executeUpdate(sql);
                System.out.println("inserted new task into the database");
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
                int cat_id = Integer.parseInt(results.getString("category_ID"));
                Category c = new Category(getCategoryName(cat_id));
                t.setParentCategory(c);
                tasks.add(t);
                System.out.println("added task: " + t.getName() + " and set its parent category to = " + t.getParentCategory().getName());

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return tasks;
    }

    public void updateTask(String task, String category, String durationString, long duration, Date startTime, Date finishTime) {

        try {
            s = c.createStatement();
            ResultSet record = s.executeQuery("select * from Tasks");
            boolean check = false;

            //check if task exists in the database

            while (record.next()) {
                if (record.getString("task_name").equals(task)) {
                    System.out.println("Task exists");
                    check = true;
                    break;
                }
            }
            if(check == false) {
                System.out.println("Could not update task - task does not exist in the DB");
                return;
            }
            int categoryID = getCategoryID(category);
            s = c.createStatement();

            String sql = "UPDATE Tasks SET duration_string = '" + durationString + "', duration = '" + duration + "', date_of_task_start = '" +    sdf.format(startTime) + "', date_of_task_finish = '" +    sdf.format(finishTime) +"'" +
                    " WHERE task_name = '"+task+"' AND category_id = '"+categoryID+"'";

                s.executeUpdate(sql);
                System.out.println("updated task new task into the database");



        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    //given a categoryID return the categoryName
    public String getCategoryName(int category) {
      String categoryName = "";
        try {
            s = c.createStatement();
            String getID = "SELECT category_name FROM Category WHERE ID = '" + category + "'";
            ResultSet r = s.executeQuery(getID);
            if (r.next()) {

                categoryName = r.getString("category_name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return categoryName;
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
                    System.out.println("Category already exists in the database");
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
            String query4 = "INSERT INTO category(id, category_name, lft, rght, subCategory, parent_category) VALUES('" + idCol + "'+1,'" + category + "','" + rootVal + "', '" + rootVal + "'+1, 0, '"+parent+"');";

            s = c.createStatement();
            s.addBatch(query3);
            s.addBatch(query4);


            if (check == false) {
                s.executeBatch();
                //  s.executeUpdate(query2);
                System.out.println("inserted new parent category into the new database");
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
                    System.out.println("Category already exists in the database");
                    check = true; //sub category already exists
                    break;

                }
            }
            String query3 = "UPDATE category SET rght = rght + 2 WHERE rght > '" + leftVal + "';";
            String query4 = "UPDATE category SET lft = lft + 2 WHERE lft >  '" + leftVal + "';";
            String query5 = "INSERT INTO category(id, category_name, lft, rght, subCategory, parent_category) VALUES( '" + idCol + "'+1,'" + category + "', '" + leftVal + "' + 1,  '" + leftVal + "' + 2, 1, '"+parent+"')";


            s.addBatch(query3);
            s.addBatch(query4);
            s.addBatch(query5);

            if (check == false) {
                s.executeBatch();
                System.out.println("inserted new category into the database");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    public List<Category> restoreCategories() {
        List<Category> Categories = new ArrayList<Category>();
        try {
            //grab all the tasks from table
            s = c.createStatement();
            ResultSet results = s.executeQuery("select * from Category");
            System.out.println("All Categories stored: ");
            //print saved tasks
            while (results.next()) {
                if (!results.getString("category_name").equals("All")) {
                    Category c = new Category(results.getString("category_name"));
                    Category parent = new Category(results.getString("parent_category"));
                    c.setParentCategory(parent);
                    Categories.add(c);
                    System.out.println("added category: " + c.getName() + " and set its parent category to = " + c.getParentCategory().getName());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return Categories;
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
                Date finish = sdf.parse(r.getString("date_of_task_finish"));
                Duration d = new Duration(start, finish);
                newTask.setDuration(d);
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
                Date finish = sdf.parse(getTasks.getString("date_of_task_finish"));
                Duration d = new Duration(start, finish);
                newTask.setDuration(d);
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = sdf.parse("2017-08-10 12:02:05");
        Date finish = sdf.parse("2017-08-10 12:32:05");
/*


        db.saveTasks("COMP1917_Proj1", "Projects", "00:30:00", 1000,
                start, finish);
        db.saveCategory("Assignments", "main");

        //restore all tasks saved on database;
        List<Task> getTasks = db.restoreTasks();

        //restore category path of a task
        List<String> getPath = db.restoreCategoryPath("Question1-3");

        db.saveSubCategory("COMP1917_W10", "Tutorial");
*/
    }
}

package model;

import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.Date;


public class DatabaseDriver {

    //Variables for connection to the database
    //  private Connection c;
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

        } catch (Exception e) {
            e.getMessage();
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    //saves a task to the task table
    public void saveTasks(String task, String Category, String durationString, long duration){


        System.out.println("saveTask category: " + Category);

        try {
            Connection  c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
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
            int CategoryID = getCategoryID(Category);

            System.out.println("CategoryID is " + CategoryID);

            String query1 = "INSERT INTO Tasks (task_name,category_ID, duration_string, duration, estimated_time_string, estimated_time) VALUES ('"+task +"', '"+CategoryID+"', '"+durationString+"', '" + duration + "', null, 0)";


            System.out.println("Task does not exist");
            if (check == false) {
                s = c.createStatement();
                s.executeUpdate(query1);
                System.out.println("inserted new task into the database");
            }

            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    public void updateEstimatedTime(String estimatedTimeString, long estimatedTime, String taskName) {

        try {
            Connection  c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();

            String query2 = "UPDATE Tasks SET estimated_time_string = '"+estimatedTimeString+"', estimated_time = '"+estimatedTime+"' WHERE task_name = '"+taskName+"' ";
            s.executeUpdate(query2);
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public void updateDueDate(Date dueDate, String taskName) {

        try {
            Connection  c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();

            String query2 = "UPDATE Tasks SET due_date = '"+sdf.format(dueDate)+" WHERE task_name = '"+taskName+"' ";
            s.executeUpdate(query2);
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public void updateGoal (Boolean goal, String taskName) {

        try {
            Connection  c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            int goalVal = 0;
            if(goal == true) {
                goalVal = 1;
            }
            String query2 = "UPDATE Tasks SET completed_goal = '"+goalVal+" WHERE task_name = '"+taskName+"' ";
            s.executeUpdate(query2);
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    //Add a task duration for an existing task
    public void addTaskDuration(String task, long duration, String durationString, Date startDate, Date finishDate) {
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            ResultSet record = s.executeQuery("select * from Tasks");
            boolean check = false;


            //check if task exists
            while (record.next()) {
                if (record.getString("task_name").equals(task)) {
                    check = true;
                    break;
                }
            }

            if (check == true) {
                int task_id = getTaskID(task);
                s = c.createStatement();
                System.out.println("start = "+ sdf.format(startDate) + " id = " + task_id);
                String query =  "INSERT INTO task_durations VALUES (NULL,'"+task_id+"','"+task+"', '"+duration+"','"+durationString+"', '" +sdf.format(startDate)+"','" +sdf.format(finishDate)+"')";
                s.executeUpdate(query);
                System.out.println("inserted duration into task_durtions table");
            }

            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }

    public List<Task> restoreTasks() {
        List<Task> tasks = new ArrayList<Task>();
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            //grab all the tasks from table
            s = c.createStatement();

            ResultSet results = s.executeQuery("select * from Tasks");
            System.out.println("All tasks stored: ");
            //print saved tasks
            while (results.next()) {
                Task t = new Task(results.getString("task_name"));

                System.out.println("1");

                int cat_id = Integer.parseInt(results.getString("category_ID"));
                System.out.println("1.1");

                Category cat = new Category(getCategoryName(cat_id));
                System.out.println("1.2");

                t.setParentCategory(cat);
                System.out.println("1.3");

                String estTime = results.getString("estimated_time");
                if(estTime == null) {
                    System.out.println("estTime was null");
                }
                else {
                    t.setEstimatedTime(Long.parseLong(estTime));

                }

                System.out.println("2");


                // long total = Long.parseLong(results.getString("duration"));
                //   t.setTotalTime(total);
                t.setEstimatedTimeString(results.getString("estimated_time_string"));
                int goal = Integer.parseInt(results.getString("completed_goal"));

                System.out.println("3");


                if(goal == 0) {
                    t.setGoalComplete(false);
                }
                else {
                    t.setGoalComplete(true);
                }


                System.out.println("4");


                tasks.add(t);
                System.out.println("added task: " + t.getName() + " and set its parent Category to = " + t.getParentCategory().getName());


            }
            c.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return tasks;
    }

    public void updateTask(String task, String Category, String durationString, long duration, String estimated_time_string, long estimated_time, boolean completed_goal, Date dueDate) {

        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
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
            int CategoryID = getCategoryID(Category);
            s = c.createStatement();
            int completed = 0;
            if(completed_goal == true) {
                completed = 1;

            }
            System.out.println("duration = " + durationString);
            String sql = "UPDATE Tasks SET duration_string = '" + durationString +"', duration = '" + duration +"', Category_ID = '"+CategoryID+"', estimated_time_string = '"+estimated_time_string+"', estimated_time = '"+estimated_time+"', completed_goal = '"+completed+"' WHERE task_name = '"+task+"'";
            // AND due_date = '\"+dueDate+\"'\";
            //  System.out.println("duration string = " + durationString);
            s.executeUpdate(sql);
            System.out.println("updated new task into the database");

            c.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }



    }

    //given a CategoryID return the CategoryName
    public String getCategoryName(int Category) {
        String CategoryName = "";
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String getID = "SELECT Category_name FROM Category WHERE ID = '" + Category + "'";
            ResultSet r = s.executeQuery(getID);
            if (r.next()) {

                CategoryName = r.getString("Category_name");
            }
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return CategoryName;
    }
    //given a CategoryName, return the CategoryID
    public int getCategoryID(String Category) {
        int ID = 0;
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String getID = "SELECT ID FROM Category WHERE Category_name = '" + Category + "'";
            ResultSet r = s.executeQuery(getID);
            while (r.next()) {

                ID = Integer.parseInt(r.getString("ID"));
            }

            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return ID;
    }


    //Add a new parent node Category/sub Category given a Category name + the parent Category above
    public void saveCategory(String Category, String parent) {

        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query1 = "SELECT @idCol := id from Category ORDER BY id DESC LIMIT 1";
            String query2 = "SELECT @value := rght from Category ORDER BY rght DESC LIMIT 1;";


            ResultSet record = s.executeQuery("select  * from Category");

            boolean check = false;
            //check if Category exists already
            while (record.next()) {
                if (record.getString("Category_name").equals(Category)) {
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
            String query3 = "UPDATE Category SET rght = rght + 2 WHERE id = 1";
            String query4 = "INSERT INTO Category(id, Category_name, lft, rght, subCategory, parent_Category) VALUES('" + idCol + "'+1,'" + Category + "','" + rootVal + "', '" + rootVal + "'+1, 0, '" + parent + "');";

            s = c.createStatement();
            s.addBatch(query3);
            s.addBatch(query4);


            if (check == false) {
                s.executeBatch();
                //  s.executeUpdate(query2);
                System.out.println("inserted new parent Category into the new database");
            }

            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }

    //save a sub Category within a parent Category
    public void saveSubCategory(String Category, String parent) {
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query1 = "SELECT @idCol := id from Category ORDER BY id DESC LIMIT 1;  ";
            String query2 = "SELECT @Left := lft FROM Category WHERE Category_name = '" + parent + "' ";


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
            //check if sub Category exists already
            while (record.next()) {
                if (record.getString("Category_name").equals(Category)) {
                    System.out.println("Category already exists in the database");
                    check = true; //sub Category already exists
                    break;

                }
            }
            String query3 = "UPDATE Category SET rght = rght + 2 WHERE rght > '" + leftVal + "';";
            String query4 = "UPDATE Category SET lft = lft + 2 WHERE lft >  '" + leftVal + "';";
            String query5 = "INSERT INTO Category(id, Category_name, lft, rght, subCategory, parent_Category) VALUES( '" + idCol + "'+1,'" + Category + "', '" + leftVal + "' + 1,  '" + leftVal + "' + 2, 1, '"+parent+"')";


            s.addBatch(query3);
            s.addBatch(query4);
            s.addBatch(query5);

            if (check == false) {
                s.executeBatch();
                System.out.println("inserted new Category into the database");
            }


            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }

    public List<Category> restoreCategories() {
        List<Category> Categories = new ArrayList<Category>();
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            //grab all the tasks from table
            s = c.createStatement();
            ResultSet results = s.executeQuery("select * from Category");
            System.out.println("All Categories stored: ");
            //print saved tasks
            while (results.next()) {
                if (!results.getString("Category_name").equals("All")) {
                    Category cat = new Category(results.getString("Category_name"));
                    Category parent = new Category(results.getString("parent_Category"));
                    cat.setParentCategory(parent);
                    Categories.add(cat);
                    System.out.println("added Category: " + cat.getName() + " and set its parent Category to = " + cat.getParentCategory().getName());
                }
            }

            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return Categories;
    }


    //get the full path of parent categories and sub categories of the current task.
    public List<String> restoreCategoryPath(String taskname) {
        List<String> path = new ArrayList<String>();

        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            //get the path of categories of the current task
            String getCategoryID = "SELECT Category_ID from Tasks where task_name = '" + taskname + "'";
            ResultSet results = s.executeQuery(getCategoryID);
            int CategoryID = 0;
            if (results.next()) {
                CategoryID = Integer.parseInt(results.getString("Category_ID"));
            }

            String query = "SELECT parent.Category_name FROM Category AS node, Category AS parent where node.lft BETWEEN parent.lft AND parent.rght AND node.id = '" + CategoryID + "' ORDER by node.lft;";
            ResultSet results2 = s.executeQuery(query);
            // System.out.println("Path : ");
            //print saved tasks
            while (results2.next()) {
                path.add(results2.getString("Category_name"));
                System.out.println(results2.getString("Category_name"));
            }

            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return path;
    }


    public void changeCategory( String oldCategoryName, String newCategoryName) {

        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query = "UPDATE Category SET Category_name ='" + newCategoryName + "' WHERE Category_name = '" + oldCategoryName + "' ";
            s.executeUpdate(query);
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public void changeTaskname(String oldTaskName, String newTaskName) {
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query = "UPDATE Tasks SET task_name ='" + newTaskName + "' WHERE task_name ='" + oldTaskName + "' ";
            s.executeUpdate(query);
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //deletes a Category and its sub categories + tasks within
    public void deleteCategory(String Categoryname) {
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query1 = "SELECT @left := lft, @right := rght, @width := rght - lft + 1 FROM Category WHERE Category_name = '" + Categoryname + "'";


            ResultSet getVals = s.executeQuery(query1);

            int leftVal = 0, rightVal = 0, widthVal = 0;
            if (getVals.next()) {
                leftVal = Integer.parseInt(getVals.getString("@left := lft"));
                rightVal = Integer.parseInt(getVals.getString("@right := rght"));
                widthVal = Integer.parseInt(getVals.getString("@width := rght - lft + 1"));

            }
            String query3 = "DELETE task_durations FROM task_durations join Tasks on (task_durations.task_id = Tasks.id) join Category on (Tasks.Category_ID = Category.id) WHERE category_name = '"+Categoryname+"'";
            String query4 = "DELETE Tasks FROM Tasks join Category on (Tasks.Category_id = Category.id) WHERE lft BETWEEN '" + leftVal + "' AND  '" + rightVal + "'";
            String query5 = "DELETE FROM Category WHERE lft BETWEEN '" + leftVal + "' AND '" + rightVal + "'";
            String query6 = "UPDATE Category SET rght = rght - '" + widthVal + "' WHERE rght >  '" + rightVal + "'";

            s.addBatch(query3);
            s.addBatch(query4);
            s.addBatch(query5);
            s.addBatch(query6);

            s.executeBatch();

            c.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public void deleteTask(String taskname) {
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query1 = "DELETE FROM task_durations WHERE task_name = '"+taskname+"'";
            String query2 = "DELETE FROM Tasks WHERE task_name ='" + taskname + "'";
            s.executeUpdate(query1);
            s = c.createStatement();
            s.executeUpdate(query2);
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public String getTask (String taskname) {
        String duration = "";
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query = "SELECT * from Tasks where task_name = '"+taskname+"'";
            ResultSet r = s.executeQuery(query);


            while(r.next()) {
                duration = r.getString("duration_string");
            }
            c.close();
        }
        catch (Exception e) {
            e.getMessage();
        }


        return duration;
    }
    //gets all tasks within a given Category name
    public List<Task> getTasksFromCategory (String Category) {
        List<Task> tasks = new ArrayList<Task>();

        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query1 = "SELECT ID from Category where Category_name = '"+Category+"' ";
            ResultSet getCategoryID = s.executeQuery(query1);

            int cat_id = 0;
            if(getCategoryID.next()) {

                cat_id = Integer.parseInt(getCategoryID.getString("ID"));

            }

            String query2 = "SELECT * from Tasks where Category_ID = '"+cat_id+"'";
            s = c.createStatement();
            ResultSet r = s.executeQuery(query2);

            while(r.next()) {
                //store name, duration & duration string of all tasks within Category
                Task newTask = new Task(r.getString("task_name"));
                newTask.setDurationString(     r.getString("duration_string"));
                // Date start = sdf.parse(r.getString("date_of_task_start"));
                //  Date finish = sdf.parse(r.getString("date_of_task_finish"));
                // Duration d = new Duration(start, finish);
                long totalTime = Long.parseLong(r.getString("duration"));
                newTask.setTotalTime(totalTime);
                tasks.add(newTask);
            }
            c.close();
        }

        catch (Exception e) {
            e.getMessage();
        }

        return tasks;

    }

    //retrieve all indivdiual tasks durations for a task
    public List<Task> getAllTaskDurations(String taskname) {
        List<Task> tasks = new ArrayList<Task>();

        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query1 = "SELECT ID from Tasks where task_name = '"+taskname+"' ";
            ResultSet getID = s.executeQuery(query1);

            int task_id = 0;
            if(getID.next()) {
                task_id = Integer.parseInt(getID.getString("ID"));
            }

            String query2 = "SELECT * from task_durations where task_id = '"+task_id+"' ";
            s = c.createStatement();
            ResultSet r = s.executeQuery(query2);
            int i = 0;
            Task newTask= null;
            while(r.next()) {
                //store individual duration, duration string and the dates for the current task
                if(i == 0) {
                    newTask = new Task(r.getString("task_name"));
                    i = 1;
                }
                newTask.setDurationString(r.getString("task_duration_string"));
                Date start = sdf.parse(r.getString("date_task_start"));
                Date finish = sdf.parse(r.getString("date_task_finish"));
                Duration d = new Duration(start, finish, r.getString("task_name"));
                System.out.println("added timing to " + newTask.getName() + " = " + d.time());
                newTask.addTiming(d);

                tasks.add(newTask);

            }
            c.close();
        }
        catch (Exception e) {
            e.getMessage();
        }

        return tasks;

    }

    //retrieve all task durations within a Category
    public List<Task> getCategoryTaskDurations (String Category) {

        List<Task> tasks = new ArrayList<Task>();

        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query1 = "SELECT ID from Category where Category_name = '"+Category+"' ";
            ResultSet getID = s.executeQuery(query1);


            int cat_id = 0;
            if(getID.next()) {
                cat_id = Integer.parseInt(getID.getString("ID"));
            }
            s = c.createStatement();
            String query2 = "SELECT * from task_durations join Tasks on (Tasks.id = task_durations.task_id) join Category on (Category.id = Tasks.category_ID) WHERE Category.ID = '"+cat_id+"' ";
            ResultSet r = s.executeQuery(query2);
            int i = 0;
            while(r.next()) {
                //store individual duration, duration string and the dates for the current task
                Task newTask = new Task(r.getString("task_name"));
                newTask.setDurationString(r.getString("task_duration_string"));
                Date start = sdf.parse(r.getString("date_task_start"));
                Date finish = sdf.parse(r.getString("date_task_finish"));
                Duration d = new Duration(start, finish, r.getString("task_name"));
                long totalTime = Long.parseLong(r.getString("duration"));
                newTask.addTiming(d);
                tasks.add(newTask);


            }
            c.close();
        }

        catch (Exception e) {
            e.getMessage();
        }

        return tasks;

    }

    public List<Integer> getTaskID(int CategoryID) {
        int task_id = 0;
        List<Integer> taskIds = new ArrayList<Integer>();
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query2 = "SELECT ID from Tasks where Category_ID = '" + CategoryID + "' ";

            ResultSet r = s.executeQuery(query2);
            while(r.next()) {
                task_id = Integer.parseInt(r.getString("ID"));
                System.out.println("task id  = " + task_id);
                taskIds.add(task_id);
            }
            c.close();
        }

        catch (SQLException e) {
            e.getMessage();
        }
        return taskIds;
    }

    public int getTaskID (String task_name) {
        int task_id = 0;
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            String query2 = "SELECT ID from Tasks where task_name = '" + task_name + "' ";

            ResultSet r = s.executeQuery(query2);
            if(r.next()) {
                task_id = Integer.parseInt(r.getString("ID"));
            }
            c.close();
        }

        catch (SQLException e) {
            e.getMessage();
        }

        return task_id;
    }


    /*  get tasks within periodA and period B
        Precondition: PeriodA comes before periodB
    */
    public List<Task> getTasksInPeriod(Date periodA, Date periodB) {
        List<Task> tasks = new ArrayList<Task>();
        try {
            Connection c = DriverManager.getConnection(db, user, pw);
            System.out.println("connected");
            s = c.createStatement();
            sdf.format(periodA);
            sdf.format(periodB);
            String query1 = "SELECT * from Tasks where date_task_start >= '"+ sdf.format(periodA)+"' AND date_task_finish <= '"+ sdf.format(periodB)+"' ";
            ResultSet getTasks = s.executeQuery(query1);

            while(getTasks.next()) {
                //store name, duration, start + finish times of all tasks within Category
                Task newTask = new Task(getTasks.getString("task_name"));
                System.out.println("task in period " + newTask.getName());
                newTask.setDurationString(getTasks.getString("duration_string"));
                Date start = sdf.parse(getTasks.getString("date_task_start"));
                Date finish = sdf.parse(getTasks.getString("date_task_finish"));
                Duration d = new Duration(start, finish, getTasks.getString("task_name"));
                newTask.setDuration(d);
                tasks.add(newTask);

            }
            c.close();
        }
        catch (Exception e) {
            e.getMessage();
        }

        return tasks;

    }
    /*select Tasks.task_name, Tasks.duration from task_durations join Tasks on (task_durations.task_id = Tasks.id) WHERE date_task_start >= "2017-10-20 10:32:49" AND date_task_finish <= "2017-10-20 23:00:00" GROUP BY tasks.task_name, tasks.duration HAVING (COUNT(*) >= 1);*/


    public static void main(String[] args) throws Exception {
/*
        //add tasks
        DatabaseDriver db = new DatabaseDriver();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = sdf.parse("2017-08-10 12:02:05");
        Date finish = sdf.parse("2017-08-10 12:32:05");

      List<Task> taskDurations = db.getAllTaskDurations("task3");
        System.out.println("getting task durations from category3");
        for(Task t : taskDurations) {
            System.out.println(t.getDurationString());
        }
        System.out.println("task durations from category All");
        List<Task> taskDurationsCat = db.getCategoryTaskDurations("All");
        for(Task t : taskDurationsCat) {
            System.out.println(t.getDurationString());
        }
*/
    }
}

package model;

import java.text.SimpleDateFormat;
import java.util.*;
public class AnalysisFuncs {
    DatabaseDriver database;

    //As a user, I want to see how time I spend on a task compares to my estimate
    //As a user, I want to see a graph of how much I work on average each day, week or month (for all tasks)
    //As a user, I want to be able to use this app with minimal need to read instructions
    //As a user I want to know how long I’ve spent working on an a particular task in a specific time period to see if I am doing too much or too little

    public AnalysisFuncs () {
        //Connect to the database
        database = new DatabaseDriver();


    }

    //As a user, I want to see how much work I've done for a particular task
    public String workSpentOnTask (String task) {

        //returns the duration (string in format hours:minutes:seconds) spent on a task
        return database.getTask(task);

    }
    //get All tasks stored in the database
    public List<Task> workSpentOnAllTasks () {

        //returns the duration (string in format hours:minutes:seconds) spent on a task
        return database.restoreTasks();

    }



    //As a user, I want to see how much work I've done for a category
    public List<Task> workSpentOnCategory (String category) {

        List<Task> tasks = database.getTasksFromCategory(category); //grabs all tasks that belong in category

        return tasks;


    }



    //Pre conditions: Period A occurs before Period B
    public List<Task> workSpentInPeriod(Date periodA, Date periodB) {

        if(periodA.after(periodB) || periodB.before(periodA)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sdf.format(periodA);
        sdf.format(periodB);
        List<Task> tasksInPeriod = database.getTasksInPeriod(periodA, periodB);



        return tasksInPeriod;
    }
    //As a user, I want to see how much work I've done for overall over a specified time period

    //Testing analysis functions
    public static void main(String [] args)  throws Exception{
        AnalysisFuncs a = new AnalysisFuncs();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = a.workSpentOnTask("Question1-3");
        System.out.println("time spent on task = " +time);
        List<Task> allTasks  = a.workSpentOnAllTasks();



        List<Task> tasksInCat = a.workSpentOnCategory("Projects");
        System.out.println("All tasks in category 'Projects': ");
        for(Task t: tasksInCat) {
            System.out.println(t.getName());
        }


        Date pA = sdf.parse("2017-08-20 12:00:00");
        Date pB = sdf.parse("2017-08-20 13:00:00");
        List<Task> TasksInPeriod = a.workSpentInPeriod(pA, pB);
        System.out.println("All tasks in the time period beteween 2017-08-20 12:00:00 and 2017-08-20 13:00:00");

        for(Task t: TasksInPeriod) {
            System.out.println(t.getName());
        }







    }
}


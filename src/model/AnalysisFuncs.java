package model;

import java.text.SimpleDateFormat;
import java.util.*;
/*public class AnalysisFuncs {
    InterfaceDriver I = new InterfaceDriver();
    //As a user, I want to see how time I spend on a task compares to my estimate
    //As a user, I want to see a graph of how much I work on average each day, week or month (for all tasks)
    //As a user, I want to be able to use this app with minimal need to read instructions
    //As a user I want to know how long I’ve spent working on an a particular task in a specific time period to see if I am doing too much or too little

    public AnalysisFuncs () {




    }


    //As a user, I want to see how much work I've done for a category - in duration order
    // (most time spent on a task to the least time spent)
    public List<Task> workSpentOnCategory (String category) {

        List<Task> tasks = new ArrayList<Task>();
        Category c = I.getCategoryByName(category);
     //   List<Category> subcats = c.getSubCategory();



        Collections.sort(tasks, new Comparator<Task>() {
            public int compare (Task t1, Task t2){
                return Long.compare(t1.getDurationInSeconds(),t2.getDurationInSeconds());
            }
        });

        for(Task t : tasks) {
            System.out.println(t.getDurationInSeconds());
        }


        return tasks;


    }



    //Pre conditions: Period A occurs before Period B
    public void workSpentInPeriod(Date periodA, Date periodB) {


}
    //As a user, I want to see how much work I've done for overall over a specified time period

    //Testing analysis functions
    public static void main(String [] args)  throws Exception{

        InterfaceDriver I = new InterfaceDriver();
        I.addCategory("All");
        I.retrieveAllCategories();
        I.retrieveAllTasks();





        AnalysisFuncs a = new AnalysisFuncs();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

       // a.workSpentOnCategory("Projects");



        Date pA = sdf.parse("2017-08-20 12:00:00");
        Date pB = sdf.parse("2017-08-20 13:00:00");

       // System.out.println("All tasks in the time period beteween 2017-08-20 12:00:00 and 2017-08-20 13:00:00");






    }
}

**/
package model;

import java.util.*;
import java.text.SimpleDateFormat;

public class Task {
    private String name;

    // are we currently tracking time for this task?
    private Boolean active;

    // what time did we start the current active clock?
    private Date activeStartTime;

    // initial start time of the task
    private Date startTime;

    //what time did we clock out f?
    private Date activeEndTime;

    // list of start and end times
    private List<Duration> timings;

    //duration of task in seconds - used to store in the database & to measure with for the analysis later on
    private long durationSecs;

    //duration in format hours:minutes:seconds
    private String durationString;

    //duration object containing start + end of duration.
    private Duration duration;

    // initial total active time of task from database
    private String initialActiveTime;

    private long estimatedTime;

    private String estimatedTimeString;

    private Date dueDate;

    private boolean goalComplete;

    //To make updates to the database
    private DatabaseDriver db;

    private long totaltime;

    private Category parentCategory;

    private boolean dueDateApproaching;

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category p) {
        this.parentCategory = p;
    }

    public Task(String name) {
        this.name = name;
        active = false;
        timings = new ArrayList<Duration>();
        db = new DatabaseDriver();
     //   estimatedTime = 10*1000;
    }

    public void setEstimatedTime(long t) {
        estimatedTime = t;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }
    public String getEstimatedTimeString() {
        return estimatedTimeString;
    }

    public void setEstimatedTimeString(String estimatedTimeString) {
        this.estimatedTimeString = estimatedTimeString;
    }
    public void setGoalComplete(Boolean goalComplete) {
        this.goalComplete = goalComplete;
    }
    public boolean getGoalComplete() {
        return this.goalComplete;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Date getDueDate() {
        return this.dueDate;
    }

    public boolean isDueDateApproaching() {
        Date currDate = new Date();


        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //final Date date = sdf.parse( ); // conversion from String
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(dueDate);
        cal.add(GregorianCalendar.DAY_OF_MONTH, -7); // date manipulation

        System.out.println("due date  + 7 = " + sdf.format(cal.getTime())); // conversion to String
        System.out.println("curr date =" + currDate); // conversion to String



        Date d1 = cal.getTime();

        long diff = Math.abs(d1.getTime() - currDate.getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);
        System.out.println("diff days = "+   diffDays);

        if(diffDays <= 7) {
            System.out.println(7 - diffDays + " days remaining till task due!");
            return true; //due date approaching in less than 7 days!
        }


        return false;
    }




    public void clockIn() {
        // we can't clock in to an already active task
        if(active) {
            System.out.println("Tried to clock in active task " + name);
        }

        else {
            System.out.println("Clocked in task " + name);

            activeStartTime = new Date();

            if (startTime==null) {
                startTime = new Date();
            }

            System.out.println("is due date approaching ? " + isDueDateApproaching());
            System.out.println("Clock-in got start time: " + activeStartTime.toString());


            active = true;

        }
    }

    public void clockOut() {
        // we can't clock out of an inactive task
        if(!active) {
            System.out.println("Tried to clock out of an inactive task " + name);
        }
        else {
            System.out.println("Clocked out task " + name);
            System.out.println("estimated time = " + estimatedTime);


            activeEndTime = new Date();
            System.out.println("Clock-out got end time: " + activeEndTime.toString());

            addTiming(activeStartTime, activeEndTime);

            System.out.println("total time = " + totaltime);
            if(totaltime >= estimatedTime) {

                System.out.println("goal reached!" );
            }



            //save task to the database after clocking out.
            getTotalTime();
            db.updateTask(getName(), getParentCategory().getName(), getTotalTimeString(),
                    getTotalTime(),getEstimatedTimeString(),getEstimatedTime(), getGoalComplete(), getDueDate());// activeEndTime, activeEndTime);


            String getDuration = getLengthOfLastClockInOut();
            db.addTaskDuration(getName(),duration.time(), getDuration, activeStartTime, activeEndTime);

            // clear the active variables
            active = false;
            activeStartTime = null;

        }

    }

    public List<Duration> getTimings() {
        return timings;
    }
    public Duration getDuration() {
        return duration;
    }

    public void addTiming(Duration d) {
        timings.add(d);
    }

    public void addTiming(Date start, Date end) {
        duration = new Duration(start, end, name);
        timings.add(duration);
    }

    public Boolean getStatus() {
        return active;
    }

    public String getName() {

        return name;
    }
    public void setName(String newName) {
        this.name = newName;
    }

    // return dynamically calculated total runtime
    public String getActiveRunTimeString() {
        if (!active) {
            System.out.println("Tried to getActiveRunTimeString for non-active task " + name);
            return "00:00:00";
        } else {
            Date rightNow = new Date();

            // current run time in milliseconds
            // will break at daylight savings time crossover but who cares
            long milliSecondDelta = rightNow.getTime() - startTime.getTime();

            // now we want to convert this to hh:mm:ss

            String timeConverted = convertTime(milliSecondDelta);

            return timeConverted;
        }
    }

    public long getActiveRunTime() {
        if(active) {
            Date rightNow = new Date();
            return rightNow.getTime() - activeStartTime.getTime();
        }
        else {
            return 0;
        }
    }

    //get total duration of task
    public String getLengthOfLastClockInOut() {
        long difference = activeEndTime.getTime()-activeStartTime.getTime();
        durationSecs = difference/1000;
        String timeConverted = convertTime(difference);

        //activeStartTime = null;


        return timeConverted;
    }

    public long getTotalTime() {
        long totalTime = 0;
        for(Duration d : timings) {
            totalTime += d.time();
        }
        totaltime = totalTime;
        return totaltime;
    }
    public long getTotalTimeSecs() {
        getTotalTime();
        return totaltime / 1000;
    }

    public double getTotalTimeInMinutes() {

        for(Duration d : timings) {
            totaltime += d.time();
        }
        return totaltime;

    }

    public void setTotalTime (long totalTime) {
        this.totaltime = totalTime;
    }

    public String getTotalTimeString() {
        return convertTime(totaltime);
    }

    public long durationInSeconds() {
        return durationSecs;
    }


    public String convertTime(long milliSecondDelta) {
        // this snippet taken from https://stackoverflow.com/questions/43892644
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = milliSecondDelta / hoursInMilli;
        milliSecondDelta = milliSecondDelta % hoursInMilli;

        long elapsedMinutes = milliSecondDelta / minutesInMilli;
        milliSecondDelta = milliSecondDelta % minutesInMilli;

        long elapsedSeconds = milliSecondDelta / secondsInMilli;
        return String.format("%02d", elapsedHours) + ":" +
                String.format("%02d", elapsedMinutes) + ":" +
                String.format("%02d", elapsedSeconds);
    }


    public void setDurationString(String duration) {
        this.durationString = duration;
    }

    public String getDurationString() {
        return this.durationString;
    }
    public long getDurationInSeconds() {
        return durationSecs;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}

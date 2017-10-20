package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class Task {
    private String name;

    // are we currently tracking time for this task?
    private Boolean active;

    // what time did we start the current active clock?
    private Date activeStartTime;

    //what time did we clock out f?
    private  Date activeEndTime;

    // list of start and end times
    private List<Duration> timings;

    //duration of task in seconds - used to store in the database & to measure with for the analysis later on
    private long durationSecs;

    //duration in format hours:minutes:seconds
    private String durationString;

    //duration object containing start + end of duration.
    private Duration duration;

    private long estimatedTime;

    private String estimatedTimeString;

    private Date dueDate;

    private boolean goalComplete;

    //To make updates to the database
    private DatabaseDriver db;



    private long totaltime;

    private Category parentCategory;

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
    public void clockIn() {
        // we can't clock in to an already active task
        if(active) {
            System.out.println("Tried to clock in active task " + name);
        }

        else {
            System.out.println("Clocked in task " + name);

            activeStartTime = new Date();


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

            System.out.println("total time = " + getTotalTime());
            if(getTotalTime() >= estimatedTime) {

                System.out.println("goal reached!" );
            }
            // clear the active variables
            active = false;


            //save task to the database after clocking out.

            db.updateTask(getName(), getParentCategory().getName(),getTotalTimeString(),
                    getTotalTime(),getEstimatedTimeString(),getEstimatedTime(), getGoalComplete(), getDueDate());// activeEndTime, activeEndTime);

            String getDuration = getLengthOfLastClockInOut();
            db.addTaskDuration(getName(),durationSecs, getDuration, activeStartTime, activeEndTime);

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

    // returns 'active' clock time: time it has currently been running
    // for
    public String getActiveRunTimeString() {
        if (!active) {
            System.out.println("Tried to getActiveRunTimeString for non-active task " + name);
            return "00:00:00";
        } else {
            Date rightNow = new Date();

            // current run time in milliseconds
            // will break at daylight savings time crossover but who cares
            long milliSecondDelta = rightNow.getTime() - activeStartTime.getTime();

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

        for(Duration d : timings) {
            totaltime += d.time();
        }
        return totaltime;
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
        return convertTime(getTotalTime());
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

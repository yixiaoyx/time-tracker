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
    private long duration;

    //duration in format hours:minutes:seconds
    private String durationString;

    private Date startTime;

    private Date finishTime;

    public Category parentCategory;

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

            activeEndTime = new Date();
            System.out.println("Clock-out got end time: " + activeEndTime.toString());

            addTiming(activeStartTime, activeEndTime);

            // clear the active variables
            active = false;

        }
    }

    public List<Duration> getTimings() {
        return timings;
    }

    public void addTiming(Duration d) {
        timings.add(d);
    }

    public void addTiming(Date start, Date end) {
        Duration d = new Duration(start, end);
        timings.add(d);
    }

    public Boolean getStatus() {
        return active;
    }

    public String getName() {

        return name;
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
            // this snippet taken from https://stackoverflow.com/questions/43892644
            String timeConverted = convertTime(milliSecondDelta);

            return timeConverted;
        }
    }

    //get total duration of task
    public String getLengthOfLastClockInOut() {
        long difference = activeEndTime.getTime()-activeStartTime.getTime();
        duration = difference/1000;
        String timeConverted = convertTime(difference);

        activeStartTime = null;


        return timeConverted;
    }

    public String getTotalTimeString() {
        long totaltime = 0;
        for(Duration d : timings) {
            totaltime += d.time();
        }
        return convertTime(totaltime);
    }

    public long durationInSeconds() {
        return duration;
    }


    public String convertTime(long milliSecondDelta) {
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
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
    public void setDurationString(String duration) {
        this.durationString = duration;
    }
    public Date getStartTime() {
        return this.startTime;
    }
    public Date getFinishTime() {
        return this.finishTime;
    }
    public String getDurationString() {
        return this.durationString;
    }
    public long getDurationInSeconds() {
        return duration;
    }

}

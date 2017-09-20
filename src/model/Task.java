package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Task {
  private String name;

  // are we currently tracking time for this task?
  private Boolean active;

  // what time did we start the current active clock?
  private Date activeStartTime;

  
  // list of start and end times
  private List<Duration> timings;

  private Category parentCategory;
  
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

      Date activeEndTime = new Date();
      System.out.println("Clock-out got end time: " + activeEndTime.toString());

      addTiming(activeStartTime, activeEndTime);
      
      // clear the active variables
      active = false;
      activeStartTime = null;
    }
  }

  // if you call Category.addTask, this will be called automatically
  public void setCategory(Category c) {
    parentCategory = c;
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
    if(!active) {
      System.out.println("Tried to getActiveRunTimeString for non-active task " + name);
      return "00:00:00";
    }
    else {
      Date rightNow = new Date();

      // current run time in milliseconds
      // will break at daylight savings time crossover but who cares
      long milliSecondDelta = rightNow.getTime() - activeStartTime.getTime();

      // now we want to convert this to hh:mm:ss
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
  }
}

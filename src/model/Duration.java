package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.Date;

public class Duration extends RecursiveTreeObject<Duration> {
  private Date start;
  private Date end;

  private String parentTask;

  
  public Duration(Date start, Date end, String parent) {
    this.start = start;
    this.end = end;
    this.parentTask = parent;
  }

  public String getParentTask() {
    return parentTask;
  }

  public Date getStart() {
    return start;
  }

  public Date getEnd() {
    return end;
  }

  public long time() {
    return end.getTime() - start.getTime();
  }

  public String timeString() {
    return convertTime(time());
  }

  public double timeInHours() {
    return time()/1000/60/60;
  }

  public double timeInMinutes() {
    System.out.println(time());
    return time()/1000.0/60.0;
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



}

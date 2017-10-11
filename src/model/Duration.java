package model;

import java.util.Date;

public class Duration {
  private Date start;
  private Date end;

  public Duration(Date start, Date end) {
    this.start = start;
    this.end = end;
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
}

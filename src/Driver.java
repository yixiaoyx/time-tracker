/**
 * App Driver
 * Created by Yi on 11/9/17.
 */
public class Driver {
    Task t;

    public Driver() {
    }

    public void createTask() {
        t = new Task("Dummy Task");
    }

    public void startClock() {
        t.clockIn();

    }

    public void stopClock() {
        t.clockOut();
    }

    public String displayTask() {
        return t.getName();
    }

    public String showActiveRuntime() {
        return t.getActiveRunTimeString();
    }

    public Boolean taskStatus() {
        return t.getStatus();
    }
}

package model;

import java.util.*;

public class Category {
  private List<Task> childTasks;
  private List<Category> subCategories;
  private Category parentCategory;


  private String name;

  public Category(String name) {
    this.name = name;
    childTasks = new ArrayList<Task>();
    subCategories = new ArrayList<Category>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addTask(Task t) {
    childTasks.add(t);
    t.setParentCategory(this);
  }

  public void addTaskWithoutParentUpdate(Task t) {
    childTasks.add(t);
  }

  public void deleteTask(Task t) {
    childTasks.remove(t);
  }

  public List<Task> getTasks() {
    return childTasks;
  }

  public List<String> getTaskNames() {
    List<String> names = new ArrayList<String>();
    for(Task t : childTasks) {
      names.add(t.getName());
    }
    return names;

  }

  public List<String> getSubCategoryNames() {
    List<String> names = new ArrayList<String>();
    for(Category c : subCategories) {
      names.add(c.getName());
    }
    return names;
  }

  public void addSubCategory(Category c) {
    subCategories.add(c);
    c.setParentCategory(this);
  }

  // shallow delete: remove the reference only
  public void removeSubCategoryReference(Category c) {
    subCategories.remove(c);
  }

  // deep delete: delete everything in the sub category
  public void deleteSubCategory(Category c, List<String> categories, List<String> tasks) {

      for (Task t: childTasks) {
          tasks.add(t.getName());
      }
      c.childTasks.clear();

      List<Category> toDelete = new ArrayList<>();
      toDelete.addAll(c.subCategories);
      for(Category sub: toDelete) {
          c.deleteSubCategory(sub, categories, tasks);
      }
      categories.add(c.name);
      subCategories.remove(c);
      toDelete.clear();
  }

  public void setParentCategory(Category c) {
    parentCategory = c;
  }

  public Category getParentCategory() {
    return this.parentCategory;
  }


  public boolean isDueSoon() {
    for(Task t : recursiveGetTasks()) {
      if(t.isDueDateApproaching()) {
        return true;
      }
    }

    return false;
  }


  public Category getSubCategoryByName(String uniqueName) {
    for(Category c : subCategories) {
        // check if it's one of our categories
        if (c.getName().equals(uniqueName)) {
            return c;
        }

        // now check if it's a sub category of one of the top level categories
        else {
            Category c2 = c.getSubCategoryByName(uniqueName);
            if (c2 != null) {
                return c2;
            }
        }
    }

    // didn't find it
    return null;

  }


  public Task getTaskByName(String uniqueName) {
    for(Task t : childTasks) {
      if(t.getName().equals(uniqueName)) {
	return t;
      }
    }

    for(Category c : subCategories) {
      Task t = c.getTaskByName(uniqueName);
      if(t != null) {
	return t;
      }
    }

    return null;
  }

  // prints out the tasks/sub-categories etc. we have
  public void displaySelf(String indent) {
    System.out.println(indent + "Category '" + getName() + "'");

    System.out.println(indent + "Tasks: ");
    for(Task t : getTasks()) {
      System.out.println(indent + "    " + t.getName());
    }

    System.out.println(indent + "Sub-categories: ");
    for(Category c : subCategories) {
      c.displaySelf(indent + "  ");
    }
  }


  public long getTotalTime() {
    // the total time = sum of all child tasks time + sum of all subcategory times
    long totaltime = 0;
    for(Task t : childTasks) {
      totaltime += t.getTotalTime();
    }
    for(Category c : subCategories) {
      totaltime += c.getTotalTime();
    }
    return totaltime;
  }

  public String getTotalTimeString() {
    return convertTime(getTotalTime());
  }

  public Map<Integer, Double> getFormattedTimings() {
    System.out.println("Getting the formatted timings from category " + name);
    Map<Integer, Double> ts = new HashMap<Integer, Double>();

    ts.put(0, 2.0);
    ts.put(1, 0.5);
    ts.put(2, 0.1);
    ts.put(3, 5.0);
    ts.put(3, 0.0);
    ts.put(3, 3.0);
    ts.put(0, 2.51);
    ts.put(11, 0.0);
    ts.put(21, 0.0);
    ts.put(31, 0.0);
    ts.put(41, 0.0);
    ts.put(51, 3.2);


    for(Task t: childTasks) {
      for(Duration d : t.getTimings()) {
        // calculate how many days ago
        int daysAgo = (int) ((System.currentTimeMillis() - d.getStart().getTime()) / (24 * 60 * 60 * 1000d));

        if(ts.containsKey(daysAgo)) {
          ts.put(daysAgo, ts.get(daysAgo) + d.timeInMinutes());
          System.out.println("Added " + d.timeInMinutes() + " to " + daysAgo);
        }
        else {
          ts.put(daysAgo, d.timeInMinutes());
          System.out.println("Put " + d.timeInMinutes() + " in " + daysAgo);

        }
      }
    }

    for(Category c : subCategories) {
      Map<Integer, Double> newTimings = c.getFormattedTimings();
      for(int k : newTimings.keySet()) {
        if(ts.containsKey(k)) {
          ts.put(k, ts.get(k) + newTimings.get(k));
          System.out.println("Added " + newTimings.get(k) + " to " + k);
          //System.out.println("Now: " + ts.get(k));
        }
        else {
          ts.put(k, newTimings.get(k));
          System.out.println("Put " + newTimings.get(k) + " in " + k);

        }

      }
    }


    return ts;
  }

  List<Task> recursiveGetTasks() {
    List<Task> l = new ArrayList<Task>();
    for(Task t : childTasks) {
      l.add(t);
    }

    for(Category c : subCategories) {
      l.addAll(c.recursiveGetTasks());
    }

    return l;
  }

  List<Category> recursiveGetCategories() {
    List<Category> l = new ArrayList<Category>();
    for(Category c : subCategories) {
      l.add(c);
    }

    for(Category c : subCategories) {
      l.addAll(c.recursiveGetCategories());
    }

    return l;
  }

  Map<String, Double> getTaskBreakdown() {
    Map<String, Double> m = new HashMap<String, Double>();
    for(Task t : recursiveGetTasks()) {
      System.out.println("Inside Category, time is: " + t.getTotalTimeInMinutes());
      m.put(t.getName(), t.getTotalTimeInMinutes());
    }
    return m;
  }

  public List<Duration> getDurations() {
    List<Duration> l = new ArrayList<Duration>();
    for(Task t : recursiveGetTasks()) {
      for(Duration d : t.getTimings()) {
        l.add(d);
      }
    }
    return l;
  }


  public List<String> searchForTasks(String searchQuery) {
    List<String> l = new ArrayList<String>();
    for(Task t : recursiveGetTasks()) {
      if(t.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
        l.add(t.getName());
      }
    }

    return l;
  }

  public List<String> searchForCategories(String searchQuery) {
    List<String> l = new ArrayList<String>();
    for(Category c : recursiveGetCategories()) {
      if(c.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
        l.add(c.getName());
      }
    }

    return l;

  }

  public void clearTasksAndCategories() {
    this.childTasks = new ArrayList<Task>();
    this.subCategories = new ArrayList<Category>();
  }

  // LAZY Copied from Task, cleanup if have time
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

}

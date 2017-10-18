package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// SEE MAIN METHOD AT BOTTOM OF FILE FOR EXAMPLE OF HOW TO USE



// use this class as a bridge between the front end and back end.
public class InterfaceDriver {

  // each of these can have sub-categories, and sub-tasks
  private List<Category> topLevelCategories;
  DatabaseDriver db;

  public InterfaceDriver() {
    topLevelCategories = new ArrayList<Category>();
    db = new DatabaseDriver();
  }

  // make sure this name is unique, otherwise will cause
  // problems. View it as a unique id rather than a name.
  public void addCategory(String uniqueName) {
    System.out.println("InterfaceDriver: added category " + uniqueName);
    topLevelCategories.add(new Category(uniqueName));
  }


  public void addSubCategory(String parentCategoryName, String uniqueName) {
    System.out.println("InterfaceDriver: adding sub-category " + uniqueName + " to category " + parentCategoryName);

    Category c = getCategoryByName(parentCategoryName);

    if(c != null) {
      c.addSubCategory(new Category(uniqueName));
      if(parentCategoryName.equals("All")) {
        db.saveCategory(uniqueName, parentCategoryName);

      }
      else {
        db.saveSubCategory(uniqueName, parentCategoryName);

      }
    }
    else {
      System.out.println("Couldn't find parent category " + parentCategoryName);
    }
  }


  public void deleteSubCategory(String uniqueName) {


      Category c = getCategoryByName(uniqueName);
      Category parent = c.getParentCategory();

      if(parent != null) {
          List<String> categories = new ArrayList<String>();
          List<String> tasks = new ArrayList<String>();
          parent.deleteSubCategory(c, categories, tasks);

          for (String name: categories) {
              db.deleteCategory(name);
          }
          for (String name: tasks) {
              db.deleteTask(name);
          }
          System.out.println("InterfaceDriver: delete sub-category " + uniqueName);
          System.out.println("deleted categories: "+categories.toString());
          System.out.println("deleted tasks: "+tasks.toString());
      }
      else {
          System.out.println("Couldn't find parent category for sub-category " + uniqueName);
      }
  }


  public void addTask(String categoryName, String uniqueName) {
    System.out.println("InterfaceDriver: added task " + uniqueName + " to category " + categoryName);

    Category c = getCategoryByName(categoryName);

    if(c != null) {
      c.addTask(new Task(uniqueName));
      db.saveTasks(uniqueName,categoryName, null, 0, null, null);
    }
    else {
      System.out.println("Couldn't find category " + categoryName);
    }
  }

  public void deleteTask(String uniqueName) {

    Task t = getTaskByName(uniqueName);
    Category c = t.getParentCategory();


    if(c != null) {
      System.out.println("InterfaceDriver: delete task " + uniqueName);
      c.deleteTask(t);
      db.deleteTask(uniqueName);
    }
    else {
      System.out.println("Couldn't find parent category of task " + uniqueName);
    }
  }

  //retrieve all categories from the database and add into topLevelCategories if it doesn't exist already
  public void retrieveAllCategories() {
    List<Category> categories = db.restoreCategories();
    for(Category c: categories) {
      if(!topLevelCategories.contains(c)) {
        System.out.println("adding category => " + c.getName() + " under its parent -> " + c.getParentCategory().getName());
        addSubCategory(c.getParentCategory().getName(), c.getName());
      }
    }


  }

  public void retrieveAllTasks() {
    List<Task> getTasks = db.restoreTasks();

    for(Task t: getTasks) {
      Category c = t.getParentCategory();
      if(c.getTaskByName(t.getName()) == null) { //find if task already exists.
        addTask(c.getName(), t.getName());
      }

    }

  }


  public void clockIn(String taskName) {
    Task t = getTaskByName(taskName);
    if(t != null) {
      System.out.println("InterfaceDriver: clocked in to " + taskName);
      t.clockIn();
    }
    else {
      System.out.println("InterfaceDriver: clock in ERROR couldn't find " + taskName);
    }
  }

  public void clockOut(String taskName) {
    Task t = getTaskByName(taskName);
    if(t != null) {
      System.out.println("InterfaceDriver: clocked out of " + taskName);
      t.clockOut();
    }
    else {
      System.out.println("InterfaceDriver: clock out ERROR couldn't find " + taskName);
    }
  }


  // finds an already existing category with the given name
  public Category getCategoryByName(String uniqueName) {
    for(Category c : topLevelCategories) {
      // check if it's just a top level category
      if(c.getName().equals(uniqueName)) {
        return c;
      }

      // now check if it's a sub category of one of the top level categories
      else {
        Category c2 = c.getSubCategoryByName(uniqueName);
        if(c2 != null) {
          return c2;
        }
      }
    }

    // awkward: didn't find it
    return null;
  }


  public Task getTaskByName(String uniqueName) {
    for(Category c : topLevelCategories) {
      Task t = c.getTaskByName(uniqueName);
      if(t != null) {
        return t;
      }
    }
    return null;
  }

  // helper function that prints out a representation of the categories/sub categories/tasks
  public void displayStructure() {
    for(Category c : topLevelCategories) {
      c.displaySelf("");
    }
  }


  public List<String> getTopLevelCategoryNames() {
    List<String> names = new ArrayList<String>();
    for(Category c : topLevelCategories) {
      names.add(c.getName());
    }
    return names;
  }

  public List<Category> getTopLevelCategory() {
    List<Category> names = new ArrayList<Category>();
    for(Category c : topLevelCategories) {
      names.add(c);
    }
    return names;
  }



  public List<String> getSubCategoryNames(String categoryName) {
    Category c = getCategoryByName(categoryName);
    if(c == null) {
      System.out.println("InterfaceDriver: getSubCategoryNames error");
      return null;
    }
    else {
      return c.getSubCategoryNames();
    }
  }

  public List<String> getTaskNames(String categoryName) {
    Category c = getCategoryByName(categoryName);
    if(c == null) {
      System.out.println("InterfaceDriver: getTaskNames error");
      return null;
    }
    else {
      return c.getTaskNames();
    }
  }

  public String getParentCategoryName(String categoryName) {
    Category c = getCategoryByName(categoryName);
    if (c==null) {
      System.out.println("cannot find "+categoryName);
    }
    Category p = c.getParentCategory();
    if(p != null) {
      return p.getName();
    }
    else {
      System.out.println("parent category is not set for "+categoryName);
      return "";
    }
  }

  public String getParentCategoryOfTask(String taskName) {
    Task t = getTaskByName(taskName);
    return t.getParentCategory().getName();
  }

  public void addTimingToTask(String taskName, Duration d) {
    Task t = getTaskByName(taskName);
    t.addTiming(d);
  }

  public void changeTaskName(String currentTaskname, String newTaskname) {
    Task t = getTaskByName(currentTaskname);
    Category c = t.getParentCategory();

    if (c != null) {
      System.out.println("InterfaceDriver: Changing task name from " + currentTaskname + " to " + newTaskname);
      t.setName(newTaskname);
      db.changeTaskname(currentTaskname, newTaskname);
    } else {
      System.out.println("Couldn't find task with the name " + currentTaskname);
    }
  }
  public void changeCategory(String currentCategoryName, String newCategoryName) {

    Category c = getCategoryByName(currentCategoryName);
    if (c != null) {
      System.out.println("InterfaceDriver: Changing category name from " + currentCategoryName + " to " + newCategoryName);
      c.setName(newCategoryName);
      db.changeCategory(currentCategoryName, newCategoryName);
    } else {
      System.out.println("Couldn't find category with the name " + currentCategoryName);
    }
  }

  // use for analysis
  public String getTaskTimeString(String taskName) {
    Task t = getTaskByName(taskName);
    return t.getTotalTimeString();
  }

  public String getCategoryTimeString(String categoryName) {
    Category c = getCategoryByName(categoryName);
    return c.getTotalTimeString();
  }

  public Map<Integer, Double> getFormattedTimingsFromCategory(String categoryName) {
    Category c = getCategoryByName(categoryName);
    return c.getFormattedTimings();
  }

  // maps from a task name to the amount of time spent on it
  public Map<String, Double> getTaskBreakdown(String categoryName) {
    Category c = getCategoryByName(categoryName);
    return c.getTaskBreakdown();
  }


  public List<Duration> getDurationsFromCategory(String categoryName) {
    Category c = getCategoryByName(categoryName);
    return c.getDurations();
  }

  // this function makes a category containing only the given task, named the same as the given task.
  // This allows us to analyse individual tasks the same way we analyse categories.
  public void makeDummyCategory(String taskName) {
    Category c = new Category(taskName);
    c.addTaskWithoutParentUpdate(getTaskByName(taskName));
    topLevelCategories.add(c);
  }

  public static void main(String[] args) {
    // Basic workflow:

    // Initialise:
    InterfaceDriver I = new InterfaceDriver();

    // Create some categories and tasks:
    I.addCategory("Uni");
    I.addCategory("Work");

    I.addSubCategory("Uni", "Ethics");
    I.addSubCategory("Ethics", "Project");

    I.addTask("Project", "Standup diary");
    I.addTask("Work", "Siphoning funds");


    // Clock in and out of tasks
    I.clockIn("Standup diary");
    I.clockOut("Standup diary");

    I.clockIn("Siphoning funds");
    I.clockOut("Siphoning funds");


    // Get names of categories, sub-categories and tasks for displaying
    I.getSubCategoryNames("Uni");
    I.getTopLevelCategoryNames();
    I.getTaskNames("Project");

    // See debugging printout of category/subcategory/task structure
    I.displayStructure();


    // TODO: get time clocked in, analysis info from tasks as required

  }
}

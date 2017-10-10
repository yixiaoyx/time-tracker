package model;

import java.util.List;
import java.util.ArrayList;

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
  
  public void addTask(Task t) {
    childTasks.add(t);
    t.setParentCategory(this);
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

  public void setParentCategory(Category c) {
    parentCategory = c;
  }

  public Category getParentCategory() {
    return this.parentCategory;
  }

  public Category getSubCategoryByName(String uniqueName) {
    for(Category c : subCategories) {
      // check if it's one of our categories
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

  
  // TODO: add functions to access subtasks, do analysis etc.

}

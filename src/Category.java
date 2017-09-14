import java.util.List;
import java.util.ArrayList;

public class Category {
  private List<Task> childTasks;
  private List<Category> childCategories;
  private Category parentCategory;

  
  private String name;
  
  public Category(String name) {
    this.name = name;
    childTasks = new ArrayList<Task>();
    childCategories = new ArrayList<Category>();
  }

  public void addTask(Task t) {
    childTasks.add(t);
    t.setCategory(this);
  }

  public void addSubCategory(Category c) {
    childCategories.add(c);
    c.setParentCategory(this);
  }

  public void setParentCategory(Category c) {
    parentCategory = c;
  }


  // TODO: add functions to access subtasks, do analysis etc.

}

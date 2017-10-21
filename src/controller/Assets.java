package controller;

import java.io.*;
import javafx.scene.image.*;

public class Assets {
    // FILEPATHS FOR BUTTON GRAPHICS
    final static File categoryFile= new File("src/assets/Category_2.png");
    final static File taskFile= new File("src/assets/Task.png");
    final static File backFile = new File("src/assets/Back_Button_3.png");
    final static File analysisFile = new File("src/assets/Graph_1.png");
    final static File addFile = new File("src/assets/Add_Button_3.png");
    final static File binFile = new File("src/assets/Bin_1.png");
    final static File searchFile = new File("src/assets/Search_1.png");
    final static File changeFile = new File("src/assets/Edit_1.png");

    final static String categoryPath = categoryFile.toURI().toString();
    final static String taskPath = taskFile.toURI().toString();
    final static String backPath = backFile.toURI().toString();
    final static String analysisPath = analysisFile.toURI().toString();
    final static String addPath = addFile.toURI().toString();
    final static String binPath = binFile.toURI().toString();
    final static String searchPath = searchFile.toURI().toString();
    final static String changePath = changeFile.toURI().toString();

    final static ImageView categoryImage = new ImageView(new Image(categoryPath, false));
    final static ImageView taskImage = new ImageView(new Image(taskPath, false));
    final static ImageView backImage = new ImageView(new Image(backPath, false));
    final static ImageView analysisImage = new ImageView(new Image(analysisPath, false));
    final static ImageView addImage = new ImageView(new Image(addPath, false));
    final static ImageView binImage = new ImageView(new Image(binPath, false));
    final static ImageView searchImage = new ImageView(new Image(searchPath, false));
    final static ImageView changeImage = new ImageView(new Image(changePath, false));
}

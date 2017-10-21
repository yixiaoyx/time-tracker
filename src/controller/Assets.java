package controller;

import java.io.*;
import javafx.scene.image.*;

public class Assets {
    // FILEPATHS FOR BUTTON GRAPHICS
    final static File categoryFile= new File("src/assets/Category_2.png");
    final static File categoryAlertFile = new File("src/assets/CategoryAlert_1.png");
    final static File taskFile= new File("src/assets/Task.png");
    final static File taskAlertFile = new File("src/assets/TaskAlert_1.png");
    final static File backFile = new File("src/assets/Back_Button_3.png");
    final static File analysisFile = new File("src/assets/Graph_1.png");
    final static File addFile = new File("src/assets/Add_Button_3.png");
    final static File binFile = new File("src/assets/Bin_1.png");
    final static File searchFile = new File("src/assets/Search_1.png");
    final static File changeFile = new File("src/assets/Edit_1.png");

    final static File badge1File = new File("src/assets/Badge_1.png");
    final static File badge2File = new File("src/assets/Badge_2.png");
    final static File badge3File = new File("src/assets/Badge_3.png");
    final static File badge4File = new File("src/assets/Badge_4.png");
    final static File badge5File = new File("src/assets/Badge_5.png");

    final static String categoryPath = categoryFile.toURI().toString();
    final static String categoryAlertPath = categoryAlertFile.toURI().toString();
    final static String taskAlertPath= taskAlertFile.toURI().toString();
    final static String taskPath = taskFile.toURI().toString();
    final static String backPath = backFile.toURI().toString();
    final static String analysisPath = analysisFile.toURI().toString();
    final static String addPath = addFile.toURI().toString();
    final static String binPath = binFile.toURI().toString();
    final static String searchPath = searchFile.toURI().toString();
    final static String changePath = changeFile.toURI().toString();
    final static String badge1Path = badge1File.toURI().toString();
    final static String badge2Path = badge2File.toURI().toString();
    final static String badge3Path = badge3File.toURI().toString();
    final static String badge4Path = badge4File.toURI().toString();
    final static String badge5Path = badge5File.toURI().toString();

    final static Image categoryImage = new Image(categoryPath, false);
    final static Image categoryAlertImage = new Image(categoryAlertPath, false);
    final static Image taskImage = new Image(taskPath, false);
    final static Image taskAlertImage = new Image(taskAlertPath, false);

    final static ImageView backImage = new ImageView(new Image(backPath, false));
    final static ImageView analysisImage = new ImageView(new Image(analysisPath, false));
    final static ImageView addImage = new ImageView(new Image(addPath, false));
    final static ImageView binImage = new ImageView(new Image(binPath, false));
    final static ImageView searchImage = new ImageView(new Image(searchPath, false));
    final static ImageView changeImage = new ImageView(new Image(changePath, false));

    final static ImageView badge1Image = new ImageView(new Image(badge1Path, false));
    final static ImageView badge2Image = new ImageView(new Image(badge2Path, false));
    final static ImageView badge3Image = new ImageView(new Image(badge3Path, false));
    final static ImageView badge4Image = new ImageView(new Image(badge4Path, false));
    final static ImageView badge5Image = new ImageView(new Image(badge5Path, false));
}

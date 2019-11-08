package com.mycompany.rplproject;


import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainApp extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        Image icon = new Image(getClass().getResourceAsStream("/fxml/Free_Sample_By_Wix.jpg"));
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("WeBook");
        stage.setScene(scene);
        stage.show();
    }
    

    public static void main(String[] args) {
        launch(args);
    }

}

package sample.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;

import java.io.IOException;

public class CustomDialog<T> extends Dialog<T>{
    public CustomDialog(String source){
        super();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(source));
        try{
            this.getDialogPane().setContent(fxmlLoader.load());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            return;
        }
        this.getDialogPane().getScene().getWindow()
                .setOnCloseRequest(event -> this.getDialogPane().getScene().getWindow().hide());

    }
}

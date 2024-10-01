package progettochatsocket.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApplication extends Application {

    FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        //Avvio l'app
        fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("GUI/hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CHAT");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }
}
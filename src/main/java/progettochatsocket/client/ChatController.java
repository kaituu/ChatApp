package progettochatsocket.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER_RIGHT;

public class ChatController implements Initializable {

    @FXML
    private TextField inputMessaggio;

    @FXML
    private TextField inputUtente;

    @FXML
    private VBox chatMessaggi;

    @FXML
    private VBox listaUtenti;

    private ClientGui client;

    //Avvio il client
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inputMessaggio.setDisable(true);

        Thread thread = new Thread(this::avviaClient);
        thread.start();
    }

    public void avviaClient() {
        client = new ClientGui(this);
        client.connetti();
    }

    public String elementBack(String msg) {
        stampaMessaggio(msg);
        return "1";
    }

    //Invia il messaggio al server attraverso il socket
    @FXML
    void bottoneInviaMessaggio(MouseEvent event) {
        String nuovoMex = inputMessaggio.getText();
        if (!nuovoMex.isEmpty()) {
            client.scriviAlServer(nuovoMex);
            inputMessaggio.clear();
        }
    }

    @FXML
    void bottoneNuovoUtente(MouseEvent event) {
        String nuovoUtente = inputUtente.getText();
        if (!nuovoUtente.isEmpty()) {

            stampaUtente(nuovoUtente);
            client.scriviAlServer(nuovoUtente); //Invio al server il nome
            //verifico che il nome sia univoco

            inputUtente.clear();

            inputUtente.setDisable(true);
            inputMessaggio.setDisable(false);
        }
    }

    public synchronized void stampaMessaggio(String msg) {


        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                TextFlow messaggio = new TextFlow(new Text(msg));
                HBox prova = new HBox();
                messaggio.setStyle("-fx-padding: 7;" + "-fx-border-style: solid inside;"
                        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                        + "-fx-border-radius: 5;" + "-fx-border-color: black;" + "fx-background-color: green;");
                // Align the message container to the right
                prova.setAlignment(CENTER_RIGHT); // This aligns the HBox content to the right.

                prova.getChildren().add(messaggio);
                chatMessaggi.getChildren().add(prova);
            }

        });
    }

    public synchronized void stampaUtente(String utente) {

        Text utentei = new Text(utente);
        utentei.setFill(Color.RED);
        TextFlow listautente = new TextFlow(utentei);

        HBox prova = new HBox();
        listautente.setStyle("-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;" + "fx-background-color: green;");
        prova.getChildren().add(listautente);
        listaUtenti.getChildren().add(prova);
    }

    @FXML
    void esci(MouseEvent event) {
        System.exit(1);
    }
}
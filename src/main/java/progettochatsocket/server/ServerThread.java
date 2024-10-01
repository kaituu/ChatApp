package progettochatsocket.server;

import progettochatsocket.common.Messaggio;
import progettochatsocket.common.User;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {
    Socket client = null;
    Chat chat = null;

    User server = null;
    User utente = null;
    String stringaRicevuta = null;         //stringa ricevuta dal client
    BufferedReader inDalClient;            //stream di input
    BufferedWriter outVersoClient;         //stream di output
    Boolean controllo = true;              //permette di terminare il thread

    //Creo il costruttore della classe
    public ServerThread() {

    }

    public ServerThread(Socket socket, Chat chat, User server) {
        this.client = socket;
        this.chat = chat;
        this.server = server;
    }

    // Effetuo l'ovveride del metodo che implementa il thread
    // ed avvia la comunicazione
    @Override
    public void run() {
        comunica();
    }

    /**
     * Secondo Thread
     * Effettua la comunicazione col client e si mette in ascolto sulla socket.
     * Il server si pone in attesa di lettura di una stringa dal canale di input dal client
     */
    public void comunica() {

        //Creazione stream di in and out per i messaggi
        try {
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante la creazione dei buffer");
            System.exit(1);
        }

        System.out.println("[SERVER]\tConnessione con il client stabilita");

        //Verifico che il nome utente sia univoco e lo aggiungo alla chat
        do {

            stringaRicevuta = ricevoDalClient();
            utente = new User(stringaRicevuta);

        } while (!chat.addUser(utente));

        Messaggio nuovoMsg = new Messaggio(server, utente.getName() + " si è unito alla chat");
        chat.addMessage(nuovoMsg);

        //Invio i messaggi non letti
        List<Messaggio> msgsNonLetti = chat.getNewMessages(utente);
        for (Messaggio msg : msgsNonLetti) {
            scriviAlClient(msg.getUser().getName() + " : " + msg.getContent());
        }

        //Inizio conversazione in chat
        scriviAlClient("BENVENUTO NELLA  CHAT");

        //Avvio thread terzario per lettura di messaggi dalla lista
        Thread thread = new Thread(() -> controlloLista());
        thread.start();
        while (controllo) {
            //leggo dal socket
            stringaRicevuta = ricevoDalClient();
            //aggiungo alla lista
            Messaggio msg = new Messaggio(utente, stringaRicevuta);
            chat.addMessage(msg);
        }

        nuovoMsg = new Messaggio(server, utente.getName() + " è uscito dalla chat");
        chat.addMessage(nuovoMsg);
        chat.removeUser(utente);
    }

    /**
     * Terzo Thread
     * Ottiene ogni secondo i nuovi messaggi scritti all'interno della lista
     * e li scrive sul socket
     */
    private void controlloLista() {
        for (; ; ) {
            //controllo ad intervalli di 2000 mills

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                controllo = false;
            }
            List<Messaggio> msgsNonLetti = chat.getNewMessages(utente);
            for (Messaggio msg : msgsNonLetti) {
                scriviAlClient(msg.getUser().getName() + " : " + msg.getContent());
            }
        }


    }

    private void scriviAlClient(String str) {

        try {

            outVersoClient.write(str + "\n");
            outVersoClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante l'invio di un messaggio al client");
            controllo = false;

        }

    }

    private String ricevoDalClient() {
        try {
            return inDalClient.readLine();
        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Errore durante la ricezione di un messaggio dal client");
            controllo = false;

        }
        return "";
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettochatsocket.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Gestisce la logica della comunicazione con il server e genera
 * i dati che verranno presentati all'utente tramite CLI o GUI
 * - Decide se lanciare la CLI o la GUI
 * - Si connette al server
 * - Inizia a comunicare con il server
 * - Legge dallo standard input (tastiera su command line) o dalla GUI
 * e invia la stringa letta al server
 * - Legge una stringa dalla socket con il server e la scrive sullo
 * standard output (command line), oppure sulla GUI
 * - Queste due cose vanno fatte contemporaneamente, su thread diversi.
 * - Problema di concorrenza: siccome potrebbero essere ricevuti messaggi
 * mentre il programma è in ascolto dell'input da tastiera su command
 * line, e questi messaggi vanno subito mostrati in output, esiste una
 * concorrenza tra input e output nell'utilizzo della command line.
 */
public class Client extends Thread {
    String serverName = "127.0.0.1";        //Indirizzo risolvibile del server
    int serverPort = 7777;                  //Porta per la creazione della socket
    Socket canale;
    BufferedReader tastiera;                //Buffer per l'input da tastiera
    String stringaDaSpedire;                 //Stringa inserita dall'utente per l'invio al server
    String stringaRicevutaDalServer;        //Stringa ricevuta dal server
    BufferedReader inDalServer;             //Stream di input
    BufferedWriter outVersoServer;          //Stream di output

    public static void main(String[] args) {
        Client client = new Client();
        client.connetti();
        client.leggoTastiera();

    }

    //Effetuo l'ovveride del metodo che implementa il thread
    // ed avvia la ricezione delli messaggi dal server dal server
    @Override
    public void run() {
        leggoSocket();
    }

    /**
     * Effettua la connessione al server.
     * crea un oggetto {@link Socket} che indica l'indirizzo ip e la porta del server a cui connettersi.
     */

    public void connetti() {

        System.out.println("[CLIENT]\tProcesso client avviato");

        //Creo lo stream per l'input da tastiera
        tastiera = new BufferedReader(new InputStreamReader(System.in));

        //Creo un socket con indirizzo e porta specificati sopra
        try {
            canale = new Socket(serverName, serverPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Host non trovato");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante l'istanza del socket");
            System.exit(1);
        }

        //Associo due oggetti al socket per leggere e scrivere sullo stream
        try {
            outVersoServer = new BufferedWriter(new OutputStreamWriter(canale.getOutputStream()));
            inDalServer = new BufferedReader(new InputStreamReader(canale.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore I/O del socket");

        }


        //Creo il thread secondario per riceve i messaggi dal server, leggoSocket
        this.start();

        leggoTastiera();


    }

    /**
     * Secondo Thread
     * Legge una stringa dalla socket standard input (tastiera su command line)
     * e la stampa a video
     */
    private void leggoSocket() {
        for (; ; ) {
            //Leggo dal socket
            stringaRicevutaDalServer = ricevoDalServer();
            //Stampo a schermo
            System.out.println(stringaRicevutaDalServer);
        }
    }

    /**
     * Primo Thread
     * Legge dallo standard input (tastiera su command line)
     * e inoltra al server
     */
    private void leggoTastiera() {


        for (; ; ) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Thread interrotto");
                System.exit(1);
            }

            stringaDaSpedire = scrivoDaTastiera();

            if (stringaDaSpedire != null) {
                scriviAlServer(stringaDaSpedire);
            }
        }
    }

    /**
     * permette l'invio di una stringa al
     * socket attraverso il bufferedWriter
     *
     * @param str stringa da inviare al server
     */
    void scriviAlServer(String str) {

        try {
            outVersoServer.write(str + "\n");
            outVersoServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante l'invio di un messaggio al server");
            System.exit(1);
        }

    }

    /**
     * permette la ricezione di stringhe dal socket
     * attraverso il bufferedReader
     *
     * @return la stringa inviata dal server
     */
    private String ricevoDalServer() {
        try {
            String temp = inDalServer.readLine();
            if (temp != null) {
                return temp;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante la ricezione di un messaggio dal server");
            System.exit(1);
        }
        return null;
    }

    /**
     *
     */
    private String scrivoDaTastiera() {
        try {
            return tastiera.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante la scrittura da tastiera");
            System.exit(1);
        }
        return null;
    }
}
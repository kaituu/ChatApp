/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettochatsocket.server;

import progettochatsocket.common.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * - (thread principale) accetta un client e crea un thread secondario
 * per ogni client che si connette
 * - (thread secondario) gestire la comunicazione con il client:
 * - all'inizio chiede il nome utente, fino a che viene scelto
 * un nome non ancora utilizzato da nessuno
 * - manda al client connesso tutto lo storico della comunicazione
 * - manda il benvenuto al client e lo presenta agli altri client
 * - inizia a gestire i messaggi del client:
 * - aspetta un messaggio in ingresso
 * - inoltra il messaggio a tutti gli altri client
 */
public class Server extends Thread {
    final static int MAX_CLIENTS = 5;
    User server = null;
    Chat chat = null;

    public static void main(String[] args) {
        Server multiServer = new Server();
        multiServer.avviaGestore();
    }

    /*
     * Thread primario che si occupa di accettare nuove
     * richieste di connessione
     */
    public void avviaGestore() {

        //creo un connection socket
        ServerSocket connectionSocket = null;
        try {
            connectionSocket = new ServerSocket(7777);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante l'istanza di ServerSocket");
        }

        chat = new Chat();
        server = new User("SERVER");

        for (int i = 0; i < MAX_CLIENTS; i++) {
            //metto il socket in attesa di una richiesta da un client
            System.out.println("1 IN ATTESA");
            Socket dataSocket = null;
            try {
                dataSocket = connectionSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Errore durante l'attesa di una connessione");
            }
            //creo un istanza della classe ServerThread su un thread (secondario) e l'avvio
            System.out.println("3 Stampa socket : " + dataSocket);
            ServerThread thread = new ServerThread(dataSocket, chat, server);
            thread.start();
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettochatsocket.server;

import progettochatsocket.common.Messaggio;
import progettochatsocket.common.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chat {

    private List<Messaggio> listaMsg = new ArrayList<>();
    private List<User> listaUsers = new ArrayList<>();

    /**
     * Aggiunge un nuovo utente alla lista
     *
     * @return true se l'operazione è avvenuta
     */
    synchronized boolean addUser(User newUser) {
        if (containsUser(newUser))
            return false;
        else {
            listaUsers.add(newUser);
            return true;
        }
    }

    /**
     * Rimuove un utente dalla lista solo se presente
     */
    synchronized void removeUser(User user) {
        for (User utente : listaUsers) {
            if (utente.getName().equals(user.getName())) {
                listaUsers.remove(utente);
                break;
            }
        }
    }

    /**
     * Verifica se un utente con lo stesso nome sia
     * gia presente nella lista
     *
     * @return presenza o meno dell'utente all'interno della lista
     */
    synchronized private boolean containsUser(User user) {
        for (User utente : listaUsers) {
            if (Objects.equals(utente.getName(), user.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ritorna tutti i messaggi che l'utente "user" deve ancora leggere
     *
     * @return messaggi non letti
     */
    synchronized List<Messaggio> getNewMessages(User user) {
        List<Messaggio> nuoviMsg = new ArrayList<>();
        for (Messaggio msg : listaMsg) {
            //controlla se l'utente ha letto il messaggio
            if (!msg.getListaUtentiVisti().contains(user)) {
                //aggiungere il messaggio alla lista da restituire
                nuoviMsg.add(msg);
                //aggiunge l'utente che ha letto il messaggio alla lista degli utenti che hanno letto
                msg.getListaUtentiVisti().add(user);
            }
        }
        return nuoviMsg;
    }

    /**
     * Aggiunge un messaggio alla lista
     */
    synchronized void addMessage(Messaggio nuovoMessaggio) {
        listaMsg.add(nuovoMessaggio);
    }
}

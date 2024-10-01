/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettochatsocket.common;

import java.util.ArrayList;
import java.util.List;

public class Messaggio {

    private User user;
    private String content;
    private List<User> listaUtentiVisti;

    public Messaggio(User user, String content) {
        this.user = user;
        this.content = content;
        listaUtentiVisti = new ArrayList<>();
    }

    /**
     * Ritorna il mittente del messaggio
     *
     * @return mittente del messaggio
     */
    public User getUser() {
        return user;
    }

    /**
     * Ritorna il contenuto del messaggio
     *
     * @return contenuto del messaggio
     */
    public String getContent() {
        return content;
    }

    public List<User> getListaUtentiVisti() {
        return listaUtentiVisti;
    }
}

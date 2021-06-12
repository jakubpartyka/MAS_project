package app.data;

import java.util.ArrayList;

public class Court {
    public static ArrayList<Court> allCourts = new ArrayList<>();

    public int id;
    public int cena;
    public String nawierzchnia;
    public boolean oswietlenie,kryty;

    public Court(int id, int cena, String nawierzchnia, boolean oswietlenie, boolean kryty) {
        this.id = id;
        this.cena = cena;
        this.nawierzchnia = nawierzchnia;
        this.oswietlenie = oswietlenie;
        this.kryty = kryty;

        allCourts.add(this);
    }
}

package app.data;

import java.sql.Date;

public abstract class Person {
    final int id;

    String imie,nazwisko;
    Date data_ur;
    private int numer;

    protected Person(int id, String imie, String nazwisko, int numer, Date data_ur) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.data_ur = data_ur;
        setNumer(numer);
    }

    public void setNumer(int numer) {
        this.numer = numer;
    }

    public int getNumer() {
        return numer;
    }
}

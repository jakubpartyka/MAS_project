package app.data.person;

import java.sql.Date;
import java.util.ArrayList;

public class Client extends Person {
    public static ArrayList<Client> all_clients = new ArrayList<>();

    public Date data_rejestracji;
    public Integer firma_id;

    public Client(int id, String imie, String nazwisko, int numer, Date data_ur, Date data_rejestracji) {
        super(id, imie, nazwisko, numer, data_ur);
        this.data_rejestracji = data_rejestracji;
        all_clients.add(this);
    }

    @Override
    public String toString() {
        return "Klient{" +
                "data_rejestracji=" + data_rejestracji +
                ", firma_id=" + firma_id +
                ", id=" + id +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", data_ur=" + data_ur +
                '}';
    }
}

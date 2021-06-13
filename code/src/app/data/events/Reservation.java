package app.data.events;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Reservation {
    public static ArrayList<Reservation> allReservations = new ArrayList<>();

    int id, Kort_id,Klient_id,Trener_id;
    Date data;
    Time czas_od, czas_do;
    ReservationStatus status;

    public Reservation(int id, int kort_id, int klient_id, int trener_id, Date data, Time czas_od, Time czas_do, ReservationStatus status) {
        this.id = id;
        Kort_id = kort_id;
        Klient_id = klient_id;
        Trener_id = trener_id;
        this.data = data;
        this.czas_od = czas_od;
        this.czas_do = czas_do;
        this.status = status;

        allReservations.add(this);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", Kort_id=" + Kort_id +
                ", Klient_id=" + Klient_id +
                ", Trener_id=" + Trener_id +
                ", data=" + data +
                ", czas_od=" + czas_od +
                ", czas_do=" + czas_do +
                ", status=" + status +
                '}';
    }
}

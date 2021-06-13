package app.data.events;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Reservation {
    public static ArrayList<Reservation> allReservations = new ArrayList<>();

    public int id, kortId, klientId, trenerId;
    public Date data;
    public Time czas_od, czas_do;
    public ReservationStatus status;

    public Reservation(int id, int kortId, int klientId, int trenerId, Date data, Time czas_od, Time czas_do, ReservationStatus status) {
        this.id = id;
        this.kortId = kortId;
        this.klientId = klientId;
        this.trenerId = trenerId;
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
                ", Kort_id=" + kortId +
                ", Klient_id=" + klientId +
                ", Trener_id=" + trenerId +
                ", data=" + data +
                ", czas_od=" + czas_od +
                ", czas_do=" + czas_do +
                ", status=" + status +
                '}';
    }

    public boolean timeInBetween(String timeString) {
        Time time = Time.valueOf(timeString+":00");
        return time.getTime() >= czas_od.getTime() && time.getTime() < czas_do.getTime();
    }

    public static Reservation getReservationById(int id){
        for (Reservation res : allReservations) {
            if (res.id == id)
                return res;
        }
        return null;
    }
}

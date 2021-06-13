package app.data.events;

import java.sql.Date;
import java.sql.Time;

public class Reservation {
    int id, Kort_id,Klient_id,Trener_id;
    Date data;
    Time czas_od, czas_do;
    String status;
}

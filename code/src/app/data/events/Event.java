package app.data.events;

import app.data.Company;

import java.util.ArrayList;

public class Event {
    ArrayList<Event> wydarzenia = new ArrayList<>();

    int id;
    Company company;
    String name;
    int participants;
    boolean buffet;
}

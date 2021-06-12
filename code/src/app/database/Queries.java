package app.database;

public enum Queries {
    LOGIN("SELECT pass FROM user WHERE login = ?"),
    GET_CLIENTS("SELECT * FROM Klient"),
    GET_TRAINERS("SELECT * FROM Trener"),
    GET_COURTS("SELECT * FROM Kort");

    final String expression;
    Queries(String expression) {
        this.expression = expression;
    }
}

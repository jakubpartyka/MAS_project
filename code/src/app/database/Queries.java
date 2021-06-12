package app.database;

public enum Queries {
    LOGIN("SELECT pass FROM user WHERE login = ?"),
    GET_CLIENTS("SELECT * FROM Klient");

    final String expression;
    Queries(String expression) {
        this.expression = expression;
    }
}

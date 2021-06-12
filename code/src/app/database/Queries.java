package app.database;

public enum Queries {
    LOGIN("SELECT pass FROM user WHERE username = ?");

    final String expression;
    Queries(String expression) {
        this.expression = expression;
    }
}

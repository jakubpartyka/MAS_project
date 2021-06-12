package app.database;

public enum Queries {
    LOGIN("SELECT pass FROM user WHERE login = ?"),
    GET_CLIENTS("SELECT * FROM Klient"),
    GET_TRAINERS("SELECT * FROM Trener"),
    GET_COURTS("SELECT * FROM Kort"),
    GET_COMPANIES("SELECT * FROM Firma"),
    ADD_COMPANY("INSERT INTO Firma (nazwa,NIP,branza) VALUES (?,?,?)"),
    UPDATE_CLIENT("UPDATE Klient SET numer=?,Firma_id=? WHERE id=?"),
    CREATE_CLIENT("INSERT INTO Klient (imie,nazwisko,data_ur,numer,data_rejestracji) VALUES (?,?,?,?,?)");

    final String expression;
    Queries(String expression) {
        this.expression = expression;
    }
}

package app.tables;

import app.data.Klient;

import javax.swing.table.AbstractTableModel;

public class ClientTableModel extends AbstractTableModel {
    @Override
    public int getRowCount() {
        return Klient.all_clients.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Klient klient = Klient.all_clients.get(rowIndex);
        switch (columnIndex){
            case 0:
                return klient.id;
            case 1:
                return klient.imie;
                
            case 2: 
                return klient.nazwisko;
                
            case 3:
                if(klient.getNumer() == 0)
                    return "Nie podano";
                return klient.getNumer();
                
            case 4:
                return klient.data_ur;
                
            case 5: 
                return klient.data_rejestracji;
                
            case 6:
                if(klient.firma_id == 0)
                    return "N/A";
                return klient.firma_id;
                
            default:
                return "N/A";
        }
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "ID";
            case 1 -> "IMIĘ";
            case 2 -> "NAZWISKO";
            case 3 -> "NUMER";
            case 4 -> "DATA UR.";
            case 5 -> "DATA REJ.";
            case 6 -> "FIRMA";
            default -> "N/A";
        };
    }
}

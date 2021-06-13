package app.tables;

import app.data.events.Reservation;

import javax.swing.table.AbstractTableModel;

public class ScheduleTableModel extends AbstractTableModel {
    @Override
    public int getRowCount() {
        return Reservation.allReservations.size();
    }

    @Override
    public int getColumnCount() {
        return 20;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return "Kort " + column;
    }

    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }
}

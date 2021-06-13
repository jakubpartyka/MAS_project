package app.tables;

import app.data.events.Reservation;
import gui.GUI;

import javax.swing.table.AbstractTableModel;
import java.sql.Date;
import java.time.LocalDate;

public class ScheduleTableModel extends AbstractTableModel {
    @Override
    public int getRowCount() {
        return 9;
    }

    @Override
    public int getColumnCount() {
        return 21;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            switch (rowIndex){
                case 0: return "10:00";
                case 1: return "11:00";
                case 2: return "12:00";
                case 3: return "13:00";
                case 4: return "14:00";
                case 5: return "15:00";
                case 6: return "16:00";
                case 7: return "17:00";
                case 8: return "18:00";
            }
        }
        else {
            int kort_id = columnIndex;
            Date date = Date.valueOf(GUI.currentDate.toString());
            return date;

        }
        return "-";
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return "GODZINA";
        return "Kort " + column;
    }

    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }
}

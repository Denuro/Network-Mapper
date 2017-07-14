/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.components;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import netmap.entities.Port;
import netmap.entities.ScreenPort;

/**
 *
 * @author darlan.ullmann
 */
public class ScreenPortTableModel extends AbstractTableModel
{
    private final List<ScreenPort> ports;
    
    private final int COLUMN_TYPE = 0;
    private final int COLUMN_SWITCHPORT_TYPE = 1;
    
    private final String COLUMNS[] = {
        "Tipo",
        "Tipo Switchport"
    };

    public ScreenPortTableModel()
    {
        this.ports = null;
    }

    public ScreenPortTableModel(List<ScreenPort> ports)
    {
        this.ports = ports;
    }

    public List<ScreenPort> getPorts()
    {
        return ports;
    }

    @Override
    public int getRowCount()
    {
        return ports.size();
    }

    @Override
    public int getColumnCount()
    {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        ScreenPort port = ports.get(rowIndex);
        
        switch (columnIndex)
        {
            case COLUMN_TYPE:
                return Port.TYPES[port.getType()];
            case COLUMN_SWITCHPORT_TYPE:
                return ScreenPort.SWITCHPORTS.values()[port.getSwitchportType()];
        }
        
        return "";
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        ScreenPort port = ports.get(rowIndex);
        port.setSwitchportType(((ScreenPort.SWITCHPORTS)value).getId());
        
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column)
    {
        return COLUMNS[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnIndex == COLUMN_SWITCHPORT_TYPE ? JComboBox.class : String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex == COLUMN_SWITCHPORT_TYPE;
    }
    
}

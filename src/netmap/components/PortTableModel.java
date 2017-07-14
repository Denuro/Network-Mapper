/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.components;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import netmap.entities.Port;

/**
 * Table Model for Port Model
 * @author Darlan
 */
public class PortTableModel extends AbstractTableModel
{
    public static final int COLUMN_TYPE = 0;
    public static final int COLUMN_SPEED = 1;
    
    public static String[] columns = {
        "Tipo",
        "Velocidade"
    };
    private List<Port> ports = new ArrayList<>();
    
    /**
     * Set the Ports for this model
     * @param ports 
     */
    public void setPorts(List<Port> ports)
    {
        this.ports = ports;
        
        fireTableDataChanged();
    }
    
    /**
     * Get the Ports for this model
     * @return 
     */
    public List<Port> getPorts()
    {
        return ports;
    }
    
    public void clear()
    {
        ports.clear();
        
        fireTableDataChanged();
    }
    
    public void delete(int row)
    {
        ports.remove(row);
        
        fireTableDataChanged();
    }
    
    /**
     * Add Ports to this model
     * @param type
     * @param speed
     * @param amount 
     */
    public void addPorts(int type, int speed, int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            Port port = new Port();
            port.setType(type);
            port.setSpeed(speed);
            
            ports.add(port);
        }
        
        fireTableDataChanged();
    }

    @Override
    public int getRowCount()
    {
        return ports.size();
    }

    @Override
    public int getColumnCount()
    {
        return 2;
    }

    @Override
    public String getColumnName(int column)
    {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case COLUMN_TYPE:
                return Port.TYPES[ports.get(rowIndex).getType()];
            case COLUMN_SPEED:
                return ports.get(rowIndex).getSpeed() + " Mbit/s";
        }
        return "";
    }
}

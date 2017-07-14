/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author darlan.ullmann
 */
@Entity
@Table(name = "ports")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Port.findAll", query = "SELECT p FROM Port p")
    , @NamedQuery(name = "Port.findById", query = "SELECT p FROM Port p WHERE p.id = :id")
    , @NamedQuery(name = "Port.findByEquipment", query = "SELECT p FROM Port p WHERE p.equipmentId = :equipment")
})
public class Port implements Serializable
{
    public static final int TYPE_ETHERNET = 0;
    public static final int TYPE_SFP = 1;
    public static final int TYPE_SFP_PLUS = 2;
    public static final int TYPE_WIRELESS = 3;
    
    public static final String[] TYPES = {
        "Ethernet",
        "SFP",
        "SFP+",
        "Wireless"
    };
    
    public static final String[] TYPE_SPEEDS = {
        "1000",
        "1000",
        "10000",
        "400",
    };

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    @Basic(optional = false)
    @Column(name = "speed")
    private int speed;
    @Basic(optional = false)
    @Column(name = "equipment_id")
    private int equipmentId;

    public Port()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getSpeed()
    {
        return speed;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getEquipmentId()
    {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Port other = (Port) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.Port[ id=" + id + " ]";
    }
    
}

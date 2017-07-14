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
@Table(name = "screen_cables")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ScreenCable.findAll", query = "SELECT s FROM ScreenCable s")
    , @NamedQuery(name = "ScreenCable.findById", query = "SELECT s FROM ScreenCable s WHERE s.id = :id")
    , @NamedQuery(name = "ScreenCable.findByCableId", query = "SELECT s FROM ScreenCable s WHERE s.cableId = :cableId")
    , @NamedQuery(name = "ScreenCable.findByType", query = "SELECT s FROM ScreenCable s WHERE s.type = :type")
    , @NamedQuery(name = "ScreenCable.findBySpeed", query = "SELECT s FROM ScreenCable s WHERE s.speed = :speed")
})
public class ScreenCable implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "cable_id")
    private int cableId;
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    @Basic(optional = false)
    @Column(name = "speed")
    private int speed;
//    @JoinTable(name = "screen_cable_ports", joinColumns =
//    {
//        @JoinColumn(name = "cable_id", referencedColumnName = "id")
//    }, inverseJoinColumns =
//    {
//        @JoinColumn(name = "port_id", referencedColumnName = "id")
//    })
//    @ManyToMany
//    private List<ScreenPort> screenPortList;
//    @JoinTable(name = "cable_positions", joinColumns =
//    {
//        @JoinColumn(name = "cable_id", referencedColumnName = "id")
//    }, inverseJoinColumns =
//    {
//        @JoinColumn(name = "position_id", referencedColumnName = "id")
//    })
//    @ManyToMany
//    private List<Position> positionList;

    public ScreenCable()
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

    public int getCableId()
    {
        return cableId;
    }

    public void setCableId(int cableId)
    {
        this.cableId = cableId;
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

//    @XmlTransient
//    public List<ScreenPort> getScreenPortList()
//    {
//        return screenPortList;
//    }
//
//    public void setScreenPortList(List<ScreenPort> screenPortList)
//    {
//        this.screenPortList = screenPortList;
//    }
//
//    @XmlTransient
//    public List<Position> getPositionList()
//    {
//        return positionList;
//    }
//
//    public void setPositionList(List<Position> positionList)
//    {
//        this.positionList = positionList;
//    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 83 * hash + this.id;
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
        final ScreenCable other = (ScreenCable) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.ScreenCable[ id=" + id + " ]";
    }
    
}

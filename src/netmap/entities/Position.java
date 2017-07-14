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
@Table(name = "position")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Position.findAll", query = "SELECT p FROM Position p")
    , @NamedQuery(name = "Position.findById", query = "SELECT p FROM Position p WHERE p.id = :id")
})
public class Position implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "x")
    private int x;
    @Basic(optional = false)
    @Column(name = "y")
    private int y;
//    @ManyToMany(mappedBy = "positionList")
//    private List<ScreenCable> screenCableList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "positionId")
//    private List<ScreenEquipment> screenEquipmentList;

    public Position()
    {
    }

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

//    @XmlTransient
//    public List<ScreenCable> getScreenCableList()
//    {
//        return screenCableList;
//    }
//
//    public void setScreenCableList(List<ScreenCable> screenCableList)
//    {
//        this.screenCableList = screenCableList;
//    }
//
//    @XmlTransient
//    public List<ScreenEquipment> getScreenEquipmentList()
//    {
//        return screenEquipmentList;
//    }
//
//    public void setScreenEquipmentList(List<ScreenEquipment> screenEquipmentList)
//    {
//        this.screenEquipmentList = screenEquipmentList;
//    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 83 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        return (object instanceof Position) && this.id == ((Position)object).id;
    }

    @Override
    public String toString()
    {
        return "netmap.Entities.Position[ id=" + id + " ]";
    }
    
}

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
@Table(name = "maps")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Map.findAll", query = "SELECT m FROM Map m")
    , @NamedQuery(name = "Map.findById", query = "SELECT m FROM Map m WHERE m.id = :id")
    , @NamedQuery(name = "Map.findByName", query = "SELECT m FROM Map m WHERE m.name = :name")
})
public class Map implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mapId")
//    private List<ScreenEquipment> screenEquipmentList;

    public Map()
    {
    }

    public Map(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

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
        int hash = 3;
        hash = 97 * hash + this.id;
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
        final Map other = (Map) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.Map[ id=" + id + " ]";
    }
    
}

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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author darlan.ullmann
 */
@Entity
@Table(name = "screen_equipments")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ScreenEquipment.findAll", query = "SELECT s FROM ScreenEquipment s")
    , @NamedQuery(name = "ScreenEquipment.findById", query = "SELECT s FROM ScreenEquipment s WHERE s.id = :id")
    , @NamedQuery(name = "ScreenEquipment.findByName", query = "SELECT s FROM ScreenEquipment s WHERE s.name = :name")
})
public class ScreenEquipment extends ScreenItem implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "obs")
    private String obs;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipmentId")
//    private List<ScreenPort> screenPortList;
    @Basic(optional = false)
    @Column(name = "equipment_id")
    private int equipmentId;
    @Basic(optional = false)
    @Column(name = "position_id")
    private int positionId;
    @Basic(optional = false)
    @Column(name = "map_id")
    private int mapId;

    public ScreenEquipment()
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs(String obs)
    {
        this.obs = obs;
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

    public int getEquipmentId()
    {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public int getPositionId()
    {
        return positionId;
    }

    public void setPositionId(int positionId)
    {
        this.positionId = positionId;
    }

    public int getMapId()
    {
        return mapId;
    }

    public void setMapId(int mapId)
    {
        this.mapId = mapId;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 53 * hash + this.id;
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
        final ScreenEquipment other = (ScreenEquipment) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString()
    {
        return "netmap.Entities.ScreenEquipment[ id=" + id + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.entities;

import java.awt.image.BufferedImage;
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
import netmap.util.Util;

/**
 *
 * @author darlan.ullmann
 */
@Entity
@Table(name = "equipments")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Equipment.findAll", query = "SELECT e FROM Equipment e")
    , @NamedQuery(name = "Equipment.findById", query = "SELECT e FROM Equipment e WHERE e.id = :id")
    , @NamedQuery(name = "Equipment.findByDescription", query = "SELECT e FROM Equipment e WHERE lower(e.description) like lower(:description)")
    , @NamedQuery(name = "Equipment.countByBrand", query = "SELECT count(e.id) FROM Equipment e WHERE e.brandId = :brand")
})
public class Equipment implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "image")
    private byte[] image;
    @Lob
    @Column(name = "info")
    private String info;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipmentId")
//    private List<Port> portList;
    @Basic(optional = false)
    @Column(name = "brand_id")
    private int brandId;
    @Basic(optional = false)
    @Column(name = "equipment_type_id")
    private int equipmentTypeId;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipmentId")
//    private List<ScreenEquipment> screenEquipmentList;

    public Equipment()
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public BufferedImage getImage()
    {
        return Util.bytesToImage(image);
    }

    public void setImage(BufferedImage image)
    {
        this.image = Util.imageToBytes(image);
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

//    @XmlTransient
//    public List<Port> getPortList()
//    {
//        return portList;
//    }
//
//    public void setPortList(List<Port> portList)
//    {
//        this.portList = portList;
//    }

    public int getBrandId()
    {
        return brandId;
    }

    public void setBrandId(int brandId)
    {
        this.brandId = brandId;
    }

    public int getEquipmentTypeId()
    {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(int equipmentTypeId)
    {
        this.equipmentTypeId = equipmentTypeId;
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
        int hash = 7;
        hash = 59 * hash + this.id;
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
        final Equipment other = (Equipment) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.Equipment[ id=" + id + " ]";
    }
    
}

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
@Table(name = "equipment_types")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "EquipmentType.findAll", query = "SELECT e FROM EquipmentType e")
    , @NamedQuery(name = "EquipmentType.findById", query = "SELECT e FROM EquipmentType e WHERE e.id = :id")
    , @NamedQuery(name = "EquipmentType.findByDescription", query = "SELECT e FROM EquipmentType e WHERE e.description = :description")
})
public class EquipmentType implements Serializable
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
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipmentTypeId")
//    private List<Equipment> equipmentList;
    @Lob
    @Column(name = "image")
    private byte[] image;

    public EquipmentType()
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

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

//    @XmlTransient
//    public List<Equipment> getEquipmentList()
//    {
//        return equipmentList;
//    }
//
//    public void setEquipmentList(List<Equipment> equipmentList)
//    {
//        this.equipmentList = equipmentList;
//    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + this.id;
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
        final EquipmentType other = (EquipmentType) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.EquipmentType[ id=" + id + " ]";
    }
    
}

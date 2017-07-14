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
import netmap.util.Util;

/**
 *
 * @author darlan.ullmann
 */
@Entity
@Table(name = "brands")
@NamedQueries(
{
    @NamedQuery(name = "Brand.findAll", query = "SELECT b FROM Brand b")
    , @NamedQuery(name = "Brand.findById", query = "SELECT b FROM Brand b WHERE b.id = :id")
    , @NamedQuery(name = "Brand.findByDescription", query = "SELECT b FROM Brand b WHERE b.description = :description")
    , @NamedQuery(name = "Brand.findByCompany", query = "SELECT b FROM Brand b WHERE b.company = :company")
    , @NamedQuery(name = "Brand.findByWebsite", query = "SELECT b FROM Brand b WHERE b.website = :website")
})
public class Brand implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "company")
    private String company;
    @Column(name = "website")
    private String website;
    @Lob
    @Column(name = "image")
    private byte[] image;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brandId")
//    private List<Equipment> equipmentList;

    public Brand()
    {
    }

    public Brand(Integer id)
    {
        this.id = id;
    }

    public Brand(Integer id, String description)
    {
        this.id = id;
        this.description = description;
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

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public BufferedImage getImage()
    {
        return Util.bytesToImage(image);
    }

    public void setImage(BufferedImage image)
    {
        this.image = Util.imageToBytes(image);
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
        hash = 73 * hash + this.id;
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
        final Brand other = (Brand) obj;
        return this.id == other.id;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.Brand[ id=" + id + " ]";
    }
    
}

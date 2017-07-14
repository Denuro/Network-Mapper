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
@Table(name = "vlans")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Vlan.findAll", query = "SELECT v FROM Vlan v")
    , @NamedQuery(name = "Vlan.findById", query = "SELECT v FROM Vlan v WHERE v.id = :id")
    , @NamedQuery(name = "Vlan.findByDescription", query = "SELECT v FROM Vlan v WHERE v.description = :description")
    , @NamedQuery(name = "Vlan.findByNumber", query = "SELECT v FROM Vlan v WHERE v.number = :number")
})
public class Vlan implements Serializable
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
    @Basic(optional = false)
    @Column(name = "number")
    private int number;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vlans")
//    private List<VlanPort> vlanPortList;

    public Vlan()
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

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

//    @XmlTransient
//    public List<VlanPort> getVlanPortList()
//    {
//        return vlanPortList;
//    }
//
//    public void setVlanPortList(List<VlanPort> vlanPortList)
//    {
//        this.vlanPortList = vlanPortList;
//    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + this.id;
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
        final Vlan other = (Vlan) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.Vlans[ id=" + id + " ]";
    }
    
}

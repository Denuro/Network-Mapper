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
@Table(name = "lags")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Lag.findAll", query = "SELECT l FROM Lag l")
    , @NamedQuery(name = "Lag.findById", query = "SELECT l FROM Lag l WHERE l.id = :id")
    , @NamedQuery(name = "Lag.findByName", query = "SELECT l FROM Lag l WHERE l.name = :name")
})
public class Lag implements Serializable
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
//    @ManyToMany(mappedBy = "lagList")
//    private List<ScreenPort> screenPortList;

    public Lag()
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

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + this.id;
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
        final Lag other = (Lag) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.Lag[ id=" + id + " ]";
    }
    
}

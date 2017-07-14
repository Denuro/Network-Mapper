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
@Table(name = "screen_ports")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ScreenPort.findAll", query = "SELECT s FROM ScreenPort s")
    , @NamedQuery(name = "ScreenPort.findById", query = "SELECT s FROM ScreenPort s WHERE s.id = :id")
    , @NamedQuery(name = "ScreenPort.findByEquipmentId", query = "SELECT s FROM ScreenPort s WHERE s.equipmentId = :id")
})
public class ScreenPort implements Serializable
{
    public enum SWITCHPORTS {
        ACCESS(0, "Access"), TRUNK(1, "Trunk"), GENERAL(2, "General");

        int id;
        String description;
        private SWITCHPORTS(int type, String description)
        {
            this.id = type;
            this.description = description;
        }
        
        public int getId()
        {
            return id;
        }

        @Override
        public String toString()
        {
            return description;
        }
    };
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "type", nullable = false)
    private int type;
    @Basic(optional = false)
    @Column(name = "switchport_type", nullable = false)
    private int switchportType;
    @Basic(optional = false)
    @Column(name = "is_lag", nullable = false)
    private int isLag;
    @Basic(optional = false)
    @Column(name = "equipment_id", nullable = false)
    private int equipmentId;
//    @ManyToMany(mappedBy = "screenPortList")
//    private List<ScreenCable> screenCableList;
//    @JoinTable(name = "lag_ports", joinColumns =
//    {
//        @JoinColumn(name = "port_id", referencedColumnName = "id")
//    }, inverseJoinColumns =
//    {
//        @JoinColumn(name = "lag_id", referencedColumnName = "id")
//    })
//    @ManyToMany
//    private List<Lag> lagList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "screenPort")
//    private List<VlanPort> vlanPortList;
//    @OneToMany(mappedBy = "portId")
//    private List<IpAddress> ipAddressList;

    public ScreenPort()
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

    public int getSwitchportType()
    {
        return switchportType;
    }

    public void setSwitchportType(int switchportType)
    {
        this.switchportType = switchportType;
    }

    public int getIsLag()
    {
        return isLag;
    }

    public void setIsLag(int isLag)
    {
        this.isLag = isLag;
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
//    public List<Lag> getLagList()
//    {
//        return lagList;
//    }
//
//    public void setLagList(List<Lag> lagList)
//    {
//        this.lagList = lagList;
//    }
//
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
//
//    @XmlTransient
//    public List<IpAddress> getIpAddressList()
//    {
//        return ipAddressList;
//    }
//
//    public void setIpAddressList(List<IpAddress> ipAddressList)
//    {
//        this.ipAddressList = ipAddressList;
//    }

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
        final ScreenPort other = (ScreenPort) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "netmap.Entities.ScreenPort[ id=" + id + " ]";
    }
    
}

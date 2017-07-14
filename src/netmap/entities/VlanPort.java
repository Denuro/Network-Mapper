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
@Table(name = "vlan_ports")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "VlanPort.findAll", query = "SELECT v FROM VlanPort v")
    , @NamedQuery(name = "VlanPort.findById", query = "SELECT v FROM VlanPort v WHERE v.id = :id")
    , @NamedQuery(name = "VlanPort.findByVlanId", query = "SELECT v FROM VlanPort v WHERE v.vlanId = :vlanId")
    , @NamedQuery(name = "VlanPort.findByPortId", query = "SELECT v FROM VlanPort v WHERE v.portId = :portId")
    , @NamedQuery(name = "VlanPort.findByType", query = "SELECT v FROM VlanPort v WHERE v.type = :type")
})
public class VlanPort implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "vlan_id", nullable = false)
    private int vlanId;
    @Basic(optional = false)
    @Column(name = "port_id")
    private int portId;
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
//    @JoinColumn(name = "vlan_id", referencedColumnName = "id", insertable = false, updatable = false)
//    @ManyToOne(optional = false)
//    private Vlan vlans;
//    @OneToMany(mappedBy = "vlanPortId")
//    private List<IpAddress> ipAddressList;

    public VlanPort()
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

    public int getVlanId()
    {
        return vlanId;
    }

    public void setVlanId(int vlanId)
    {
        this.vlanId = vlanId;
    }

    public int getPortId()
    {
        return portId;
    }

    public void setPortId(int portId)
    {
        this.portId = portId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 13 * hash + this.id;
        return hash;
    }

//    public Vlan getVlans()
//    {
//        return vlans;
//    }
//
//    public void setVlans(Vlan vlans)
//    {
//        this.vlans = vlans;
//    }
//    @XmlTransient
//    public List<IpAddress> getIpAddressList()
//    {
//        return ipAddressList;
//    }
//
//    public void setIpAddressList(List<IpAddress> ipAddressList)
//    {
//        this.ipAddressList = ipAddressList;
    
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
        final VlanPort other = (VlanPort) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }

//    }
    @Override
    public String toString()
    {
        return "netmap.Entities.VlanPort[ id=" + id + " ]";
    }
    
}

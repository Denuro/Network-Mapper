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
@Table(name = "ip_addresses")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "IpAddress.findAll", query = "SELECT i FROM IpAddress i")
    , @NamedQuery(name = "IpAddress.findById", query = "SELECT i FROM IpAddress i WHERE i.id = :id")
    , @NamedQuery(name = "IpAddress.findByIpv4", query = "SELECT i FROM IpAddress i WHERE i.ipv4 = :ipv4")
    , @NamedQuery(name = "IpAddress.findByIpv6", query = "SELECT i FROM IpAddress i WHERE i.ipv6 = :ipv6")
})
public class IpAddress implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Column(name = "ipv4")
    private String ipv4;
    @Column(name = "ipv6")
    private String ipv6;
    @Basic(optional = false)
    @Column(name = "port_id")
    private int portId;
    @Basic(optional = false)
    @Column(name = "vlan_port_id")
    private int vlanPortId;

    public IpAddress()
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

    public String getIpv4()
    {
        return ipv4;
    }

    public void setIpv4(String ipv4)
    {
        this.ipv4 = ipv4;
    }

    public String getIpv6()
    {
        return ipv6;
    }

    public void setIpv6(String ipv6)
    {
        this.ipv6 = ipv6;
    }

    public int getPortId()
    {
        return portId;
    }

    public void setPortId(int portId)
    {
        this.portId = portId;
    }

    public int getVlanPortId()
    {
        return vlanPortId;
    }

    public void setVlanPortId(int vlanPortId)
    {
        this.vlanPortId = vlanPortId;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
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
        final IpAddress other = (IpAddress) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "netmap.Entities.IpAddress[ id=" + id + " ]";
    }
    
}

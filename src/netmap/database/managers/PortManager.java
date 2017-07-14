/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.database.managers;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import netmap.entities.Equipment;
import netmap.entities.Port;

/**
 * Database Manager for Port Model
 * @author Julia
 */
public class PortManager
{
    private static PortManager instance;

    /**
     * Get the current instance for the PortManager
     * @return 
     */
    public static PortManager getInstance()
    {
        if (instance == null)
        {
            instance = new PortManager();
        }
        
        return instance;
    }

    public PortManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;
    
    /**
     * Get all Port stored in the database related to the Equipment
     * @param equipment
     * @return ports
     */
    public List<Port> get(Equipment equipment)
    {
        TypedQuery<Port> query = em.createNamedQuery("Port.findByEquipment", Port.class);
        query.setParameter("equipment", equipment.getId());
        return query.getResultList();
    }

    /**
     * Save a Port model in the database
     * @param ports
     */
    public void add(List<Port> ports)
    {
        em.getTransaction().begin();
        for (Port port : ports)
        {
            if (em.contains(port))
            {
                em.merge(port);
            }
            else
            {
                em.persist(port);
            }
        }
        em.getTransaction().commit();
    }
    
    /**
     * Delete all ports related to equipment
     * @param equipment
     */
    public void delete(Equipment equipment)
    {
        em.getTransaction().begin();
        for (Port port : get(equipment))
        {
            em.detach(port);
        }
        em.getTransaction().commit();
    }
    
}

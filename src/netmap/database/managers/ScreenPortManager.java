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
import netmap.entities.ScreenPort;

/**
 * Database Manager for EquipmentDisplay Model
 * @author Darlan
 */
public class ScreenPortManager
{
    private static ScreenPortManager instance;
    
    /**
     * Get the current instance for the EquipmentDisplaymanager
     * @return 
     */
    public static ScreenPortManager getInstance()
    {
        if (instance == null)
        {
            instance = new ScreenPortManager();
        }
        
        return instance;
    }

    private ScreenPortManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;

    /**
     * Get an Screen Equipment stored in the database
     * @param id
     * @return brand
     */
    public ScreenPort get(int id)
    {
        return em.find(ScreenPort.class, id);
    }

    /**
     * Get all Screen Equipments stored in the database
     * @return equipments
     */
    public List<ScreenPort> getAll()
    {
        TypedQuery<ScreenPort> query = em.createNamedQuery("ScreenPort.findAll", ScreenPort.class);
        return query.getResultList();
    }

    /**
     * Get all Screen Equipments stored in the database by description
     * @param equipmentId
     * @return equipments
     */
    public List<ScreenPort> getAllByEquipment(int equipmentId)
    {
        TypedQuery<ScreenPort> query = em
                .createNamedQuery("ScreenPort.findByEquipmentId", ScreenPort.class)
                .setParameter("id", equipmentId);
        return query.getResultList();
    }

    /**
     * Save an Screen Equipment model in the database
     * @param screenPort
     */
    public void save(ScreenPort screenPort)
    {
        em.getTransaction().begin();
        if (em.contains(screenPort))
        {
            em.merge(screenPort);
        }
        else
        {
            em.persist(screenPort);
        }
        em.persist(screenPort);
        em.getTransaction().commit();
    }

    /**
     * Delete an equipment type from the database
     * @param screenPort
     */
    public void delete(ScreenPort screenPort)
    {
        em.getTransaction().begin();
        em.remove(screenPort);
        em.getTransaction().commit();
    }
}

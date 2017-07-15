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
import netmap.entities.ScreenCable;

/**
 * Database Manager for EquipmentDisplay Model
 * @author Darlan
 */
public class ScreenCableManager
{
    private static ScreenCableManager instance;
    
    /**
     * Get the current instance for the EquipmentDisplaymanager
     * @return 
     */
    public static ScreenCableManager getInstance()
    {
        if (instance == null)
        {
            instance = new ScreenCableManager();
        }
        
        return instance;
    }

    private ScreenCableManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;

    /**
     * Get an Screen Equipment stored in the database
     * @param id
     * @return brand
     */
    public ScreenCable get(int id)
    {
        return em.find(ScreenCable.class, id);
    }

    /**
     * Get all Screen Equipments stored in the database
     * @return equipments
     */
    public List<ScreenCable> getAll()
    {
        TypedQuery<ScreenCable> query = em.createNamedQuery("ScreenCable.findAll", ScreenCable.class);
        return query.getResultList();
    }

    /**
     * Get all Screen Equipments stored in the database by description
     * @param name
     * @return equipments
     */
    public List<ScreenCable> getAllByDescription(String name)
    {
        TypedQuery<ScreenCable> query = em
                .createNamedQuery("ScreenCable.findByName", ScreenCable.class)
                .setParameter("name", name);
        return query.getResultList();
    }

    /**
     * Save an Screen Equipment model in the database
     * @param screenEquipment
     */
    public void save(ScreenCable screenEquipment)
    {
        em.getTransaction().begin();
        if (em.contains(screenEquipment))
        {
            em.merge(screenEquipment);
        }
        else
        {
            em.persist(screenEquipment);
        }
        em.getTransaction().commit();
    }

    /**
     * Delete an equipment type from the database
     * @param screenEquipment
     */
    public void delete(ScreenCable screenEquipment)
    {
        em.getTransaction().begin();
        em.remove(screenEquipment);
        em.getTransaction().commit();
    }
}

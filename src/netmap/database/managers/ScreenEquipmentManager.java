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
import netmap.entities.ScreenEquipment;

/**
 * Database Manager for EquipmentDisplay Model
 * @author Darlan
 */
public class ScreenEquipmentManager
{
    private static ScreenEquipmentManager instance;
    
    /**
     * Get the current instance for the EquipmentDisplaymanager
     * @return 
     */
    public static ScreenEquipmentManager getInstance()
    {
        if (instance == null)
        {
            instance = new ScreenEquipmentManager();
        }
        
        return instance;
    }

    private ScreenEquipmentManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;

    /**
     * Get an Screen Equipment stored in the database
     * @param id
     * @return brand
     */
    public ScreenEquipment get(int id)
    {
        return em.find(ScreenEquipment.class, id);
    }

    /**
     * Get all Screen Equipments stored in the database
     * @return equipments
     */
    public List<ScreenEquipment> getAll()
    {
        TypedQuery<ScreenEquipment> query = em.createNamedQuery("ScreenEquipment.findAll", ScreenEquipment.class);
        return query.getResultList();
    }

    /**
     * Get all Screen Equipments stored in the database by description
     * @param name
     * @return equipments
     */
    public List<ScreenEquipment> getAllByDescription(String name)
    {
        TypedQuery<ScreenEquipment> query = em
                .createNamedQuery("ScreenEquipment.findByName", ScreenEquipment.class)
                .setParameter("name", name);
        return query.getResultList();
    }

    /**
     * Save an Screen Equipment model in the database
     * @param screenEquipment
     */
    public void save(ScreenEquipment screenEquipment)
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
    public void delete(ScreenEquipment screenEquipment)
    {
        em.getTransaction().begin();
        em.remove(screenEquipment);
        em.getTransaction().commit();
    }
}

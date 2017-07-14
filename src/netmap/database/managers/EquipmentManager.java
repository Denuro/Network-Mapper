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
import netmap.entities.Brand;
import netmap.entities.Equipment;

/**
 * Database Manager for Equipment Model
 * @author Julia
 */
public class EquipmentManager
{

    private static EquipmentManager instance;

    /**
     * Get the current instance for the EquipmentManager
     * @return 
     */
    public static EquipmentManager getInstance()
    {
        if (instance == null)
        {
            instance = new EquipmentManager();
        }

        return instance;
    }

    private EquipmentManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;

    /**
     * Get an Equipment stored in the database
     * @param id
     * @return brand
     */
    public Equipment get(int id)
    {
        return em.find(Equipment.class, id);
    }

    /**
     * Get all Equipments stored in the database
     * @return equipments
     */
    public List<Equipment> getAll()
    {
        TypedQuery<Equipment> query = em.createNamedQuery("Equipment.findAll", Equipment.class);
        return query.getResultList();
    }

    /**
     * Get all equipments in database matching the search param
     * @param search
     * @return equipments
     */
    public List<Equipment> getAllByDescription(String search)
    {
        TypedQuery<Equipment> query = em
                .createNamedQuery("Equipment.findByDescription", Equipment.class)
                .setParameter("description", search);
        return query.getResultList();
    }

    /**
     * Get the count of equipments in database matching the brand
     * @param brand
     * @return count
     */
    public int getCountByBrand(Brand brand)
    {
        TypedQuery<Integer> query = em.createNamedQuery("Equipment.countByBrand", Integer.class);
        query.setParameter("brand", brand.getId());
        return query.getSingleResult();
    }

    /**
     * Save an Equipment model in the database
     * @param equipment
     */
    public void save(Equipment equipment)
    {
        em.getTransaction().begin();
        if (em.contains(equipment))
        {
            em.merge(equipment);
        }
        else
        {
            em.persist(equipment);
        }
        em.getTransaction().commit();
    }

    /**
     * Delete an equipment from the database
     * @param equipment
     */
    public void delete(Equipment equipment)
    {
        em.getTransaction().begin();
        em.detach(equipment);
        em.getTransaction().commit();
    }
}

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
import netmap.entities.EquipmentType;

/**
 * Database Manager for EquipmentType Model
 * @author Julia
 */
public class EquipmentTypeManager
{
    private static EquipmentTypeManager instance;

    /**
     * Get the current instance for the EquipmentManager
     * @return 
     */
    public static EquipmentTypeManager getInstance()
    {
        if (instance == null)
        {
            instance = new EquipmentTypeManager();
        }

        return instance;
    }

    private EquipmentTypeManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;

    /**
     * Get an Equipment Type stored in the database
     * @param id
     * @return brand
     */
    public EquipmentType get(int id)
    {
        return em.find(EquipmentType.class, id);
    }

    /**
     * Get all Equipment types stored in the database
     * @return equipments
     */
    public List<EquipmentType> getAll()
    {
        TypedQuery<EquipmentType> query = em.createNamedQuery("EquipmentType.findAll", EquipmentType.class);
        return query.getResultList();
    }

    /**
     * Get all Equipment types stored in the database by description
     * @param description
     * @return equipments
     */
    public List<EquipmentType> getAllByDescription(String description)
    {
        TypedQuery<EquipmentType> query = em
                .createNamedQuery("EquipmentType.findByDescription", EquipmentType.class)
                .setParameter("description", description);
        return query.getResultList();
    }

    /**
     * Save an Equipment Type model in the database
     * @param equipmentType
     */
    public void save(EquipmentType equipmentType)
    {
        em.getTransaction().begin();
        if (em.contains(equipmentType))
        {
            em.merge(equipmentType);
        }
        else
        {
            em.persist(equipmentType);
        }
        em.getTransaction().commit();
    }

    /**
     * Delete an equipment type from the database
     * @param equipmentType
     */
    public void delete(EquipmentType equipmentType)
    {
        em.getTransaction().begin();
        em.remove(equipmentType);
        em.getTransaction().commit();
    }
}

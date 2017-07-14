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
import netmap.entities.Map;

/**
 * Database Manager for Map Model
 * @author Darlan
 */
public class MapManager
{
    private static MapManager instance;
    
    /**
     * Get the current instance for the Map Manager
     * @return 
     */
    public static MapManager getInstance()
    {
        if (instance == null)
        {
            instance = new MapManager();
        }
        
        return instance;
    }

    private MapManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;

    /**
     * Get an Map stored in the database
     * @param id
     * @return brand
     */
    public Map get(int id)
    {
        return em.find(Map.class, id);
    }

    /**
     * Get all Maps stored in the database
     * @return maps
     */
    public List<Map> getAll()
    {
        TypedQuery<Map> query = em.createNamedQuery("Map.findAll", Map.class);
        return query.getResultList();
    }

    /**
     * Save an Map model in the database
     * @param screenMap
     */
    public void save(Map screenMap)
    {
        em.getTransaction().begin();
        if (em.contains(screenMap))
        {
            em.merge(screenMap);
        }
        else
        {
            em.persist(screenMap);
        }
        em.getTransaction().commit();
    }

    /**
     * Delete an map type from the database
     * @param screenMap
     */
    public void delete(Map screenMap)
    {
        em.getTransaction().begin();
        em.remove(screenMap);
        em.getTransaction().commit();
    }
}

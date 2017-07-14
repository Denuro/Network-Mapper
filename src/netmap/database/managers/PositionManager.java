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
import netmap.entities.Position;

/**
 * Database Manager for Position Model
 * @author Darlan
 */
public class PositionManager
{
    private static PositionManager instance;
    
    /**
     * Get the current instance for the PositionDisplaymanager
     * @return 
     */
    public static PositionManager getInstance()
    {
        if (instance == null)
        {
            instance = new PositionManager();
        }
        
        return instance;
    }

    private PositionManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;

    /**
     * Get an Screen Position stored in the database
     * @param id
     * @return brand
     */
    public Position get(int id)
    {
        return em.find(Position.class, id);
    }

    /**
     * Get all Screen Positions stored in the database
     * @return positions
     */
    public List<Position> getAll()
    {
        TypedQuery<Position> query = em.createNamedQuery("Position.findAll", Position.class);
        return query.getResultList();
    }

    /**
     * Save an Screen Position model in the database
     * @param screenPosition
     */
    public void save(Position screenPosition)
    {
        em.getTransaction().begin();
        if (em.contains(screenPosition))
        {
            em.merge(screenPosition);
        }
        else
        {
            em.persist(screenPosition);
        }
        em.getTransaction().commit();
    }

    /**
     * Delete an position type from the database
     * @param screenPosition
     */
    public void delete(Position screenPosition)
    {
        em.getTransaction().begin();
        em.remove(screenPosition);
        em.getTransaction().commit();
    }
}

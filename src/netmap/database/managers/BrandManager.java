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

/**
 * Database Manager for Brand Model
 * @author Julia
 */
public class BrandManager
{
    private static BrandManager instance;
    
    /**
     * Get the current instance for the BrandManager
     * @return 
     */
    public static BrandManager getInstance()
    {
        if (instance == null)
        {
            instance = new BrandManager();
        }
        
        return instance;
    }

    private BrandManager()
    {
        em = Persistence.createEntityManagerFactory("network_mapper").createEntityManager();
    }
    
    private final EntityManager em;
    
    /**
     * Get a Brand stored in the database
     * @param id
     * @return brand
     */
    public Brand get(int id)
    {
        return em.find(Brand.class, id);
    }
    
    /**
     * Get all Brands stored in the database
     * @return brands
     */
    public List<Brand> getAll()
    {
        TypedQuery<Brand> query = em.createNamedQuery("Brand.findAll", Brand.class);
        return query.getResultList();
    }
    
    /**
     * Get all brands in database matching the search param
     * @param search
     * @return brands
     */
    public List<Brand> getAllByDescription(String search)
    {
        TypedQuery<Brand> query = em
                .createNamedQuery("Brand.findByDescription", Brand.class)
                .setParameter("description", search);
        return query.getResultList();
    }
    
    /**
     * Save a Brand model in the database
     * @param brand 
     */
    public void save(Brand brand)
    {
        em.getTransaction().begin();
        if (em.contains(brand))
        {
            em.merge(brand);
        }
        else
        {
            em.persist(brand);
        }
        em.getTransaction().commit();
    }
    
    /**
     * Delete a brand from the database
     * @param brand 
     */
    public void delete(Brand brand)
    {
        em.getTransaction().begin();
        em.remove(brand);
        em.getTransaction().commit();
    }
}

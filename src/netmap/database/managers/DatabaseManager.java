/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.database.managers;

import netmap.database.Database;
import java.sql.PreparedStatement;
import netmap.util.Util;

/**
 * Database Manager to start the SQLite database
 * @author Julia
 */
public class DatabaseManager
{
    private static DatabaseManager instance;
    
    public static DatabaseManager getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseManager();
        }
        
        return instance;
    }
    
    /**
     * Create the tables for the SQLite Database
     */
    public void initializeDatabase()
    {
        try (Database db = Database.getInstance())
        {
            PreparedStatement ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `brands` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `description` VARCHAR(200) NOT NULL," +
                    "  `company` VARCHAR(200),"+
                    "  `website` VARCHAR(200),"+
                    "  `image` BLOB NULL" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `equipment_types` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `description` VARCHAR(200) NOT NULL" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "INSERT INTO `equipment_types` (`description`) VALUES " +
                    "  (\"Servidor\"), " + 
                    "  (\"Roteador\"), " + 
                    "  (\"Switch\")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `equipments` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `equipment_type_id` INTEGER NOT NULL," +
                    "  `brand_id` INTEGER NOT NULL," +
                    "  `description` VARCHAR(200) NOT NULL," +
                    "  `image` BLOB NULL," +
                    "  `info` TEXT NULL," +
                    "  FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`)" +
                    "  FOREIGN KEY (`equipment_type_id`) REFERENCES `equipment_types` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `ports` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `equipment_id` INTEGER NOT NULL," +
                    "  `type` INTEGER NOT NULL," +
                    "  `speed` INTEGER NOT NULL," +
                    "  FOREIGN KEY (`equipment_id`) REFERENCES `equipments` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `position` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `x` INTEGER NOT NULL," +
                    "  `y` INTEGER NOT NULL" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `maps` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `name` VARCHAR(200) NOT NULL," +
                    "  `description` TEXT NULL" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `screen_equipments` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `map_id` INTEGER NOT NULL," +
                    "  `equipment_id` INTEGER NOT NULL," +
                    "  `position_id` INTEGER NOT NULL," +
                    "  `name` VARCHAR(200) NOT NULL," +
                    "  `obs` TEXT NULL," +
                    "  FOREIGN KEY (`map_id`) REFERENCES `maps` (`id`)," +
                    "  FOREIGN KEY (`equipment_id`) REFERENCES `equipments` (`id`)," +
                    "  FOREIGN KEY (`position_id`) REFERENCES `position` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `screen_ports` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `equipment_id` INTEGER NOT NULL," +
                    "  `type` INTEGER NOT NULL," +
                    "  `is_lag` INTEGER NOT NULL," +
                    "  FOREIGN KEY (`equipment_id`) REFERENCES `screen_equipments` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `screen_cables` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `port_1_id` INTEGER NOT NULL," +
                    "  `port_2_id` INTEGER NOT NULL," +
                    "  `cable_id` INTEGER NOT NULL," +
                    "  `type` INTEGER NOT NULL," +
                    "  `speed` INTEGER NOT NULL," +
                    "  FOREIGN KEY (`port_1_id`) REFERENCES `screen_ports` (`id`)," +
                    "  FOREIGN KEY (`port_2_id`) REFERENCES `screen_ports` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `cable_positions` (" +
                    "  `cable_id` INTEGER NOT NULL," +
                    "  `position_id` INTEGER NOT NULL," +
                    "  FOREIGN KEY (`cable_id`) REFERENCES `screen_cables` (`id`)," +
                    "  FOREIGN KEY (`position_id`) REFERENCES `position` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `lags` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `lag_ports` (" +
                    "  `lag_id` INTEGER NOT NULL," +
                    "  `port_id` INTEGER NOT NULL," +
                    "  FOREIGN KEY (`lag_id`) REFERENCES `lags` (`id`)," +
                    "  FOREIGN KEY (`port_id`) REFERENCES `screen_ports` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `vlans` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `description` VARCHAR(80) NOT NULL," +
                    "  `number` INTEGER NOT NULL" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `vlan_ports` (" +
                    "  `id` INTEGER NOT NULL," +
                    "  `vlan_id` INTEGER NOT NULL," +
                    "  `port_id` INTEGER NOT NULL," +
                    "  `type` INTEGER NOT NULL," +
                    "  FOREIGN KEY (`vlan_id`) REFERENCES `vlans` (`id`)," +
                    "  FOREIGN KEY (`port_id`) REFERENCES `screen_ports` (`id`)" +
                    ")");
            ps.executeUpdate();
            
            ps = db.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `ip_adresses` (" +
                    "  `id` INTEGER PRIMARY KEY NOT NULL," +
                    "  `vlan_port_id` INTEGER NULL," +
                    "  `port_id` INTEGER NULL," +
                    "  `ipv4` VARCHAR(15) NULL," +
                    "  `ipv6` VARCHAR(45) NULL," +
                    "  FOREIGN KEY (`vlan_port_id`) REFERENCES `vlan_ports` (`id`)," +
                    "  FOREIGN KEY (`port_id`) REFERENCES `screen_ports` (`id`)" +
                    ")");
            ps.executeUpdate();
        }
        catch (Exception e)
        {
            Util.handleException(e);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Save and load properties from a file
 * @author Darlan
 */
public class Properties
{
    private static Properties instance;
    
    public static Properties getInstance()
    {
        if (instance == null)
        {
            instance = new Properties();
        }
        
        return instance;
    }
    
    private final File file;
    private final java.util.Properties props;

    private Properties()
    {
        file = new File(Util.getApplicationDataFolder()+"user.properties");
        props = new java.util.Properties();
        
        if (file.exists())
        {
            load();
        }
    }
    
    private void load()
    {
        try
        {
            props.load(new FileReader(file));
        }
        catch (Exception e)
        {
            Util.handleException(e);
        }
    }
    
    private void save()
    {
        try
        {
            props.store(new FileWriter(file), "Properties file for Network Manager\nDo not edit by hand");
        }
        catch (Exception e)
        {
            Util.handleException(e);
        }
    }
    
    public int getInt(String key, int def)
    {
        int ret = 0;
        try
        {
            ret = Integer.parseInt(props.getProperty(key));
        }
        catch (Exception e)
        {
            ret = def;
        }
        return ret;
    }
    
    public String getString(String key, String def)
    {
        return props.getProperty(key, def);
    }
    
    public boolean getBoolean(String key, boolean def)
    {
        String property = props.getProperty(key, "keynotfound");
        return property.equals("keynotfound") ? def : property.equals("true");
    }
    
    public void setInt(String key, int value)
    {
        props.setProperty(key, String.valueOf(value));
        save();
    }
    
    public void setString(String key, String value)
    {
        props.setProperty(key, value);
        save();
    }
    
    public void setBoolean(String key, boolean value)
    {
        props.setProperty(key, value ? "true" : "false");
        save();
    }
}

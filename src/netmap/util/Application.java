/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.util;

import javax.swing.UIManager;
import netmap.database.managers.MapManager;
import netmap.entities.Map;
import netmap.frames.MainFrame;

/**
 * Application manage application related parameters
 * @author darlan.ullmann
 */
public class Application
{
    public static int MAJOR_VERSION = 0;
    public static int MINOR_VERSION = 4;
    public static String DATABASE_FILENAME = "netmap.db";
    
    private static Application instance;
    
    /**
     * Get the current instance of the Application
     * @return 
     */
    public static Application getInstance()
    {
        if ( instance == null )
        {
            instance = new Application();
        }
        
        return instance;
    }
    
    private MainFrame mainFrame;

    private Application()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            if (MapManager.getInstance().get(1) == null)
            {
                Map map = new Map(1, "Mapa");
                MapManager.getInstance().save(map);
            }
            
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Application.getInstance().getMainFrame().shutdown();
                }
            }));
        }
        catch (Exception e)
        {
            Util.handleException(e);
        }
    }
    
    /**
     * Set the Main Frame of the application
     * @param frame 
     */
    public void setMainFrame(MainFrame frame)
    {
        mainFrame = frame;
    }
    
    /**
     * Returns the main frame of the application
     * @return 
     */
    public MainFrame getMainFrame()
    {
        return mainFrame;
    }
}

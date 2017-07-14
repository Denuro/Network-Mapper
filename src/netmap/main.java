/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap;

import netmap.frames.MainFrame;
import netmap.util.Application;
import javax.swing.JFrame;

/**
 * main class of the application, start things up!
 * @author darlan.ullmann
 */
public class main
{
    /*
        Icons designed by Madebyoliver from Flaticon - http://www.flaticon.com
        Image scaling by Riyad Kalla -  https://github.com/rkalla/imgscalr
    */

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args )
    {   
        MainFrame frame = new MainFrame();
        
        Application.getInstance().setMainFrame( frame );
        
        frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        frame.setVisible( true );
    }
    
}

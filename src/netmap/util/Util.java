/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.util;

import netmap.frames.ErrorFrame;
import netmap.util.imgscalr.Scalr;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

/**
 * Utilities for the application
 * @author darlan.ullmann
 */
public class Util
{

    private static final String[] IMG_EXTENSIONS =
    {
        "png", "jpg", "gif", "jpeg"
    };

    /** 
     * Return a new FileFilter instance for image files
     * @return 
     */
    public static FileFilter getImageFileFilter()
    {
        return new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                {
                    return true;
                }
                else
                {
                    String fileName = f.getName().toLowerCase();
                    for (String extension : IMG_EXTENSIONS)
                    {
                        int i = f.getName().lastIndexOf('.');
                        if (i > 0 && i < fileName.length() - 1 && fileName.substring(i + 1).equals(extension))
                        {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public String getDescription()
            {
                return "Arquivos de imagem";
            }
        };
    }

    /**
     * enum for the Operational System
     */
    public static enum OS
    {

        Windows, Mac, Linux, Unknown
    };

    /**
     * Get the Operational System of the opened application
     * @return 
     */
    public static OS getOsType()
    {
        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.contains("windows"))
        {
            return OS.Windows;
        }
        else if (osname.contains("nux"))
        {
            return OS.Linux;
        }
        else if (osname.contains("mac"))
        {
            return OS.Mac;
        }

        return OS.Unknown;
    }

    /**
     * Get the data folder used by the application, Operational System specific
     * @return 
     */
    public static String getApplicationDataFolder()
    {
        String folder = "";
        switch (Util.getOsType())
        {
            case Windows:
                folder = System.getenv("APPDATA") + File.separator + "Network Mapper" + File.separator;
                break;
            case Linux:
                folder = System.getProperty("user.home") + File.separator + ".network_mapper" + File.separator;
                break;
            case Mac:
                folder = System.getProperty("user.home") + File.separator
                        + "Library" + File.separator + "Application" + File.separator
                        + "Network Mapper" + File.separator;
                break;
        }

        File folderFile = new File(folder);
        if (!folderFile.exists())
        {
            folderFile.mkdirs();
        }

        return folder;
    }

    /**
     * Get the database folder used by the SQLite database
     * @return 
     */
    public static String getDatabaseFolder()
    {
        return "";
    }

    /**
     * Read an image from a url
     * @param url
     * @return image
     */
    public static BufferedImage readImage(URL url)
    {
        try
        {
            return ImageIO.read(url);
        }
        catch (IOException e)
        {
            handleException(e);
        }
        
        return null;
    }

    /**
     * Read an image from a file
     * @param f
     * @return image
     */
    public static BufferedImage readImage(File f)
    {
        try
        {
            return readImage(f.toURI().toURL());
        }
        catch (MalformedURLException e)
        {
            Util.handleException(e);
        }
        
        return null;
    }

    /**
     * Resize an image using Scal, using height or width, what fits better
     * @param img
     * @param size
     * @return resized image
     */
    public static BufferedImage resizeImage(BufferedImage img, int size)
    {
        return Scalr.resize(img, size, Scalr.OP_ANTIALIAS);
    }

    /**
     * Resize an image using Scal, using width only
     * @param img
     * @param width
     * @return resized image
     */
    public static BufferedImage resizeImageWidth(BufferedImage img, int width)
    {
        return Scalr.resize(img, Scalr.Mode.FIT_TO_WIDTH, width, Scalr.OP_ANTIALIAS);
    }

    /**
     * Turn a BufferedImage into an byte array
     * @param img
     * @return image
     */
    public static byte[] imageToBytes(BufferedImage img)
    {
        byte[] ret = null;
        if (img != null)
        {
            try
            {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
                {
                    ImageIO.write(img, "png", baos);
                    baos.flush();
                    ret = baos.toByteArray();
                }
            }
            catch (IOException e)
            {
                handleException(e);
            }
        }
        return ret;
    }

    /**
     * Turns a byte array into an BufferedImage
     * @param bytes
     * @return image
     */
    public static BufferedImage bytesToImage(byte[] bytes)
    {
        BufferedImage ret = null;
        try
        {
            if (bytes != null)
            {
                InputStream in = new ByteArrayInputStream(bytes);
                ret = ImageIO.read(in);
            }
        }
        catch (IOException e)
        {
            handleException(e);
        }
        return ret;
    }

    /**
     * Handles an application exception
     * @param e 
     */
    public static void handleException(Exception e)
    {
        handleException(null, e);
    }

    /**
     * Handles an application exception
     * @param title
     * @param e 
     */
    public static void handleException(String title, Exception e)
    {
        e.printStackTrace();

        ErrorFrame ef = new ErrorFrame(title, e);
        ef.setLocationRelativeTo(null);
        ef.setModal(true);
        ef.setVisible(true);
    }

    /**
     * Get saved FileChooser directory
     * @return folder path
     */
    public static String getFileChooserDirectory()
    {
        return Properties.getInstance().getString("filechooser.directory", "");
    }
    
    /**
     * Save the selected FileChooser file directory
     * @param f 
     */
    public static void saveFileChooserDirectory(File f)
    {
        if (!f.isDirectory())
        {
            f = f.getParentFile();
        }
        
        Properties.getInstance().setString("filechooser.directory", f.getAbsolutePath());
    }
}

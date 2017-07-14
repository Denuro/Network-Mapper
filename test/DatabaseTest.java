
import netmap.database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author darlan.ullmann
 */
public class DatabaseTest
{

    public static void main(String args[])
    {
        try (Database db = Database.getInstance())
        {
            PreparedStatement ps = db.prepareStatement("select * from brands");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next())
            {
                System.out.println(rs.getInt("id"));
                try (Database db1 = Database.getInstance())
                {
                    PreparedStatement ps1 = db1.prepareStatement("select * from brands where id = ?");
                    ps1.setInt(1, rs.getInt("id"));
                    ResultSet rs1 = ps1.executeQuery();

                    if (rs1.next())
                    {
                        System.out.println(rs1.getString("description"));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

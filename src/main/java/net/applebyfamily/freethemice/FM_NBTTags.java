package net.applebyfamily.freethemice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class FM_NBTTags {

	public String FileName;
	public String Cata;
	public FM_NBTTags(String tCata) {
		Cata = tCata;
	}
	private String checkPath(String Path)
	{
		String FileSeparator = FinderMod.instance.MC.mcDataDir.separator;
        String ASP = Path;
		if (ASP.endsWith(".") == true)
		{
			ASP = ASP.substring(0, ASP.length() - 1);
		}
		if (ASP.endsWith(FileSeparator) == false)
		{
			ASP = ASP + FileSeparator;			
		}
		return ASP;
	}
	public NBTTagCompound readNBTSettings()
	{ 
		String MainLocation = getMainLocation();
		
		NBTTagCompound par1NBTTagCompoundSettings;
        File var1 = new File(MainLocation,  FileName);       
        if (var1.exists())
        {
            try
            {       		
        		
            	par1NBTTagCompoundSettings = CompressedStreamTools.readCompressed(new FileInputStream(var1));
            	
            	return par1NBTTagCompoundSettings.getCompoundTag(Cata);
            }
            catch (Exception var5)
            {
                var5.printStackTrace();
            }
        }
        else
        {        	
            NBTTagCompound var4 = new NBTTagCompound();
            saveNBTSettings(var4);                      
            return var4;
        }
        return null;	
	}

	public void saveNBTSettings(NBTTagCompound var4)
	{
		String MainLocation = getMainLocation();
		
		NBTTagCompound par1NBTTagCompoundSettings = new NBTTagCompound();
        try
        {
        	par1NBTTagCompoundSettings.setTag(Cata, var4);
            File var3 = new File(MainLocation,  FileName);            
            CompressedStreamTools.writeCompressed(par1NBTTagCompoundSettings, new FileOutputStream(var3));
            
        }
        catch (Exception var5)
        {
            
        }
	}	
	
	private String getMainLocation() {
		String FileSeparator = FinderMod.instance.MC.mcDataDir.separator;
		String MainLocation = checkPath(Minecraft.getMinecraft().mcDataDir.getAbsolutePath());
        File versionsDir = new File(MainLocation, "FinderMod");
        if (versionsDir.exists() == false)
        {
        	versionsDir.mkdir();
        }
		MainLocation = checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}	
    
	
}
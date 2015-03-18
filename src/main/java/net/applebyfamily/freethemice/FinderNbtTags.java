/*
 * Findermod. Finds stuff.
 * Copyright (C) 2013-2015 freethemice
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.applebyfamily.freethemice;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FinderNbtTags {

	public String fileName;
	public String cata;
	public FinderNbtTags(String cata) {
		this.cata = cata;
	}
	private String checkPath(String Path)
	{
		String FileSeparator = FinderMod.instance.minecraft.mcDataDir.separator;
        String ASP = Path;
		if (ASP.endsWith("."))
		{
			ASP = ASP.substring(0, ASP.length() - 1);
		}
		if (!ASP.endsWith(FileSeparator))
		{
			ASP = ASP + FileSeparator;			
		}
		return ASP;
	}
	public NBTTagCompound readNBTSettings()
	{ 
		String MainLocation = getMainLocation();
		
		NBTTagCompound par1NBTTagCompoundSettings;
        File var1 = new File(MainLocation,  fileName);
        if (var1.exists())
        {
            try
            {       		
        		
            	par1NBTTagCompoundSettings = CompressedStreamTools.readCompressed(new FileInputStream(var1));
            	
            	return par1NBTTagCompoundSettings.getCompoundTag(cata);
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
        try {
        	par1NBTTagCompoundSettings.setTag(cata, var4);
            File var3 = new File(MainLocation, fileName);
            CompressedStreamTools.writeCompressed(par1NBTTagCompoundSettings, new FileOutputStream(var3));
            
        } catch (Exception expected) {
        }
	}	
	
	private String getMainLocation() {
		String FileSeparator = FinderMod.instance.minecraft.mcDataDir.separator;
		String MainLocation = checkPath(Minecraft.getMinecraft().mcDataDir.getAbsolutePath());
        File versionsDir = new File(MainLocation, "config/FinderMod");
        if (versionsDir.exists() == false)
        {
        	versionsDir.mkdir();
        }
		MainLocation = checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}	
    
	
}
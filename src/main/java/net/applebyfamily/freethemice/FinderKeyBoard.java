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

import java.util.Timer;
import java.util.TimerTask;

import javax.vecmath.Point3d;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;

import org.lwjgl.input.Keyboard;

public class FinderKeyBoard {
	
	public boolean searchKeyPressed, waypointKeyPressed;
	public Timer GameTick;
	public FinderKeyBoard() {
		searchKeyPressed = false;
		waypointKeyPressed = false;
	   	GameTick = new Timer();
    	GameTick.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				//System.out.println("testset");
				if (FinderMod.instance.loaded == true)
				{
					listener();
				}
				
			}
		},10,10);
	}
	
	public void listener()
	{	
		try {
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforB[0]) == true && searchKeyPressed == false && FinderMod.instance.myGuiHandler._MyGui._visible == false && FinderMod.instance.minecraft.currentScreen == null)
			{
				searchKeyPressed = true;
				MovingObjectPosition pos = FinderMod.minecraft.objectMouseOver;
				Block tmpPOS = FinderMod.minecraft.theWorld.getChunkFromBlockCoords(pos.getBlockPos()).getBlock(pos.getBlockPos());
				String blockID = tmpPOS.getUnlocalizedName().replace("item.", "").replace("tile.", "");
				FinderMod.instance.myGuiHandler._MyGui.textfield.setText(blockID + "");
				FinderMod.instance.myGuiHandler._MyGui._takeInput = false;
				FinderMod.instance.myGuiHandler._MyGui._visible = true;
				FinderMod.instance.thePlayer.openGui(FinderMod.instance, 0, FinderMod.instance.theWorld, (int)FinderMod.instance.thePlayer.posX,  (int)FinderMod.instance.thePlayer.posY,  (int)FinderMod.instance.thePlayer.posZ);
							
			}
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforB[0]) == false && searchKeyPressed == true)
			{
				FinderMod.instance.myGuiHandler._MyGui._takeInput = true;
				searchKeyPressed = false;
			}
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforN[0]) == true && waypointKeyPressed == false && FinderMod.instance.myGuiHandler._MyGui._visible == false && FinderMod.instance.minecraft.currentScreen == null)
			{				
				waypointKeyPressed = true;				
				Point3d testTMP = new Point3d(FinderMod.instance.thePlayer.posX, FinderMod.instance.thePlayer.posY, FinderMod.instance.thePlayer.posZ);
				
				for(int i = 0; i < FinderMod.instance.eventManager.Waypoints.size(); i ++)
				{
					if (FinderMod.instance.eventManager.Waypoints.get(i).distance(testTMP) < 5)
					{
						FinderMod.instance.eventManager.Waypoints.remove(i);
						 
						FinderMod.instance.sendChatMessage("Waypoint deleted, search for 'waypoints' to see waypoints!");
						saveWaypoints();
						return;
					}
				}
				
				//MinecraftServer.getServer().
				FinderMod.instance.eventManager.Waypoints.add(new FinderPoint3d(FinderMod.instance.thePlayer.posX, FinderMod.instance.thePlayer.posY, FinderMod.instance.thePlayer.posZ));
				FinderMod.instance.sendChatMessage("Waypoint added, search for 'waypoints' to see waypoints!");				
				saveWaypoints();
			}
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforN[0]) == false && waypointKeyPressed == true)
			{
				waypointKeyPressed = false;
			}
		} catch (Exception e) {
			//System.out.println("Keyboard Crashed");
		}
	}

	private void saveWaypoints() {
		NBTTagCompound par1NBTTagCompoundSettings = new NBTTagCompound();
		par1NBTTagCompoundSettings.setInteger("max", FinderMod.instance.eventManager.Waypoints.size());
		for(int i = 0; i < FinderMod.instance.eventManager.Waypoints.size(); i ++)
		{
			par1NBTTagCompoundSettings.setDouble(i + "_point_X",  FinderMod.instance.eventManager.Waypoints.get(i).x);
			par1NBTTagCompoundSettings.setDouble(i + "_point_Y",  FinderMod.instance.eventManager.Waypoints.get(i).y);
			par1NBTTagCompoundSettings.setDouble(i + "_point_Z",  FinderMod.instance.eventManager.Waypoints.get(i).z);
		}
		FinderMod.instance.eventManager.MyNBTWorldSaves.fileName = FinderMod.instance.eventManager.getFileName();
		FinderMod.instance.eventManager.MyNBTWorldSaves.saveNBTSettings(par1NBTTagCompoundSettings);
	}
}

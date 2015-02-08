package net.applebyfamily.freethemice;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.vecmath.Point3d;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;

import org.lwjgl.input.Keyboard;

public class FM_Keyboard {
	
	public boolean _iskeydown_B, _iskeydown_N;
	public Timer GameTick;
	public FM_Keyboard() {		
		_iskeydown_B = false;
		_iskeydown_N = false;
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
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforB[0]) == true && _iskeydown_B == false && FinderMod.instance.myGuiHandeler._MyGui._visible == false && FinderMod.instance.MC.currentScreen == null)
			{
				_iskeydown_B = true;
				MovingObjectPosition pos = FinderMod.MC.objectMouseOver;
				Block tmpPOS = FinderMod.MC.theWorld.getChunkFromBlockCoords(pos.getBlockPos()).getBlock(pos.getBlockPos());
				String blockID = tmpPOS.getUnlocalizedName().replace("item.", "").replace("tile.", "");
				FinderMod.instance.myGuiHandeler._MyGui.textfield.setText(blockID + "");
				FinderMod.instance.myGuiHandeler._MyGui._takeInput = false;
				FinderMod.instance.myGuiHandeler._MyGui._visible = true;
				FinderMod.instance.thePlayer.openGui(FinderMod.instance, 0, FinderMod.instance.theWorld, (int)FinderMod.instance.thePlayer.posX,  (int)FinderMod.instance.thePlayer.posY,  (int)FinderMod.instance.thePlayer.posZ);
							
			}
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforB[0]) == false && _iskeydown_B == true)
			{
				FinderMod.instance.myGuiHandeler._MyGui._takeInput = true;
				_iskeydown_B = false;
			}
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforN[0]) == true && _iskeydown_N == false && FinderMod.instance.myGuiHandeler._MyGui._visible == false && FinderMod.instance.MC.currentScreen == null)
			{				
				_iskeydown_N = true;				
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
				FinderMod.instance.eventManager.Waypoints.add(new FM_Point3d(FinderMod.instance.thePlayer.posX, FinderMod.instance.thePlayer.posY, FinderMod.instance.thePlayer.posZ));
				FinderMod.instance.sendChatMessage("Waypoint added, search for 'waypoints' to see waypoints!");				
				saveWaypoints();
			}
			if (Keyboard.isKeyDown(FinderMod.instance.NumberforN[0]) == false && _iskeydown_N == true)
			{
				_iskeydown_N = false;
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
		FinderMod.instance.eventManager.MyNBTWorldSaves.FileName = FinderMod.instance.eventManager.getFileName();
		FinderMod.instance.eventManager.MyNBTWorldSaves.saveNBTSettings(par1NBTTagCompoundSettings);
	}
}

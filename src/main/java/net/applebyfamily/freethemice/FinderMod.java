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
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

import java.util.Timer;
import java.util.TimerTask;

@Mod(modid = FinderMod.MOD_ID, version = FinderMod.VERSION)
public class FinderMod {
	
	private FinderNbtTags nbtSetting;
    public static final String MOD_ID = "Finder Mod";
    public static final String VERSION = "1.0.18";
    public EntityPlayerSP thePlayer;
    public World theWorld;
    
    @Instance(value = MOD_ID)
    public static FinderMod instance;
    public static Minecraft minecraft;
    public static FinderKeyBoard myKeyboard;

    public Timer gameTick;
    public boolean loaded;
    public boolean runOnEnter;
    public Tessellator mainDraw ;
    public FinderGuiHandler myGuiHandler;
    public FinderEvents eventManager;
    public int[] NumberforB = new int[2];
    public int[] NumberforN = new int[2];
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {    	
    	
    	instance = this;
    	nbtSetting = new FinderNbtTags("settings");
    	nbtSetting.fileName = "SettingsAreHere.dat";

        FMLLog.log(Level.INFO, "FinderMod  Copyright (C) 2013-2015  freethemice\n" +
                "This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.\n" +
                "This is free software, and you are welcome to redistribute it\n" +
                "under certain conditions; type `show c' for details.");

    	minecraft = FMLClientHandler.instance().getClient();
    	myKeyboard = new FinderKeyBoard();
    	
    	myGuiHandler = new FinderGuiHandler();
    	MinecraftForge.EVENT_BUS.register(myGuiHandler);
    	

    	eventManager = new FinderEvents();
    	FMLCommonHandler.instance().bus().register(eventManager);
    	MinecraftForge.EVENT_BUS.register(eventManager);
    	
    	
    	NBTTagCompound tmpNBTTag = nbtSetting.readNBTSettings();
    	
    	NumberforB[0] = tmpNBTTag.getInteger("Finder: Menu");
    	if (NumberforB[0] == 0)
    	{
    		NumberforB[0] = 48;
    	}
    	NumberforB[1] = minecraft.gameSettings.keyBindings.length;
    	setupKeyBinding("Finder: Menu", NumberforB[0]);
    	
    	NumberforN[0] = tmpNBTTag.getInteger("Finder: Add/Delete Waypoint");
    	if (NumberforN[0] == 0)
    	{
    		NumberforN[0] = 49;
    	}
    	NumberforN[1] = minecraft.gameSettings.keyBindings.length;
    	setupKeyBinding("Finder: Add/Delete Waypoint", NumberforN[0]);

    	gameTick = new Timer();
    	gameTick.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if (NumberforB[0] != minecraft.gameSettings.keyBindings[NumberforB[1]].getKeyCode())
				{
					NumberforB[0]  = minecraft.gameSettings.keyBindings[NumberforB[1]].getKeyCode();
					NBTTagCompound tmpNBTTag = nbtSetting.readNBTSettings();
					tmpNBTTag.setInteger("Finder: Menu", minecraft.gameSettings.keyBindings[NumberforB[1]].getKeyCode());
					nbtSetting.saveNBTSettings(tmpNBTTag);
					System.out.println("Saved B");
				}
				if (NumberforN[0] != minecraft.gameSettings.keyBindings[NumberforN[1]].getKeyCode())
				{
					NumberforN[0]  = minecraft.gameSettings.keyBindings[NumberforN[1]].getKeyCode();
					NBTTagCompound tmpNBTTag = nbtSetting.readNBTSettings();					
					tmpNBTTag.setInteger("Finder: Add/Delete Waypoint", minecraft.gameSettings.keyBindings[NumberforN[1]].getKeyCode());
					nbtSetting.saveNBTSettings(tmpNBTTag);
					System.out.println("Saved N");
				}
				// TODO Auto-generated method stub
				if (thePlayer == null)
				{
					if (minecraft.thePlayer != null)
					{
						thePlayer = minecraft.thePlayer;
					}
				}
				if (theWorld == null)
				{
					if (minecraft.theWorld != null)
					{
						 theWorld = minecraft.theWorld;
						 mainDraw = Tessellator.getInstance();
					}
				}
				
				if (thePlayer != null && theWorld != null)
				{
					loaded = true;
					if (thePlayer != minecraft.thePlayer)
					{
						thePlayer = null;
						loaded = false;
						runOnEnter = false;
					}
					if (theWorld != minecraft.theWorld)
					{
						theWorld = null;
						loaded = false;
						runOnEnter = false;
					}
					
					
				}
				if (loaded)
				{					
					if (runOnEnter)
					{				    				    					    	
						runOnEnter = true;
						eventManager.playerEnterWorld();	
					}				
					eventManager.gameTick();
				}
			}
		}, 10, 10);
        
    }

    public void sendChatMessage(String textout)
    {
        IChatComponent GoinOut =IChatComponent.Serializer.jsonToComponent("FinderMod");
        GoinOut.appendText(": " + textout);

        minecraft.ingameGUI.getChatGUI().printChatMessage(GoinOut);
    }

    private void setupKeyBinding(String Discription, int KeyCode) {

        KeyBinding[] tmp111 = new KeyBinding[minecraft.gameSettings.keyBindings.length + 1];
        for(int i = 0; i < minecraft.gameSettings.keyBindings.length;i++)
        {
            tmp111[i] = minecraft.gameSettings.keyBindings[i];
        }
        tmp111[minecraft.gameSettings.keyBindings.length] = new KeyBinding(Discription, KeyCode, "key.categories.misc");

        minecraft.gameSettings.keyBindings = tmp111.clone();
    }
}

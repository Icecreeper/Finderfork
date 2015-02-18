package net.applebyfamily.freethemice;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;













import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.server.FMLServerHandler;

@Mod(modid = FinderMod.MODID, version = FinderMod.VERSION)
public class FinderMod {
	
	private FM_NBTTags MyNBTSetting;
    public static final String MODID = "Finder Mod";
    public static final String VERSION = "1.0.18";
    public EntityPlayerSP thePlayer;
    public World theWorld;
    
    @Instance(value = MODID)
    public static FinderMod instance;
    public static Minecraft MC;
    public static FM_Keyboard myKeyboard;
        

    
    
    public Timer GameTick;
    public boolean loaded, runOnEnter;
    public int testCount = 0;
    public Tessellator mainDraw ;
    public FM_GuiHandler myGuiHandeler;
    public FM_Events eventManager;
    public int[] NumberforB = new int[2];
    public int[] NumberforN = new int[2];
    
    
    //@SidedProxy(clientSide = "net.applebyfamily.freethemice.ClientProxy", serverSide = "net.applebyfamily.freethemice.CommonProxy")
    //public static ClientProxy proxy;


    public void sendChatMessage(String textout)
    {
   	
    	IChatComponent GoinOut =IChatComponent.Serializer.jsonToComponent("FinderMod");
    	GoinOut.appendText(": " + textout);

    	Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(GoinOut);
    	
    }

	private void setupKeyBinding(String Discription, int KeyCode) {

		    	KeyBinding[] tmp111 = new KeyBinding[Minecraft.getMinecraft().gameSettings.keyBindings.length + 1];
		    	for(int i = 0; i < Minecraft.getMinecraft().gameSettings.keyBindings.length;i++)
		    	{
		    		tmp111[i] = Minecraft.getMinecraft().gameSettings.keyBindings[i];
		    	}
		    	tmp111[Minecraft.getMinecraft().gameSettings.keyBindings.length] = new KeyBinding(Discription, KeyCode, "key.categories.misc");
		    	
		    	Minecraft.getMinecraft().gameSettings.keyBindings = tmp111.clone();
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {    	
    	
    	instance = this;
    	MyNBTSetting = new FM_NBTTags("settings");
    	MyNBTSetting.FileName = "SettingsAreHere.dat";
    	
    	MC = FMLClientHandler.instance().getClient();
    	myKeyboard = new FM_Keyboard();
    	
    	myGuiHandeler = new FM_GuiHandler();
    	MinecraftForge.EVENT_BUS.register(myGuiHandeler);
    	

    	eventManager = new FM_Events();
    	FMLCommonHandler.instance().bus().register(eventManager);
    	MinecraftForge.EVENT_BUS.register(eventManager);
    	
    	
    	NBTTagCompound tmpNBTTag = MyNBTSetting.readNBTSettings();
    	
    	NumberforB[0] = tmpNBTTag.getInteger("Finder: Menu");
    	if (NumberforB[0] == 0)
    	{
    		NumberforB[0] = 48;
    	}
    	NumberforB[1] = Minecraft.getMinecraft().gameSettings.keyBindings.length;
    	setupKeyBinding("Finder: Menu", NumberforB[0]);
    	
    	NumberforN[0] = tmpNBTTag.getInteger("Finder: Add/Delete Waypoint");
    	if (NumberforN[0] == 0)
    	{
    		NumberforN[0] = 49;
    	}
    	NumberforN[1] = Minecraft.getMinecraft().gameSettings.keyBindings.length;
    	setupKeyBinding("Finder: Add/Delete Waypoint", NumberforN[0]);
    	
    	
    	
    	
    	GameTick = new Timer();
    	GameTick.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if (NumberforB[0] != Minecraft.getMinecraft().gameSettings.keyBindings[NumberforB[1]].getKeyCode())
				{
					NumberforB[0]  = Minecraft.getMinecraft().gameSettings.keyBindings[NumberforB[1]].getKeyCode();
					NBTTagCompound tmpNBTTag = MyNBTSetting.readNBTSettings();
					tmpNBTTag.setInteger("Finder: Menu", Minecraft.getMinecraft().gameSettings.keyBindings[NumberforB[1]].getKeyCode());
					MyNBTSetting.saveNBTSettings(tmpNBTTag);
					System.out.println("Saved B");
				}
				if (NumberforN[0] != Minecraft.getMinecraft().gameSettings.keyBindings[NumberforN[1]].getKeyCode())
				{
					NumberforN[0]  = Minecraft.getMinecraft().gameSettings.keyBindings[NumberforN[1]].getKeyCode();
					NBTTagCompound tmpNBTTag = MyNBTSetting.readNBTSettings();					
					tmpNBTTag.setInteger("Finder: Add/Delete Waypoint", Minecraft.getMinecraft().gameSettings.keyBindings[NumberforN[1]].getKeyCode());
					MyNBTSetting.saveNBTSettings(tmpNBTTag);
					System.out.println("Saved N");
				}
				// TODO Auto-generated method stub
				if (thePlayer == null)
				{
					if (MC.thePlayer != null)
					{
						thePlayer = MC.thePlayer;
					}
				}
				if (theWorld == null)
				{
					if (MC.theWorld != null)
					{
						 theWorld = MC.theWorld;
						 mainDraw = Tessellator.getInstance();
					}
				}
				
				if (thePlayer != null && theWorld != null)
				{
					loaded = true;
					if (thePlayer != MC.thePlayer)
					{
						thePlayer = null;
						loaded = false;
						runOnEnter = false;
					}
					if (theWorld != MC.theWorld)
					{
						theWorld = null;
						loaded = false;
						runOnEnter = false;
					}
					
					
				}
				if (loaded == true)
				{					
					if (runOnEnter == false)
					{				    				    					    	
						runOnEnter = true;
						eventManager.playerEnterWorld();	
					}				
					eventManager.gameTick();
				}
			}
		}, 10, 10);
        
    }
    

}

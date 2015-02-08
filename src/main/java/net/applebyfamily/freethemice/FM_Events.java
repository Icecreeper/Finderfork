package net.applebyfamily.freethemice;

import java.util.List;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Icon;
import javax.vecmath.Point3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


import org.lwjgl.util.vector.Vector3f;



import sun.awt.windows.ThemeReader;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.TickEvent;



public class FM_Events {

	public ArrayList<FM_Point3d> DrawersList;
	public ArrayList<FM_Point3d> tmpDrawersList;
	public ArrayList<FM_Point3d> Waypoints;
	public boolean calculating;
	public int range;
	public boolean _UpdateList;
	public FM_NBTTags MyNBTWorldSaves;
	public int _FoundCount = 0;
	public FM_Events()
	{
		this.DrawersList = new ArrayList<FM_Point3d>();
		this.tmpDrawersList = new ArrayList<FM_Point3d>();
		this.Waypoints = new ArrayList<FM_Point3d>();
		this._UpdateList = false;
		this.MyNBTWorldSaves = new FM_NBTTags("waypoints");
	}

    public void gameTick()
    {

    	
    	if (this.calculating == true || this._UpdateList == true)
    	{
    		return;
    	}
    	

    	Point3d tmpPlayer = new Point3d((int)FinderMod.instance.thePlayer.posX , (int)FinderMod.instance.thePlayer.posY, (int)FinderMod.instance.thePlayer.posZ );    	
    	 	
    	this.calculating = true;

    	
		World theWorld = FinderMod.instance.theWorld;
		
		int Find = FinderMod.instance.myGuiHandeler._MyGui.searchNumber;
		this.tmpDrawersList.clear();
		boolean A = false,B = false,C= false;
		if (Find == 0)
		{		
			if (FinderMod.instance.myGuiHandeler._MyGui.searchName != null)
			{
				if (FinderMod.instance.myGuiHandeler._MyGui.searchName.equals("") == false)
				{
						if (FinderMod.instance.myGuiHandeler._MyGui.searchName.equalsIgnoreCase("waypoints") == true || FinderMod.instance.myGuiHandeler._MyGui.searchName.equalsIgnoreCase("waypoint") == true)
						{
							for( int i = 0; i < this.Waypoints.size();i++)
							{
								this.tmpDrawersList.add((FM_Point3d) this.Waypoints.get(i).clone());
							}
							closeShortRange();
							
							this._UpdateList = true;
							this.calculating = false;
							this._FoundCount = this.tmpDrawersList.size();
							return;						
						}
						
						List tmpTEL = FinderMod.MC.theWorld.loadedEntityList;
						for(int x = 0; x< tmpTEL.size();x++)
						{
						
							if (tmpTEL.get(x).toString().toLowerCase().contains(FinderMod.instance.myGuiHandeler._MyGui.searchName.toLowerCase()) == true)					
							{
								FM_Point3d tmp = new FM_Point3d(((Entity)tmpTEL.get(x)).posX, ((Entity)tmpTEL.get(x)).posY, ((Entity)tmpTEL.get(x)).posZ);
								this.tmpDrawersList.add(tmp);
							}
							
							
						}
						tmpTEL = FinderMod.MC.theWorld.loadedTileEntityList;
						for(int x = 0; x< tmpTEL.size();x++)
						{	
							
							if (tmpTEL.get(x).toString().toLowerCase().contains(FinderMod.instance.myGuiHandeler._MyGui.searchName.toLowerCase()) == true)					
							{
								FM_Point3d tmp = new FM_Point3d(((TileEntity)tmpTEL.get(x)).xCoord, ((TileEntity)tmpTEL.get(x)).yCoord, ((TileEntity)tmpTEL.get(x)).zCoord);
								tmp.block = true;
								this.tmpDrawersList.add(tmp);
							}
								
							
							
						}		
						if (this.tmpDrawersList.size() == 0)
						{
							for(int x = -this.range; x< this.range + 1;x++)
							{			
					    		for(int y = 0; y< 256;y++)
					    		{
					        		for(int z = -this.range; z< this.range + 1;z++)
					        		{
										if (theWorld.getBlock((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z).getUnlocalizedName().toString().toLowerCase().contains(FinderMod.instance.myGuiHandeler._MyGui.searchName.toLowerCase()) == true)					
										{        				
											FM_Point3d tmp = new FM_Point3d((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z);
											
					        				if (tmp != null)
					        				{
					        					tmp.block = true;
					        					this.tmpDrawersList.add(tmp);
					        				}
					        				
					        			}
					        			
					        		}
					    		}
					    		
							}
							checkRang();
						}
						closeShortRange();
				}				
			}			
			this._UpdateList = true;
			this.calculating = false;
			this._FoundCount = this.tmpDrawersList.size();
			return;
		}
			
		
		if (Find > 0)
		{
			for(int x = -this.range; x< this.range + 1;x++)
			{			
	    		for(int y = 0; y< 256;y++)
	    		{
	        		for(int z = -this.range; z< this.range + 1;z++)
	        		{
	        			
	        			if (Block.getIdFromBlock(theWorld.getBlock((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z)) == Find)            				
	        			{	        				
	        				FM_Point3d tmp = new FM_Point3d((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z);
	        				if (tmp != null)
	        				{
	        					tmp.block = true;
	        					this.tmpDrawersList.add(tmp);
	        				}
	        				
	        			}
	        			
	        		}
	    		}
			}
	
		}		
		checkRang();
		closeShortRange();
		this._UpdateList = true;
		this.calculating = false;
		this._FoundCount = this.tmpDrawersList.size();
    }
    public String getSearchingFor()
    {
		int Find = FinderMod.instance.myGuiHandeler._MyGui.searchNumber;
		if (Find == 0)
		{		
			if (FinderMod.instance.myGuiHandeler._MyGui.searchName != null)
			{
				if (FinderMod.instance.myGuiHandeler._MyGui.searchName.equals("") == false)
				{
					return FinderMod.instance.myGuiHandeler._MyGui.searchName;
				}
			}
			return "Nothing";
		}
		return Find + "";
    }
	private void closeShortRange() {

		
		
				
		try {
			if (this.tmpDrawersList.size() > 1)
			{
				Collections.sort(this.tmpDrawersList, new Point3dCompare());
			}
		} catch (Exception e) {
			return;
		}
		
		
		if (this.tmpDrawersList.size() > 100)
		{
			this.tmpDrawersList.subList(100, this.tmpDrawersList.size()).clear();	
		}
		
	}
	public int getMaxRange()
	{
		int by = 100;
		
		try {
			by = Integer.parseInt(FinderMod.instance.myGuiHandeler._MyGui.rangefield.getText());
		} catch (NumberFormatException e) {
		}
		if (by < 30)
		{
			by = 30;
		}
			
		return by;
	}
	private void checkRang() {
		if (this.tmpDrawersList.size() < 100)
		{
			this.range = this.range + 10;
			//System.out.println(range);
			int by = getMaxRange();
			
			if (this.range >  by)
			{
				this.range = 10;
				
			}
		}
	}
    public void playerEnterWorld(){
    	this.MyNBTWorldSaves.FileName = getFileName();
    	NBTTagCompound var4 = this.MyNBTWorldSaves.readNBTSettings();
    	this.Waypoints.clear();    	
    	int maxCount = var4.getInteger("max");
    	for(int i = 0; i<maxCount;i++)
    	{
    		double pointX = var4.getDouble(i + "_point_X");
    		double pointY = var4.getDouble(i + "_point_Y");
    		double pointZ = var4.getDouble(i + "_point_Z");
    		FM_Point3d tmpPoint = new FM_Point3d(pointX, pointY, pointZ);
    		this.Waypoints.add((FM_Point3d) tmpPoint.clone());    		
    	}
    	
    }
	public String getFileName()
	{
		String NameP = "Multiplayer";
		try {
			NameP = MinecraftServer.getServer().worldServers[0].getWorldInfo().getSeed() + "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				
				NameP = Minecraft.getMinecraft().func_147104_D().serverIP;
				return "Multi_" + NameP + ".dat";
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
			}
		}
		return "Seed_" + NameP + ".dat";
	}
	

	
    @SubscribeEvent
	public void onOverlayRender( final RenderGameOverlayEvent.Text event )
    {
    	if (FinderMod.instance.loaded == true)
    	{
	    	if(Minecraft.getMinecraft().currentScreen == null)
	    	{
	    		if (getSearchingFor().equalsIgnoreCase("nothing") == false)
	    		{
	    			ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	    			FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
	    			int width = scaledresolution.getScaledWidth();
	    			int height = scaledresolution.getScaledHeight();
	    			fontrenderer.drawStringWithShadow(this._FoundCount +  "/100", width/2 - 10, 10, 0xffffffff);
	    		}
	    	}
    	}
    }
    @SubscribeEvent
	public void onWorldRender( final RenderWorldLastEvent event )
    {
    	
    	if (FinderMod.instance.loaded == true)
    	{
    			
    			for(int i = this.DrawersList.size() - 1; i > -1; i--)
    			{
    				FM_Point3d tmp = this.DrawersList.get(i);
    				if (tmp != null)
    				{
    					
    					  switch (i) {
	  			            case 0: 
	  			            		tmp.setRGB(0.0, 0.0, 0.9);
	  			            		 break;    					  
    			            case 1: 
    			            		tmp.setRGB(0.0, 0.9, 0.0);
    			                     break;
    			            case 2:  
    			            		tmp.setRGB(0.9, 0.9, 0.2);
    			                     break;
    			            case 3:  
			            			tmp.setRGB(0.9, 0.4, 0.0);
			            			 break;

    			            default: 
    			            		tmp.setRGB(0.9, 0.0, 0.0);
    			                     break;
    			        }				
    					  drawAroundBlock(tmp);
    				}
    			}
    			
    			if (this._UpdateList == true)
    			{

					this.DrawersList = (ArrayList<FM_Point3d>) this.tmpDrawersList.clone();
				
    				this._UpdateList = false;
    				//System.out.println("Drawer Updated: " + DrawersList.size());
    			}
    	}
    	
 
    }

	private void drawAroundBlock(FM_Point3d myPoint3d ) {
		if (myPoint3d.block == true)
		{
			final Block blockb = FinderMod.instance.theWorld.getBlock(myPoint3d.x(), myPoint3d.y(), myPoint3d.z());
			if( Block.getIdFromBlock( blockb ) != 0) {

				drawESP( AxisAlignedBB.getBoundingBox( myPoint3d.x, myPoint3d.y,myPoint3d.z,
						myPoint3d.x + 1.0f, myPoint3d.y + 1.0f,myPoint3d.z + 1.0f ), myPoint3d.r, myPoint3d.g, myPoint3d.b );
			}
		}
		else
		{				drawESP( AxisAlignedBB.getBoundingBox( myPoint3d.x - 0.5f, myPoint3d.y,myPoint3d.z - 0.5f,
				myPoint3d.x + 0.5f, myPoint3d.y + 1.0f,myPoint3d.z + 0.5f ), myPoint3d.r, myPoint3d.g, myPoint3d.b );
			
		}
	}

    public void drawESP( final AxisAlignedBB bb, final double r, final double g, final double b )  {
		Minecraft.getMinecraft( ).entityRenderer.disableLightmap( 0 );
		GL11.glPushMatrix( );
		GL11.glEnable( 3042 );
		GL11.glBlendFunc( 770, 771 );
		GL11.glLineWidth( 1.5F );
		GL11.glDisable( GL11.GL_LIGHTING );
		GL11.glDisable( GL11.GL_TEXTURE_2D );
		GL11.glEnable( GL11.GL_LINE_SMOOTH );
		GL11.glDisable( 2929 );
		GL11.glDepthMask( false );
		GL11.glColor4d( r, g, b, 0.1825F );
		drawBoundingBox( bb );
		GL11.glColor4d( r, g, b, 1.0F );
		drawOutlinedBoundingBox( bb );
		GL11.glLineWidth( 2.0F );
		GL11.glDisable( GL11.GL_LINE_SMOOTH );
		GL11.glEnable( GL11.GL_TEXTURE_2D );
		GL11.glEnable( GL11.GL_LIGHTING );
		GL11.glEnable( 2929 );
		GL11.glDepthMask( true );
		GL11.glDisable( 3042 );
		GL11.glPopMatrix( );
		
		Minecraft.getMinecraft( ).entityRenderer.enableLightmap( 0 );
	}
    public static void drawBoundingBox( final AxisAlignedBB axisalignedbb ) {
		final Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads( ); // starts x
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.draw( );
		tessellator.startDrawingQuads( );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.draw( ); // ends x
		tessellator.startDrawingQuads( ); // starts y
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.draw( );
		tessellator.startDrawingQuads( );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.draw( ); // ends y
		tessellator.startDrawingQuads( ); // starts z
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.draw( );
		tessellator.startDrawingQuads( );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.draw( ); // ends z
	}

	/**
	 * Draws lines for the edges of the bounding box.
	 */
	public static void drawOutlinedBoundingBox( final AxisAlignedBB axisalignedbb ) {
		final Tessellator var2 = Tessellator.instance;
		var2.startDrawing( 3 );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.draw( );
		var2.startDrawing( 3 );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.draw( );
		var2.startDrawing( 1 );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.draw( );
	}
    @SubscribeEvent
    public void SomethingPickedup(ItemPickupEvent event)
    {
    	//System.out.println("Test");
    }
    
}
class Point3dCompare implements Comparator<Point3d>{
	@Override
	public int compare(Point3d arg0, Point3d arg1) {
		
		
		Point3d tmpPlayer = new Point3d(FinderMod.MC.thePlayer.posX , FinderMod.MC.thePlayer.posY, FinderMod.MC.thePlayer.posZ );
		
		
        if(arg0.distance(tmpPlayer) > arg1.distance(tmpPlayer)){
            return 1;
        } else {
            return -1;
        }
	}
}


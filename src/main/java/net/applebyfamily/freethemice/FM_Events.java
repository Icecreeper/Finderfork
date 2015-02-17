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

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GLStateManager;


import org.lwjgl.util.vector.Vector3f;



import sun.awt.windows.ThemeReader;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;



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
								FM_Point3d tmp = new FM_Point3d(((TileEntity)tmpTEL.get(x)).getPos().getX(), ((TileEntity)tmpTEL.get(x)).getPos().getY(), ((TileEntity)tmpTEL.get(x)).getPos().getZ());
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
                                        BlockPos blockPos = new BlockPos((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z);
                                        if (theWorld.getChunkFromBlockCoords(blockPos).getBlock(blockPos).getUnlocalizedName().toString().toLowerCase().contains(FinderMod.instance.myGuiHandeler._MyGui.searchName.toLowerCase()) == true)
										{        				
											FM_Point3d tmp = new FM_Point3d(blockPos.getX(),blockPos.getY(),blockPos.getZ());
											
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
                        BlockPos blockPos = new BlockPos((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z);
	        			if (Block.getIdFromBlock(theWorld.getChunkFromBlockCoords(blockPos).getBlock(blockPos)) == Find)
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
				
				NameP = Minecraft.getMinecraft().getCurrentServerData().serverIP;
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
	    		if (getSearchingFor().equalsIgnoreCase("Nothing") == false)
	    		{
	    			ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	    			FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
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
			BlockPos blockPos = new BlockPos(myPoint3d.x(), myPoint3d.y(), myPoint3d.z());
            final Block blockb = FinderMod.instance.theWorld.getChunkFromBlockCoords(blockPos).getBlock(blockPos);
			if( Block.getIdFromBlock( blockb ) != 0) {

				drawESP( new AxisAlignedBB( myPoint3d.x, myPoint3d.y,myPoint3d.z,
						myPoint3d.x + 1.0f, myPoint3d.y + 1.0f,myPoint3d.z + 1.0f ), myPoint3d.r, myPoint3d.g, myPoint3d.b );
			}
		}
		else
		{				drawESP(new AxisAlignedBB( myPoint3d.x - 0.5f, myPoint3d.y,myPoint3d.z - 0.5f,
				myPoint3d.x + 0.5f, myPoint3d.y + 1.0f,myPoint3d.z + 0.5f ), myPoint3d.r, myPoint3d.g, myPoint3d.b );
			
		}
	}

    public void drawESP( final AxisAlignedBB bb, final double r, final double g, final double b )  {
		Minecraft.getMinecraft( ).entityRenderer.disableLightmap(/* 0 */);

		GLStateManager.PushMatrix( );
		GLStateManager.Enable( 3042 );
		GLStateManager.BlendFunc( 770, 771 );
		GLStateManager.LineWidth( 1.F );
		GLStateManager.Disable( GLStateManager.GL_LIGHTING );
		GLStateManager.Disable( GLStateManager.GL_TEXTURE_2D );
		GLStateManager.Enable( GLStateManager.GL_LINE_SMOOTH );
		GLStateManager.Disable( 2929 );
		GLStateManager.DepthMask( false );
		GLStateManager.Color4d( r, g, b, 0.1825F );
		drawBoundingBox( bb );
		GLStateManager.Color4d( r, g, b, 1.0F );
		drawOutlinedBoundingBox( bb );
        GLStateManager.Color4d( 255, 255, 255, 0.5F );
        GLStateManager.DepthMask( true );
		GLStateManager.LineWidth( 2.0F );
        GLStateManager.Enable( 2929 );
		GLStateManager.Disable( GLStateManager.GL_LINE_SMOOTH );
		GLStateManager.Enable( GLStateManager.GL_TEXTURE_2D );
		GLStateManager.Enable( GLStateManager.GL_LIGHTING );
		GLStateManager.Disable( 3042 );
		GLStateManager.PopMatrix( );
		
		Minecraft.getMinecraft( ).entityRenderer.enableLightmap(/* 0 */);
	}
    public static void drawBoundingBox( final AxisAlignedBB axisalignedbb ) {
		final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawingQuads( ); // starts x
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		tessellator.draw( );
		worldRenderer.startDrawingQuads( );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		tessellator.draw( ); // ends x
		worldRenderer.startDrawingQuads( ); // starts y
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		tessellator.draw( );
		worldRenderer.startDrawingQuads( );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		tessellator.draw( ); // ends y
		worldRenderer.startDrawingQuads( ); // starts z
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		tessellator.draw( );
		worldRenderer.startDrawingQuads( );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		worldRenderer.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		tessellator.draw( ); // ends z
	}

	/**
	 * Draws lines for the edges of the bounding box.
	 */
	public static void drawOutlinedBoundingBox( final AxisAlignedBB axisalignedbb ) {
		final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
		var3.startDrawing( 3 );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var2.draw( );
		var3.startDrawing( 3 );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var2.draw( );
		var3.startDrawing( 1 );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.minZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.maxX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.minY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var3.addVertex( axisalignedbb.minX - TileEntityRendererDispatcher.staticPlayerX, axisalignedbb.maxY
				- TileEntityRendererDispatcher.staticPlayerY, axisalignedbb.maxZ - TileEntityRendererDispatcher.staticPlayerZ );
		var2.draw( );
	}
    @Deprecated
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

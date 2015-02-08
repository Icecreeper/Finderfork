package net.applebyfamily.freethemice;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

//
// GuiBuffBar implements a simple status bar at the top of the screen which 
// shows the current buffs/debuffs applied to the character.
//
public class FM_Gui extends GuiScreen
{
  private Minecraft mc;
  public GuiTextField textfield, rangefield;
  public int searchNumber;
  public String searchName;
  public boolean _takeInput, _visible;
  public FM_GuiLabel[] labels;
  public FM_Gui()
  {
    super();

    searchNumber  = 0;

    this.mc = FMLClientHandler.instance().getClient();

    textfield = new GuiTextField(mc.fontRenderer, 80, 80, 150, 20); 
    textfield.setText("56");
    

    rangefield = new GuiTextField(mc.fontRenderer, 80, 110, 150, 20); 
    rangefield.setText("100");
    
    labels = new FM_GuiLabel[9];
    
    labels[0] = new FM_GuiLabel(mc.fontRenderer, 10, 10);
    labels[0].setText("Press 'Enter' to search, 'Esc' to cancel/refresh, 'tab' to change text fields");

    labels[1] = new FM_GuiLabel(mc.fontRenderer, 10, 30);
    labels[1].setText("Search for entities, blocks, IDs, players, and drops");
    
    labels[2] = new FM_GuiLabel(mc.fontRenderer, 10, 50);
    labels[2].setText("Examples: 'drops', 'chest', 'horse', 'iron', 'ore'");
    
    labels[3] = new FM_GuiLabel(mc.fontRenderer, 10, 85);
    labels[3].setText("Search for:");
    
    labels[4] = new FM_GuiLabel(mc.fontRenderer, 10, 115);
    labels[4].setText("Max range:");
    
    
    labels[5] = new FM_GuiLabel(mc.fontRenderer, 10, 150);
    labels[5].setText("*Range only applies to block search.");
    
    labels[6] = new FM_GuiLabel(mc.fontRenderer, 10, 170);
    labels[6].setText("Searching for: Nothing");
    
    labels[7] = new FM_GuiLabel(mc.fontRenderer, 10, 190);
    labels[7].setText("Current Range: 0/0");
    
    labels[8] = new FM_GuiLabel(mc.fontRenderer, 10, 210);
    labels[8].setText("Found: 0/100");
    
    
    textfield.setFocused(true);
	  
  }
  @Override
  public boolean doesGuiPauseGame(){
	  return false;
  }
  
  @Override
  public void drawScreen(int x, int y, float f)
  {
	  
	  try {
		  drawDefaultBackground();

		 labels[6].setText("Searching for: " + FinderMod.instance.eventManager.getSearchingFor());
		 labels[7].setText("Current Range: " + FinderMod.instance.eventManager.range + "/" +  FinderMod.instance.eventManager.getMaxRange());
		 labels[8].setText("Found: " + FinderMod.instance.eventManager._FoundCount + "/100");
		
				 
		for(int i = 0; i <labels.length; i++)
		{
			if (labels[i] != null)
			{
				labels[i].drawTextBox();
			}
		}
		
		
		
		textfield.drawTextBox();
		rangefield.drawTextBox();
		  
		  super.drawScreen(x, y, f);
	} catch (Exception e) {
		// TODO Auto-generated catch block

	}
  }
  
  @Override
  public void keyTyped(char c, int i){
	  super.keyTyped(c, i);
	   
	  //System.out.println("Keyboard: " + );

	  if (_takeInput == false)
	  {
		  return;
	  }
	  if (28 == i)//enter
	  {
		  if (isStringNumber(textfield.getText()) == true)
		  {
			  searchNumber = Integer.parseInt(textfield.getText());
			  searchName = "";
		  }
		  else
		  {
			  searchName = textfield.getText();
			  if (textfield.getText().equalsIgnoreCase("drop") || textfield.getText().equalsIgnoreCase("drops"))
			  {
				  searchName = "item";
			  }
			  searchNumber = 0;
			  //textfield.setText(searchNumber + "");
		  }
		  
		  if (isStringNumber(rangefield.getText()) == false)
		  {
			  rangefield.setText(100 + "");
		  }
		  
		  FinderMod.instance.thePlayer.closeScreen();
		  FinderMod.instance.eventManager.range = 10;	
		  System.out.println("-----");
		  _visible = false;		  
		  return;
	  }
	  if (1 == i) //esc
	  {
		  if (isStringNumber(rangefield.getText()) == false)
		  {
			  rangefield.setText(100 + "");
		  }
		  
		  FinderMod.instance.eventManager.range = 10;	
		  _visible = false;
		  return;
	  }
	  if (15 == i)//tab
	  {
		  if (textfield.isFocused() == true)
		  {
			  textfield.setFocused(false);
			  rangefield.setFocused(true);
			  
		  }
		  else
		  {
			  rangefield.setFocused(false);
			  textfield.setFocused(true);
			  
		  }
	  }
	  if (textfield.isFocused() == true)
	  {

		  String Pre = textfield.getText();
		  textfield.textboxKeyTyped(c, i);  
	  }
	  if (rangefield.isFocused() == true)
	  {
		  String Pre = rangefield.getText();
		  rangefield.textboxKeyTyped(c, i);  
		  if (isStringNumber(rangefield.getText()) == false && rangefield.getText().equals("") == false)
		  {
			  rangefield.setText(Pre);
		  }
	  }
	  }
  public boolean isStringNumber(String s)
  {
	  try {
		int t =  Integer.parseInt(s);
		return true;
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		return false;
	} 
  }
}
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

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

/**
 * GuiBuffBar implements a simple status bar at the top of the screen which
 * shows the current buffs/debuffs applied to the character.
 */
public class FinderGui extends GuiScreen
{
  public GuiTextField textfield, rangefield;
  public int searchNumber;
  public String searchName;
  public boolean _takeInput, _visible;
  public FinderGuiLabel[] labels;
  public FinderGui()
  {
    super();

    searchNumber  = 0;

    textfield = new GuiTextField(893, FinderMod.minecraft.fontRendererObj, 80, 80, 150, 20);
    textfield.setText("56");
    

    rangefield = new GuiTextField(894, FinderMod.minecraft.fontRendererObj, 80, 110, 150, 20);
    rangefield.setText("100");
    labels = new FinderGuiLabel[9];
    
    labels[0] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 10);
    labels[0].setText("Press 'Enter' to search, 'Esc' to cancel/refresh, 'tab' to change text fields");

    labels[1] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 30);
    labels[1].setText("Search for entities, blocks, IDs, players, and drops");
    
    labels[2] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 50);
    labels[2].setText("Examples: 'drops', 'chest', 'horse', 'iron', 'ore'");
    
    labels[3] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 85);
    labels[3].setText("Search for:");
    
    labels[4] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 115);
    labels[4].setText("Max range:");
    
    
    labels[5] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 150);
    labels[5].setText("*Range only applies to block search.");
    
    labels[6] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 170);
    labels[6].setText("Searching for: Nothing");
    
    labels[7] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 190);
    labels[7].setText("Current Range: 0/0");
    
    labels[8] = new FinderGuiLabel(FinderMod.minecraft.fontRendererObj, 10, 210);
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
  public void keyTyped(char c, int i) throws java.io.IOException{
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
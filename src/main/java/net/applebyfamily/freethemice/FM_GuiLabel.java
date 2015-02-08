package net.applebyfamily.freethemice;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiTextField;

public class FM_GuiLabel extends GuiTextField {

	public FM_GuiLabel(FontRenderer par1FontRenderer, int x, int y) {
		super(par1FontRenderer, x, y, 50000, 20);
	    this.setFocused(false);
	    this.setEnabled(false);
	    this.setEnableBackgroundDrawing(false);
	    this.setMaxStringLength(99999);
	    this.setDisabledTextColour(16777215);
	}

}
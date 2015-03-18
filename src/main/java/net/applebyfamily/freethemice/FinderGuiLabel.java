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

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class FinderGuiLabel extends GuiTextField {

	public FinderGuiLabel(FontRenderer par1FontRenderer, int x, int y) {
		super(892, par1FontRenderer, x, y, 50000, 20);
	    this.setFocused(false);
	    this.setEnabled(false);
	    this.setEnableBackgroundDrawing(false);
	    this.setMaxStringLength(99999);
	    this.setDisabledTextColour(16777215);
	}

}
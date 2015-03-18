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

import javax.vecmath.Point3d;

public class FinderPoint3d extends Point3d{

	public boolean block = false; 
	public double r;
	public double g;
	public double b;
	
	public FinderPoint3d()
	{
		super();
	}
	public FinderPoint3d(double x, double y, double z)
	{
		super(x, y, z);
	}
	public void setRGB(double tr, double tg, double tb)	
	{
		this.r = tr;
		this.g = tg;
		this.b = tb;
		
		
	}
	public int x()
	{
		return (int)x;		
	}
	public int y()
	{
		return (int)y;		
	}
	public int z()
	{
		return (int)z;		
	}
}

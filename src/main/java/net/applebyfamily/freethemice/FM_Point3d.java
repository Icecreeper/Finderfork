package net.applebyfamily.freethemice;

import javax.vecmath.Point3d;

public class FM_Point3d extends Point3d{

	public boolean block = false; 
	public double r;
	public double g;
	public double b;
	
	public FM_Point3d ()
	{
		super();
	}
	public FM_Point3d (double x, double y, double z)
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

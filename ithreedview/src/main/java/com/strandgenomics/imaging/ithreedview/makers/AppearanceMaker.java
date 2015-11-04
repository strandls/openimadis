package com.strandgenomics.imaging.ithreedview.makers;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Vector4f;


public class AppearanceMaker{
	Appearance appearance;

	public AppearanceMaker()
	{

	}

	public Appearance getAppearance(float xScale, float yScale, float zScale)
	{
		appearance = null; // clean any previous appearance variables
		PolygonAttributes p = new PolygonAttributes();
		p.setCullFace(PolygonAttributes.CULL_NONE);
		Material m = new Material();
		m.setLightingEnable(false);
		m.setCapability(Material.ALLOW_COMPONENT_WRITE);

		TexCoordGeneration tg = new TexCoordGeneration();
		tg.setFormat(TexCoordGeneration.TEXTURE_COORDINATE_3);

		tg.setPlaneS(new Vector4f(xScale,0.0f,0.0f,0.0f));
		tg.setPlaneT(new Vector4f(0.0f,yScale,0.0f,0.0f));
		tg.setPlaneR(new Vector4f(0.0f,0.0f,zScale,0.0f));

		appearance = new Appearance();
		appearance.setTexCoordGeneration(tg);
		appearance.setMaterial(m);
		appearance.setPolygonAttributes(p);
		appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f));

		appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		appearance.setCapability(Appearance.ALLOW_TEXTURE_READ);
		return appearance;
	}

	public void clearAppearance()
	{
		appearance = null;
	}
}

/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

package plantsInsects.styles;

import java.awt.Color;

import plantsInsects.Plant;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;

public class PlantStyle extends DefaultStyleOGL2D {

	@Override
	public Color getColor(Object o) {
		Plant agent = (Plant)o;
		return agent.getSpeciesParams().getDisplayCol();
	}
	
	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial)
	{
		return this.shapeFactory.createRectangle(15, 15);
	}
}

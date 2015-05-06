package plantsInsects.styles;

import java.awt.Color;
import java.awt.Font;

import plantsInsects.Insect;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

public class InsectStyle extends DefaultStyleOGL2D {

	

	@Override
	public Color getColor(Object o) {
		Insect agent = (Insect)o;
		return agent.getSpeciesParams().getDisplayCol();
	}
}

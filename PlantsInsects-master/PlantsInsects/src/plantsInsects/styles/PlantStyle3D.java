package plantsInsects.styles;

import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;

import plantsInsects.Plant;
import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;
import repast.simphony.visualization.visualization3D.style.Style3D.LabelPosition;

public class PlantStyle3D implements Style3D<Plant> {

	public TaggedBranchGroup getBranchGroup(Plant agent, TaggedBranchGroup taggedGroup) {

		if (taggedGroup == null || taggedGroup.getTag() == null) {
			taggedGroup = new TaggedBranchGroup("DEFAULT");
			float zVal = .03f;
			switch(agent.getSpeciesParams().getHeight()) {
				case High:
					zVal = zVal * 6;
					break;
				case Medium:
					zVal = zVal * 3;
					break;
				default:
					break;
			}
			Shape3D box = ShapeFactory.createCylinder(.03f, zVal, "DEFAULT");
			box.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			//taggedGroup.getBranchGroup().addChild(box);
			
			Transform3D trans = new Transform3D();
	   		trans.set(new Vector3f(0, zVal / 2 - .03f, 0));
	  		TransformGroup grp = new TransformGroup(trans);

	  		grp.addChild(box);
			taggedGroup.getBranchGroup().addChild(grp);

			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(Plant o) {
		return null;
	}

	public String getLabel(Plant o, String currentLabel) {
		return null; //return currentLabel.length() > 0 ? currentLabel : String.valueOf(o.getId());
//		return o.toString();
	}

	public Color getLabelColor(Plant t, Color currentColor) {
		return Color.YELLOW;
	}

	public Font getLabelFont(Plant t, Font currentFont) {
		return null;
	}

	public LabelPosition getLabelPosition(Plant o, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}

	public float getLabelOffset(Plant t) {
		return .035f;
	}

	public TaggedAppearance getAppearance(Plant agent, TaggedAppearance taggedAppearance, Object shapeID) {
		if (taggedAppearance == null) {
			taggedAppearance = new TaggedAppearance();
		}
		
		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), agent.getSpeciesParams().getDisplayCol());
		
		return taggedAppearance;
	}

	public float[] getScale(Plant o) {
		return null;
	}

}

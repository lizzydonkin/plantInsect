package plantsInsects.styles;

import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import plantsInsects.Insect;
import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;
import repast.simphony.visualization.visualization3D.style.Style3D.LabelPosition;

public class InsectStyle3D implements Style3D<Insect> {

	public TaggedBranchGroup getBranchGroup(Insect agent, TaggedBranchGroup taggedGroup) {

		if (taggedGroup == null || taggedGroup.getTag() == null) {
			taggedGroup = new TaggedBranchGroup("DEFAULT");
			Shape3D sphere = ShapeFactory.createSphere(.03f, "DEFAULT");
			sphere.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			
			float zVal = .03f * 9;
			Transform3D trans = new Transform3D();
	   		trans.set(new Vector3f(0, zVal, 0));
	  		TransformGroup grp = new TransformGroup(trans);
	  		
	  		grp.addChild(sphere);
			taggedGroup.getBranchGroup().addChild(grp);

			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(Insect o) {
		return null;
	}

	public String getLabel(Insect o, String currentLabel) {
		return null;
	}

	public Color getLabelColor(Insect t, Color currentColor) {
		return Color.YELLOW;
	}

	public Font getLabelFont(Insect t, Font currentFont) {
		return null;
	}

	public LabelPosition getLabelPosition(Insect o, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}

	public float getLabelOffset(Insect t) {
		return .035f;
	}

	public TaggedAppearance getAppearance(Insect agent, TaggedAppearance taggedAppearance, Object shapeID) {
		if (taggedAppearance == null) {
			taggedAppearance = new TaggedAppearance();
		}
		
		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), agent.getSpeciesParams().getDisplayCol());
		
		return taggedAppearance;
	}

	public float[] getScale(Insect o) {
		return null;
	}

}

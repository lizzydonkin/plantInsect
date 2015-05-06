package plantsInsects;

import java.awt.Color;

import plantsInsects.enums.PlantHeight;

public class PlantParams {

	private String plantId;
	private double percentage;
	private int damageThreshold;
	private int reproductiveSuccess;
	private PlantHeight height;
	private Color displayCol;
	
	private int count;
	private int totalDamage;
	
	public PlantParams(String plantId, double percentage, int damageThreshold,
			int reproductiveSuccess, PlantHeight height, Color col) {
		this.plantId = plantId;
		this.percentage = percentage;
		this.damageThreshold = damageThreshold;
		this.reproductiveSuccess = reproductiveSuccess;
		this.height = height;
		this.displayCol = col;
		
		this.count = 0;
		this.totalDamage = 0;
	}
	
	public PlantParams() {
		this.plantId = "";
		this.percentage = 0.5;
		this.damageThreshold = 2000;
		this.reproductiveSuccess = 20;
		this.height = PlantHeight.Medium;
		this.displayCol = Color.GREEN;
	}

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public int getDamageThreshold() {
		return damageThreshold;
	}

	public void setDamageThreshold(int damageThreshold) {
		this.damageThreshold = damageThreshold;
	}

	public int getReproductiveSuccess() {
		return reproductiveSuccess;
	}

	public void setReproductiveSuccess(int reproductiveSuccess) {
		this.reproductiveSuccess = reproductiveSuccess;
	}

	public PlantHeight getHeight() {
		return height;
	}

	public void setHeight(PlantHeight height) {
		this.height = height;
	}

	public Color getDisplayCol() {
		return displayCol;
	}

	public void setDisplayCol(Color displayCol) {
		this.displayCol = displayCol;
	}
	
	public void increaseTotalDamage() {
		totalDamage++;
	}
	
	public void setTotalDamage(int td) {
		totalDamage = td;
	}
	
	public int getTotalDamge() {
		return totalDamage;
	}
	
	public int getMaxDamage() {
		return count * damageThreshold;
	}
	
	public void setCount(int c) {
		count = c;
	}
}

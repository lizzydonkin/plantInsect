package plantsInsects;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class Plant {
	
	private Grid<Object> grid;
	private int damageTaken;
	private ArrayList<InsectEgg> eggs;
	private final PlantParams speciesParams;

	public Plant(PlantParams params, Context<Object> context)
	{
		grid = (Grid<Object>) context.getProjection("grid");
		damageTaken = 0;
		eggs = new ArrayList<InsectEgg>();
		speciesParams = params;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void hatchEggs()
	{
		if(SeasonHelper.isWinterSeason() || eggs.isEmpty()) {
			//System.out.println(eggs.size());
			return;
		}
		
		Context<Object> context = ContextUtils.getContext(this);
		
		double survivalChance = 1.0 - ((double)damageTaken) / ((double)speciesParams.getDamageThreshold()); 
		if(eggs.size() >= speciesParams.getReproductiveSuccess()) {
			final double EGGS_PERCENTAGE_REDUCED = 0.3;
			int notToBeHatched = Math.min(speciesParams.getReproductiveSuccess(), (int) ((eggs.size() - speciesParams.getReproductiveSuccess()) * EGGS_PERCENTAGE_REDUCED));
			survivalChance = Math.max(0, survivalChance - ((double)notToBeHatched) / ((double)speciesParams.getReproductiveSuccess()));
		}
		
		ArrayList<InsectEgg> due = new ArrayList<InsectEgg>();
		for (InsectEgg egg: eggs)
		{
			if(egg.getHatchTime() == 0)
			{
				due.add(egg);
			}
			else
			{
				egg.decreaseHatchTime();
			}
		}
		
		//System.out.println(due.size());
		
		double surviveCountD = due.size() * survivalChance;
		int surviveCount = (int) (Math.min(speciesParams.getReproductiveSuccess(), Math.round(surviveCountD)));
		SimUtilities.shuffle(due, RandomHelper.getUniform());
		
		int specCount = 0;
		int genCount = 0;
		
		for(int i = 0; i < surviveCount; i++) {
			InsectEgg egg = due.get(i);
			int eggsBornWith = 0;
			int max = egg.getSpeciesParams().getMaxEggs();

			if(egg.getSpeciesParams().getPreferredPlantIds().size() > 1 ||
					!egg.getSpeciesParams().getPreferredPlantIds().contains(speciesParams.getPlantId())) {
				eggsBornWith = (int) Math.round(((double)max) * (1 - egg.getSpeciesParams().getTradeOff()));
			} else {
				eggsBornWith = max;
			}
			
			// egg does not hatch
			if(eggsBornWith == 0) {
				continue;
			}
			
			if(egg.getSpeciesParams().getInsectId().equals("gen")) {
				genCount++;
			} else if(egg.getSpeciesParams().getInsectId().equals("spec")) {
				specCount++;
			}
			
			egg.setEggsLeft(eggsBornWith);
			context.add(egg);
			egg.getSpeciesParams().incrementCount();
			GridPoint pt = grid.getLocation(this);
			grid.moveTo(egg, pt.getX(), pt.getY());
			egg.getVisitedPlants().add(this);
			
			if(damageTaken != speciesParams.getDamageThreshold())
				speciesParams.increaseTotalDamage();
			damageTaken = Math.min(damageTaken + 1, speciesParams.getDamageThreshold());
		}
		
//		if(speciesParams.getPlantId().equals("crop") && !eggs.isEmpty())
//			System.out.println("Total: " + eggs.size() + ";\t Due: " + due.size() + ";\t Damage: " + damageTaken + ";\t Surv Chance: " + String.format("%.4f", survivalChance) + ";\t Gen: " + genCount + ";\t Spec: " + specCount);
		
		eggs.removeAll(due);
	}
	
	public void addEggs(Insect ins, int count)
	{		
		for(int i = 0; i < count; i++)
		{
			eggs.add(new InsectEgg(ins.getSpeciesParams(), ContextUtils.getContext(ins)));
		}		
	}
	
	public int removeHalfEggs() {
		int i;
		for(i = 0; i < eggs.size() / 2; i++) {
			eggs.remove(0);
		}
		return i;
	}
	
	public int getEggCount(){
		return eggs.size();
	}
	
	public int getDamageTaken() {
		return damageTaken;
	}

	public void setDamageTaken(int damage) {
		this.damageTaken = damage;
	}
	
	public PlantParams getSpeciesParams() {
		return speciesParams;
	}

}

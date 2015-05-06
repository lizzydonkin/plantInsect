package plantsInsects;

import java.util.ArrayList;
import java.util.List;

import plantsInsects.enums.PlantHeight;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class Insect {

	private Grid<Object> grid;
	private int eggsLeft;
	private ArrayList<Plant> visitedPlants;
	private final InsectParams speciesParams;
	private int currentTolerance;
	
	public Insect(InsectParams params, int eggs, Context<Object> context)
	{
		grid = (Grid<Object>) context.getProjection("grid");
		eggsLeft = eggs;
		visitedPlants = new ArrayList<Plant>();
		speciesParams = params;
		currentTolerance = params.getTolerance();
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void step()
	{				
		if(SeasonHelper.isWinterSeason())
			return;
		
		//System.out.println(grid.getLocation(this));
		Plant plant = getNextPlant();
		GridPoint newPt = grid.getLocation(plant);
		grid.moveTo(this, newPt.getX(), newPt.getY());
		//System.out.println(newPt);
		
		Context<Object> context = ContextUtils.getContext(this);
		
		// if current plant is preferred or tolerance reached 0
		if(speciesParams.getPreferredPlantIds().contains(plant.getSpeciesParams().getPlantId())
				|| currentTolerance <= 0) {
			int eggsToLay = Math.min(eggsLeft, speciesParams.getEggsPerPlant().get(plant.getSpeciesParams().getPlantId()));
			eggsLeft -= eggsToLay;
			currentTolerance = speciesParams.getTolerance();
			plant.addEggs(this, eggsToLay);
			
			// laid all eggs
			if(eggsLeft <= 0) {
				System.out.println(speciesParams.getInsectId() + ": " + toString() + " died because all its eggs hatched");
				context.remove(this);
				speciesParams.decrementCount();
				return;
			}
		}
		else {
			currentTolerance--;
		}
		
		System.out.println(currentTolerance);
		
		// daily mortality rate
		if(RandomHelper.nextDoubleFromTo(0, 1) < speciesParams.getMortalityRate()) {
			System.out.println(speciesParams.getInsectId() + ": " + toString() + " died as part of daily mortality rate");
			context.remove(this);
			speciesParams.decrementCount();
			return;
		}
		
		visitedPlants.add(plant);
		if(visitedPlants.size() > speciesParams.getMemorySize())
			visitedPlants.remove(0);
		
		double migOutChance = speciesParams.getMigrationOutRate();
		
		// increase migOutChance from memory (each non-preferred plant in memory increases the chance)
		final double MAX_MEMORY_IMPACT_ON_MIGRATION = 0.33;
		double singlePlantImpact = MAX_MEMORY_IMPACT_ON_MIGRATION / speciesParams.getMemorySize();
		for(Plant visited: visitedPlants) {
			if(!speciesParams.getPreferredPlantIds().contains(visited.getSpeciesParams().getPlantId())) {
				if(migOutChance > 0)
				{
					migOutChance += singlePlantImpact;
				}
			}
		}
		
		//System.out.println(speciesParams.getInsectId() + ": " + toString() + " migration chance after memory = " + migOutChance);
		
		// if on border double migOutChance
		int edgeY = grid.getDimensions().getHeight() - 1;
		int edgeX = grid.getDimensions().getWidth() - 1;
		if(newPt.getX() == 0 || newPt.getX() == edgeX || newPt.getY() == 0 || newPt.getY() == edgeY) {
			//System.out.println(speciesParams.getInsectId() + ": " + toString() + " is on border so migration chance doubled");
			migOutChance = Math.min(1, migOutChance * 2);
		}
		
		// migration out
		if(RandomHelper.nextDoubleFromTo(0, 1) < migOutChance) {
			System.out.println(speciesParams.getInsectId() + ": " + toString() + " migrated out");
			context.remove(this);
			speciesParams.decrementCount();
			return;
		}
	}
	
	private Plant getNextPlant()
	{
		Plant plant = null;
		switch(speciesParams.getSensoryMode()) {
			case Visual:
				plant = getNextPlantVisual();
				break;
			case Olfactory:
				plant = getNextPlantOlfactory();
				break;
			case Contact:
				plant = getNextPlantContact();
				break;
			default:
				break;
		}
		
		return plant;
	}
	
private Plant getNextPlantVisual() {		
	Plant closestNonPreferred = null;
	
	for(int radius = 1; radius <= speciesParams.getMaxFlightLength(); radius++) {
		ArrayList<Plant> plantsInCircle = getPlantsInRadius(radius);
		SimUtilities.shuffle(plantsInCircle, RandomHelper.getUniform());
		
		for(Plant plant: plantsInCircle) {
			if(speciesParams.getPreferredPlantIds().contains(plant.getSpeciesParams().getPlantId()) && !visitedPlants.contains(plant) && canSeePlant(plant))						
				return plant;
			else if(closestNonPreferred == null && !visitedPlants.contains(plant) && canSeePlant(plant))
				closestNonPreferred = plant;
		}
	}
	
	return closestNonPreferred;
}

private Plant getNextPlantContact() {
	GridCellNgh<Plant> nghCreator = new GridCellNgh<Plant>(grid, grid.getLocation(this),
			Plant.class, speciesParams.getMaxFlightLength(), speciesParams.getMaxFlightLength());
	List<GridCell<Plant>> gridCells = nghCreator.getNeighborhood(false);
	SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
	
	// return the first unvisited plant since they are shuffled, it will be random
	Plant result = null;
	for(GridCell<Plant> cell: gridCells) {
		Plant current = cell.items().iterator().next();
		if(!visitedPlants.contains(current)) {
			result = current;
			break;
		}
	}
	return  result;
}

private Plant getNextPlantOlfactory() {
	ArrayList<Plant> plantsInCircle = getPlantsInRadius(speciesParams.getMaxFlightLength());
	Plant chosen = plantsInCircle.get(RandomHelper.nextIntFromTo(0, plantsInCircle.size() - 1));
	
	ArrayList<Plant> plantsInLine = getPlantsInLine(grid.getLocation(chosen));
	for(Plant p: plantsInLine) {
		if(!visitedPlants.contains(p) && speciesParams.getPreferredPlantIds().contains(p.getSpeciesParams().getPlantId())) 
			return p;
	}
	
	return plantsInLine.get(plantsInLine.size() - 1);
}

private Plant getPlantAt(int x, int y) {
	Plant result = null;
	for(Object o: grid.getObjectsAt(x, y)) {
		if(o.getClass() == Plant.class) {
			result = (Plant) o;
			break;
		}
	}
	
	return result;
}

private ArrayList<Plant> getPlantsInRadius(int radius) {
	GridPoint insPoint = grid.getLocation(this);
	ArrayList<Plant> plantsInCircle = new ArrayList<Plant>();
	for(int i = -radius; i <= radius; i++) {
		int x1 = insPoint.getX() - radius;
		int y1 = insPoint.getY() + i;
		
		// otherwise creates duplicate
		if(radius != i)
			if(x1 >= 0 && x1 < grid.getDimensions().getWidth() && y1 >= 0 && y1 < grid.getDimensions().getHeight())
				plantsInCircle.add(getPlantAt(x1, y1));
		
		int x2 = insPoint.getX() + radius;
		int y2 = insPoint.getY() + i;
		if(radius != -i)
			if(x2 >= 0 && x2 < grid.getDimensions().getWidth() && y2 >= 0 && y2 < grid.getDimensions().getHeight())
				plantsInCircle.add(getPlantAt(x2, y2));
		
		int x3 = insPoint.getX() + i;
		int y3 = insPoint.getY() - radius;
		if(radius != -i)
			if(x3 >= 0 && x3< grid.getDimensions().getWidth() && y3 >= 0 && y3 < grid.getDimensions().getHeight())
				plantsInCircle.add(getPlantAt(x3, y3));
		
		int x4 = insPoint.getX() + i;
		int y4 = insPoint.getY() + radius;
		if(radius != i)
			if(x4 >= 0 && x4< grid.getDimensions().getWidth() && y4 >= 0 && y4 < grid.getDimensions().getHeight())
				plantsInCircle.add(getPlantAt(x4, y4));
	}
	//System.out.println(plantsInCircle.size());
	return plantsInCircle;
}

private ArrayList<Plant> getPlantsInLine(GridPoint toPoint) {
	
	GridPoint insPoint = grid.getLocation(this);
	ArrayList<Plant> plants = new ArrayList<Plant>();
	
	// Bresenham's line algorithm
	   
	int dx = Math.abs(toPoint.getX() - insPoint.getX());
	int dy = Math.abs(toPoint.getY() - insPoint.getY());
	int sx, sy;
	int err = dx - dy;
	
	if(insPoint.getX() < toPoint.getX())
		sx = 1;
	else
		sx = -1;
	
	if(insPoint.getY() < toPoint.getY())
		sy = 1;
	else
		sy = -1;
	
	int x = insPoint.getX();
	int y = insPoint.getY();
	
	while(true) {
		plants.add(getPlantAt(x, y));
		if(x == toPoint.getX() && y == toPoint.getY()) 
			break;
		
		int e2 = 2*err;
		if(e2 > -dy) {
			err = err - dy;
			x = x + sx;
		}
		
		if(e2 < dx) {
			err = err + dx;
			y = y + sy;
		}
	}
	
	return plants;
}

private boolean canSeePlant(Plant plant) {
	if(plant.getSpeciesParams().getHeight() == PlantHeight.High) {
		return true;
	}
	
	GridPoint plantPoint = grid.getLocation(plant);
	ArrayList<Plant> plantsInLine = getPlantsInLine(plantPoint);
	
	int highPos = -1, medPos = -1;
	
	for(int i = 0; i < plantsInLine.size(); i++) {
		PlantHeight currentHeight = plantsInLine.get(i).getSpeciesParams().getHeight();
		if(currentHeight == PlantHeight.High) {
			highPos = i;
		} else if(currentHeight == PlantHeight.Medium) {
			medPos = i;
		}
	}
	
	boolean canSee = false;
	if(highPos < 0) {
		if(medPos < 0 || plant.getSpeciesParams().getHeight() == PlantHeight.Medium) {
			canSee =  true;
		} else if(medPos >= plantsInLine.size() - 1 - medPos) {
			canSee = false;
		} else {
			canSee = true;
		}
	} else {
		if(highPos >= plantsInLine.size() - 1 - highPos) {
			canSee = false;
		} else {
			canSee = true;
		}
		
		if(medPos > highPos && medPos >= plantsInLine.size() - 1 - medPos) {
			canSee = false;
		}
	}
	
	return canSee;
}

	public int getEggsLeft() {
		return eggsLeft;
	}

	public void setEggsLeft(int eggsLeft) {
		this.eggsLeft = eggsLeft;
	}

	public ArrayList<Plant> getVisitedPlants() {
		return visitedPlants;
	}

	public void setVisitedPlants(ArrayList<Plant> visitedPlants) {
		this.visitedPlants = visitedPlants;
	}

	public InsectParams getSpeciesParams() {
		return speciesParams;
	}
}

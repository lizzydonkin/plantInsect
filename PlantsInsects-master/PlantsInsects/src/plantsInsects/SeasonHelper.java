package plantsInsects;

import java.util.HashSet;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.collections.IndexedIterable;

public class SeasonHelper {
	
	private Context<Object> context;
	private int yearCount;
	private boolean pause;
	
	private static boolean winterSeason = false;
	
	public SeasonHelper(int years, boolean pause, Context<Object> context) {
		this.yearCount = years;
		this.context = context;
		this.pause = pause;
	}

	@ScheduledMethod(start = 182, interval = 182, priority = 3)
	public void seasonChange()
	{
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
		if(tick % (364 * yearCount) == 0) {
			RunEnvironment.getInstance().endRun();
		} else if(tick % 364 == 182) {
			setWinterSeason(true);
			System.out.println("start winter season");
			
			HashSet<PlantParams> plantParams = new HashSet<PlantParams>();
			int removed = 0;
			for(Object plantObj: context.getObjects(Plant.class)) {
				Plant plant = (Plant) plantObj;
				if(plant != null) {
					plant.setDamageTaken(0);
					removed += plant.removeHalfEggs();
					plantParams.add(plant.getSpeciesParams());
				}
			}
			
			System.out.println("removed eggs: " + removed);
			
			for(PlantParams params: plantParams) {
				params.setTotalDamage(0);
			}
			
			HashSet<InsectParams> insParams = new HashSet<InsectParams>();
			IndexedIterable<Object> insects = context.getObjects(Insect.class);
			while(insects.iterator().hasNext()) {
				Insect ins = (Insect) insects.iterator().next();
				context.remove(ins);
				insParams.add(ins.getSpeciesParams());
			}
			
			for(InsectParams params: insParams) {
				params.setCount(0);
			}

		} else if(tick % 364 == 0) {
			setWinterSeason(false);
			System.out.println("start summer season");
			
			if(pause) {
				RunEnvironment.getInstance().pauseRun();
			}
		}
	}
	
	public static boolean isWinterSeason() {
		return winterSeason;
	}

	public static void setWinterSeason(boolean winterSeason) {
		SeasonHelper.winterSeason = winterSeason;
	}
}

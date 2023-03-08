package source.princess.systems;

import java.awt.Color;

import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.Misc;

public class UmbrellaFighterRecursiveDrone_AI extends BaseShipSystemScript
	{
	
	private ShipAPI ship;
	private CombatEngineAPI engine;
	private List<WeaponAPI> weapons;
	
	
	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
	
	public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine)
		{
		this.ship = ship;
		this.engine = engine;
		
		
		}
	
	@SuppressWarnings("unchecked")
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target)
		{
		tracker.advance(amount);
		
		if (tracker.intervalElapsed()) 
			{
			}
		}
	}

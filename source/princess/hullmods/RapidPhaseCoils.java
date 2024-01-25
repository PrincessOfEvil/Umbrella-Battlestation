package source.princess.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;

public class RapidPhaseCoils extends BaseHullMod {
	private static final float PHASE_BONUS_MULT = 5f;
	private static final float PHASE_MALUS_MULT = 4f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
	    stats.getDynamic().getMod(
	        "phase_cloak_flux_level_for_min_speed_mod").modifyMult(id, 0f);
	
		stats.getDynamic().getMod("phase_cloak_speed").modifyMult(id, PHASE_BONUS_MULT / 0.33f);
		stats.getDynamic().getMod("phase_cloak_accel").modifyMult(id, PHASE_BONUS_MULT / 0.33f);
		
		stats.getPhaseCloakActivationCostBonus().modifyMult(id, PHASE_MALUS_MULT);
		stats.getPhaseCloakUpkeepCostBonus().modifyMult(id, PHASE_MALUS_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round(PHASE_BONUS_MULT * 100f) + "%";
		if (index == 1) return "" + (int) Math.round(PHASE_MALUS_MULT * 100f) + "%";
		return null;
	}

}

package data.weapons.princess;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.combat.VPDriverEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Based off an unused, but seemingly working vanilla script.
/**
First X parts of CustomPrimaryHL are used as data for the script. You can write whatever you want afterwards.

X is 5 for Danmaku.

Default tooltip data:

Danmaku [2000, ENERGY, 10, 1.2] (When any projectile hits, the weapon deals 2000 energy damage, at 1.2 efficiency, to the target instantaneously and gets disabled for 10 seconds. All projectiles are automatically phased back to the autoloader on hit without dealing any damage on their own.)
%s [%s, %s, %s, %s] (When any projectile hits, the weapon deals %s %s damage, at %s efficiency, to the target instantaneously and gets disabled for %s seconds. %s)
Danmaku | 2000 | ENERGY | 10 | 1.2 | 2000 | energy | 1.2 | 10 | All projectiles are automatically phased back to the autoloader on hit without dealing any damage on their own.
Danmaku Damage Type Cooldown Efficiency Damage Type Efficiency Cooldown

*/

public class DanmakuScript implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin
	{
	public float damage = -1f;
	public DamageType type = DamageType.ENERGY;
	public float efficiency = 1f;
	public float cooldown = 6.9f;
	private float currentCooldown = 0f;
	
	public float emp = 0f;
	
	public List<DamagingProjectileAPI> shots;
	
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine)
		{
		if (this.shots == null)
			this.shots = new ArrayList<DamagingProjectileAPI>(); 
		this.shots.add(0, projectile);
		}
	
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon)
		{
		if (this.shots == null)
			return; 
		Iterator<DamagingProjectileAPI> iter = this.shots.iterator();
		while (iter.hasNext())
			{
			DamagingProjectileAPI shot = (DamagingProjectileAPI) iter.next();
			if (shot.isExpired() || shot.isFading())
				remove(iter);
			}
		
		if (currentCooldown > 0f)
			{
			currentCooldown -= VPDriverEffect.getRoFMult(weapon) * amount;
			weapon.setRemainingCooldownTo(currentCooldown);
			weapon.setForceNoFireOneFrame(true);
			}
		}
		
	public void cooldown()
		{
		currentCooldown += cooldown;
		}
	
		
	public void flux(ShipAPI source, WeaponAPI weapon)
		{
		source.getFluxTracker().increaseFlux(DanmakuScript.computeActualFluxCost(source, weapon, damage * efficiency), false);
		}
		
	public void loadData(WeaponAPI weapon)
		{
		String[] params = weapon.getSpec().getCustomPrimaryHL().split("\\|");
		for (int i = 0; i < params.length; i++)
			params[i] = params[i].trim();
		
		damage = Float.parseFloat(params[1]);
		type = (DamageType) Enum.valueOf(DamageType.class, params[2]);
		efficiency = Float.parseFloat(params[4]);
		cooldown = Float.parseFloat(params[3]);
		}
	
	public void cleanupProj()
		{
		Iterator<DamagingProjectileAPI> iter = this.shots.iterator();
		while (iter.hasNext())
			{
			DamagingProjectileAPI shot = (DamagingProjectileAPI) iter.next();
			
			if (Global.getCombatEngine().isEntityInPlay(shot))
				Global.getCombatEngine().removeEntity(shot);
			
			remove(iter);
			}
		}
	
	public void remove(Iterator<DamagingProjectileAPI> iter)
		{
		iter.remove(); 
		}
		
		
	public static float computeActualFluxCost(ShipAPI ship, WeaponAPI weapon, float val)
		{
		if (weapon == null || ship == null)
			return val; 
		float f = val;
		WeaponAPI.WeaponType weaponType = weapon.getType();
		MutableShipStatsAPI stats = ship.getMutableStats();
		if (weaponType == WeaponAPI.WeaponType.ENERGY)
			{
			f = stats.getEnergyWeaponFluxCostMod().computeEffective(val);
			}
		else if (weaponType == WeaponAPI.WeaponType.BALLISTIC)
			{
			f = stats.getBallisticWeaponFluxCostMod().computeEffective(val);
			}
		else if (weaponType == WeaponAPI.WeaponType.MISSILE)
			{
			f = stats.getMissileWeaponFluxCostMod().computeEffective(val);
			} 
		if (weapon.isBeam())
			f *= stats.getBeamWeaponFluxCostMult().getModifiedValue(); 
		return f;
		}
	}

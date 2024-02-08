package data.weapons.princess;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EmpArcEntityAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.combat.RealityDisruptorChargeGlow;
import org.lwjgl.util.vector.Vector2f;
import java.awt.Color;

public class DanmakuProjScript implements OnHitEffectPlugin
	{
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine)
		{
		// All hail Alex putting useful helper functions in random places.
	//	if (RealityDisruptorChargeGlow.isProjectileExpired(projectile)) return; // It *is* expired!
		if (projectile.getOwner() == target.getOwner()) return;
		ShipAPI source = projectile.getSource();
		if (source == null || source.isHulk()) return;
		WeaponAPI weapon = projectile.getWeapon();
		DanmakuScript dsc = (DanmakuScript) weapon.getEffectPlugin();
		if (dsc.damage + 1f < 0.00005f)
			dsc.loadData(weapon);
			
		weapon.stopFiring();
		dsc.cooldown();
		
		damageResult.setDamageToHull(0f);
		damageResult.setTotalDamageToArmor(0f);
		damageResult.setDamageToShields(0f);
		
		EmpArcEntityAPI arc = engine.spawnEmpArc(source, weapon.getFirePoint(0), (CombatEntityAPI)source, target, 
			dsc.type, 
			dsc.damage, 
			dsc.emp, 
			100000.0F, 
			"tachyon_lance_emp_impact", 
			40.0F,
			projectile.getProjectileSpec().getFringeColor(), 
			projectile.getProjectileSpec().getCoreColor());
		arc.setCoreWidthOverride(30F);
		
		dsc.flux(source, weapon);
		
		dsc.cleanupProj();
		}
	}

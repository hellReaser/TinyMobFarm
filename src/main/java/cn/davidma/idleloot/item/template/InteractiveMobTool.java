package cn.davidma.idleloot.item.template;

import cn.davidma.idleloot.util.Msg;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public abstract class InteractiveMobTool extends StandardItemBase {

	protected abstract String[] verb(); // [0]: present tense; [1]: past tense.
	protected abstract boolean interactEntity(ItemStack item, EntityPlayer player, EntityLivingBase mob);
	protected abstract boolean interactBlock();
	
	protected boolean shiny;
	private boolean[] damageItem;
	
	public InteractiveMobTool(String name, int maxDamage, boolean[] damageItem) {
		super(name);
		shiny = false;
		setMaxStackSize(1);
		setMaxDamage(maxDamage);
		this.damageItem = damageItem; // [0]: mob; [1]: block.
	}
	
	@Override
	public boolean hasEffect(ItemStack item) {
		return shiny;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack item, EntityPlayer player, EntityLivingBase mob, EnumHand hand) {
		
		// Check.
		if (mob.world.isRemote) return true;
		
		if (!mob.isEntityAlive()) {
			
			// Mob is dying.
			Msg.tellPlayer(player, "Cannot " + verb()[0] + " a dying mob.");
			return false;
		}
		
		if (!mob.isNonBoss()) {
			
			// Mob is a boss.
			Msg.tellPlayer(player, "Only non-boss mobs can be " + verb()[1] + ".");
			return false;
		}
		
		boolean result = interactEntity(item, player, mob);
		if (result && damageItem[0]) {
			item.damageItem(1, player);
		}
		return result;
	}
}

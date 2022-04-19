package com.exitius.eautoclick.runnables;

import com.exitius.eautoclick.EAutoClick;
import com.exitius.eautoclick.Settings;
import com.exitius.eautoclick.commands.AutoclickCommand;
import com.exitius.eautoclick.utils.EntityUtils;
import com.exitius.eautoclick.utils.TimeAPI;
import com.exitius.eautoclick.utils.messages.Messages;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class AutoClickRunnable extends BukkitRunnable {

    private final Player player;
    public long lastDamage;

    public AutoClickRunnable(Player player) {
        this.player = player;
        this.lastDamage = System.currentTimeMillis();
    }

    @Override
    public void run() {
        if (EAutoClick.getSettings().LITE_MODE)
            for (String s : AutoclickCommand.autoclick.keySet()) {
                Player player = Bukkit.getPlayer(s);
                if (player == null)
                    AutoclickCommand.autoclick.remove(s);
                else
                    click(player);
            }
        else
            click(player);
    }

    private void click(Player player) {
        List<Entity> entities = EntityUtils.getNearbyEntities(player, 2);
        if (entities.size() != 0) {
            for (Entity entity : entities) {
                if (entity.getType() != EntityType.PLAYER && isLookingAt(player, (LivingEntity) entity)
                        && verify(player) && player.getItemInHand() != null) {
                    double damage = getAttackDamage(player.getItemInHand());
                    if (EAutoClick.getSettings().FIXEDDAMAGE_ENABLED)
                        damage = EAutoClick.getSettings().FIXEDDAMAGE_DAMAGE;
                    for (Material material : EAutoClick.getSettings().CUSTOMDAMAGE.keySet()) {
                        if (player.getItemInHand().getType() == material)
                            damage = EAutoClick.getSettings().CUSTOMDAMAGE.get(material);
                    }
                    if (EAutoClick.getSettings().SHARPNESS)
                        if (player.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) {
                            damage += (1.25 * player.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)) - 1;
                        }
                    if (EAutoClick.getSettings().STRENGTH_POTION)
                        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                            final int[] amplifier = new int[1];
                            player.getActivePotionEffects().forEach(potionEffect -> amplifier[0] = potionEffect.getAmplifier());
                            damage += 3 * amplifier[0];
                        }
                    ((Damageable) entity).damage(damage, player);
                    lastDamage = System.currentTimeMillis();
                    // TODO setup a new packet system for hand animation
                    //if (EAutoClick.getSettings().HAND_ANIMATION)
                    //    EAutoClick.getPlugin().getPacketAPI().receivePacket(player, new PacketPlayInArmAnimation());
                }

            }

        }
        if (EAutoClick.getSettings().AUTODISABLE_ENABLED)
            if (TimeAPI.toTime(EAutoClick.getSettings().AUTODISABLE_TIME + "s")+lastDamage <= System.currentTimeMillis()) {
                Messages.sendMessage(player, EAutoClick.getMessages().getMessage("auto_disable"));
                this.cancel();
                AutoclickCommand.autoclick.remove(player.getName());
            }
    }

    private boolean verify(Player player) {
        if (Settings.PLOTSQUARED) {
            if (EAutoClick.getSettings().PLOTS_ENABLED) {
                PlotAPI api = EAutoClick.getPlugin().getPlotAPI();
                PlotPlayer pplayer = api.wrapPlayer(player.getName());
                if (pplayer != null) {
                    Plot plot = pplayer.getCurrentPlot();
                    if (plot != null) {
                        if (plot.getOwners().contains(pplayer.getUUID()) || player.hasPermission("eautoclick.bypass.location"))
                            return true;
                        if (EAutoClick.getSettings().PLOTS_MEMBER && plot.getMembers().contains(pplayer.getUUID()))
                            return true;
                        return EAutoClick.getSettings().PLOTS_TRUSTED && plot.getTrusted().contains(pplayer.getUUID());
                    }
                }
            }
        } else if (Settings.FACTIONS) {
            if (EAutoClick.getSettings().PLOTS_ENABLED) {
                MPlayer mplayer = MPlayer.get(player);
                Faction faction = mplayer.getFaction();
                Faction factionAtLocation = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));

                return faction.getName().equalsIgnoreCase(factionAtLocation.getName());
            }
        }

        for (String disabled_world : EAutoClick.getSettings().DISABLED_WORLDS) {
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(disabled_world))
                return false;
        }

        return true;
    }

    private boolean isLookingAt(Player player, LivingEntity livingEntity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.90;
    }

    public double getAttackDamage(ItemStack itemStack) {
        double attackDamage = 1.0;
        if (itemStack != null) {
            switch (itemStack.getType()) {
                case DIAMOND_SWORD: {
                    attackDamage = 7.0;
                    break;
                }
                case IRON_SWORD:
                case DIAMOND_AXE: {
                    attackDamage = 6.0;
                    break;
                }
                case DIAMOND_PICKAXE:
                case IRON_AXE: {
                    attackDamage = 5.0;
                    break;
                }
                case WOOD_SWORD:
                case GOLD_SWORD:
                case IRON_PICKAXE:
                case DIAMOND_SPADE: {
                    attackDamage = 4.0;
                    break;
                }
                case GOLD_AXE:
                case IRON_SPADE:
                case WOOD_AXE: {
                    attackDamage = 3.0;
                    break;
                }
                case WOOD_PICKAXE:
                case GOLD_PICKAXE: {
                    attackDamage = 2.0;
                    break;
                }
            }
        }
        return attackDamage;
    }

    public long getLastDamage() {
        return this.lastDamage;
    }

    public Player getPlayer() {
        return this.player;
    }
}

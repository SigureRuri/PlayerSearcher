package com.github.sigureruri.playersearcher;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class PlayerSearcher extends JavaPlugin {
    @Override
    public void onEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                getServer().getOnlinePlayers().forEach(player -> {
                    searchNearbyPlayers(player);
                });
            }
        }.runTaskTimer(this, 20L, 20L);
    }

    private void searchNearbyPlayers(final Player player) {
        final Vector playerVector = player.getEyeLocation().add(0, -0.5, 0).toVector();

        player.getNearbyEntities(60, 60, 60).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .forEach(nearbyPlayer -> {
                    final Vector nearbyPlayerVector = nearbyPlayer.getEyeLocation().toVector();
                    final Vector vector = nearbyPlayerVector.subtract(playerVector).normalize();

                    for (int i = 0; i < 10; i++) {
                        final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 0.6F);
                        // vectorにはclone()を付けなかったらイイ感じに見やすかった
                        final Location dustLocation = playerVector.clone().add(vector.multiply(1 + (i * 0.1) * 2.5)).toLocation(player.getWorld());;

                        player.spawnParticle(Particle.REDSTONE, dustLocation, 1, dustOptions);
                    }
                });
    }
}

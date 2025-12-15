//ruta del archivo: app/src/main/java/cl/angel/ge/gui/QuantityGuiListener.java
package cl.angel.ge.gui;

import cl.angel.ge.wizard.WizardService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuantityGuiListener implements Listener {

    private final JavaPlugin plugin;
    private final WizardService wizard;

    public QuantityGuiListener(JavaPlugin plugin, WizardService wizard) {
        this.plugin = plugin;
        this.wizard = wizard;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() == null)
            return;
        if (!QuantityGui.TITLE.equals(e.getView().getTitle()))
            return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p))
            return;

        WizardService.State state = wizard.get(p.getUniqueId());
        if (state == null || state.step != WizardService.Step.QUANTITY) {
            p.closeInventory();
            return;
        }

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType().isAir())
            return;

        Material type = clicked.getType();

        if (type == Material.BARRIER) {
            wizard.clear(p.getUniqueId());
            p.closeInventory();
            p.sendMessage("§c[GE] Operación cancelada.");
            return;
        }

        if (type == Material.REDSTONE) {
            // -10 o -1 según slot
            int raw = e.getRawSlot();
            if (raw == 11)
                state.quantity = Math.max(1, state.quantity - 10);
            if (raw == 12)
                state.quantity = Math.max(1, state.quantity - 1);
            refresh(p, state);
            return;
        }

        if (type == Material.EMERALD) {
            // +1 o +10 según slot
            int raw = e.getRawSlot();
            if (raw == 14)
                state.quantity = Math.min(9999, state.quantity + 1);
            if (raw == 15)
                state.quantity = Math.min(9999, state.quantity + 10);
            refresh(p, state);
            return;
        }

        if (type == Material.LIME_CONCRETE) {
            state.step = WizardService.Step.PRICE;
            p.closeInventory();
            Bukkit.getScheduler().runTask(plugin, () -> AnvilInputGui.openPrice(p));
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView() == null)
            return;
        if (!QuantityGui.TITLE.equals(e.getView().getTitle()))
            return;
        e.setCancelled(true);
    }

    private void refresh(Player p, WizardService.State state) {
        ItemStack center = p.getOpenInventory().getTopInventory().getItem(13);
        Material preview = (center == null || center.getType().isAir()) ? Material.STONE : center.getType();
        QuantityGui.open(p, state, preview);
    }

}

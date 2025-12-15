//ruta del archivo: app/src/main/java/cl/angel/ge/gui/GeGuiListener.java
package cl.angel.ge.gui;

import cl.angel.ge.market.MarketItemKey;
import cl.angel.ge.wizard.WizardService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public final class GeGuiListener implements Listener {

    private final WizardService wizard;

    public GeGuiListener(WizardService wizard) {
        this.wizard = wizard;
    }

    private boolean isGeTitle(String title) {
        return GeGui.TITLE.equals(title);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() == null || !isGeTitle(e.getView().getTitle()))
            return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player player))
            return;

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType().isAir())
            return;

        switch (clicked.getType()) {
            case EMERALD -> {
                WizardService.State s = wizard.start(player.getUniqueId(), WizardService.Mode.BUY);
                s.step = WizardService.Step.ITEM;
                s.quantity = 1;
                player.closeInventory();
                AnvilInputGui.openSearch(player);
            }
            case GOLD_INGOT -> {
                // SELL: item en mano (sin chat), luego cantidad GUI, luego precio Anvil
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand == null || hand.getType().isAir()) {
                    player.sendMessage("§c[GE] Para vender, pon el ítem en tu mano primero.");
                    return;
                }

                WizardService.State s = wizard.start(player.getUniqueId(), WizardService.Mode.SELL);
                s.itemKey = MarketItemKey.from(hand).asStringKey();
                s.step = WizardService.Step.QUANTITY;
                s.quantity = 1;

                player.closeInventory();
                QuantityGui.open(player, s, hand.getType());
            }
            case BOOK -> player.sendMessage("§b[GE] Mis órdenes (pendiente)");
            case BARRIER -> player.closeInventory();
            default -> {
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView() == null || !isGeTitle(e.getView().getTitle()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getView() == null || !isGeTitle(e.getView().getTitle()))
            return;
    }
}

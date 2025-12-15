//ruta del archivo: app/src/main/java/cl/angel/ge/gui/BuySearchGuiListener.java
package cl.angel.ge.gui;

import cl.angel.ge.market.MarketItemKey;
import cl.angel.ge.wizard.WizardService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public final class BuySearchGuiListener implements Listener {

    private final WizardService wizard;

    public BuySearchGuiListener(WizardService wizard) {
        this.wizard = wizard;
    }

    private boolean isBuySearchTitle(String title) {
        return title != null && title.startsWith(BuySearchGui.TITLE_PREFIX);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() == null || !isBuySearchTitle(e.getView().getTitle()))
            return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p))
            return;

        WizardService.State state = wizard.get(p.getUniqueId());
        if (state == null || state.mode != WizardService.Mode.BUY) {
            p.closeInventory();
            return;
        }

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType().isAir())
            return;

        Material type = clicked.getType();

        // Botones
        if (type == Material.BARRIER) {
            wizard.clear(p.getUniqueId());
            p.closeInventory();
            p.sendMessage("§c[GE] Operación cancelada.");
            return;
        }

        if (type == Material.ARROW) {
            int raw = e.getRawSlot();
            if (raw == 45) { // prev
                if (state.page > 0)
                    state.page--;
                BuySearchGui.open(p, state);
            } else if (raw == 53) { // next
                int maxPage = (state.searchResults.size() - 1) / 45;
                if (state.page < maxPage)
                    state.page++;
                BuySearchGui.open(p, state);
            }
            return;
        }

        // Selección de item (cualquier slot 0..44)
        if (e.getRawSlot() >= 0 && e.getRawSlot() < 45) {
            state.itemKey = MarketItemKey.from(new ItemStack(type)).asStringKey();
            state.step = WizardService.Step.QUANTITY;
            state.quantity = 1;

            p.closeInventory();
            QuantityGui.open(p, state, type);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView() == null || !isBuySearchTitle(e.getView().getTitle()))
            return;
        e.setCancelled(true);
    }
}

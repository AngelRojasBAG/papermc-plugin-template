//ruta del archivo: app/src/main/java/cl/angel/ge/gui/AnvilInputListener.java
package cl.angel.ge.gui;

import cl.angel.ge.search.MaterialSearch;
import cl.angel.ge.wizard.WizardService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnvilInputListener implements Listener {

    private final JavaPlugin plugin;
    private final WizardService wizard;

    public AnvilInputListener(JavaPlugin plugin, WizardService wizard) {
        this.plugin = plugin;
        this.wizard = wizard;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        if (!(e.getInventory() instanceof AnvilInventory anvil))
            return;

        String title = e.getView().getTitle();
        if (!title.equals(AnvilInputGui.SEARCH_TITLE) && !title.equals(AnvilInputGui.PRICE_TITLE))
            return;

        // Quitar costo/limitaciones del yunque (Paper puede bloquear por "Too
        // Expensive")
        anvil.setRepairCost(0);
        anvil.setMaximumRepairCost(0);

        // Forzar un resultado clickeable (aunque no haya "crafteo" real)
        ItemStack left = anvil.getItem(0);
        if (left == null)
            return;

        e.setResult(left.clone());
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent e) {
        if (!(e.getInventory() instanceof AnvilInventory anvil))
            return;

        String title = e.getView().getTitle();
        if (!title.equals(AnvilInputGui.SEARCH_TITLE) && !title.equals(AnvilInputGui.PRICE_TITLE))
            return;

        // Slot 2 = resultado del yunque
        if (e.getRawSlot() != 2)
            return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p))
            return;

        WizardService.State state = wizard.get(p.getUniqueId());
        if (state == null) {
            p.closeInventory();
            return;
        }

        // Texto real escrito por el jugador (más confiable que displayName del result)
        String text = anvil.getRenameText();
        if (text == null)
            text = "";
        text = text.trim();
        if (text.isEmpty())
            return;

        // SEARCH: solo para BUY + Step.ITEM
        if (title.equals(AnvilInputGui.SEARCH_TITLE)) {
            if (state.mode != WizardService.Mode.BUY || state.step != WizardService.Step.ITEM) {
                p.closeInventory();
                return;
            }

            state.searchQuery = text;
            state.page = 0;
            state.searchResults = MaterialSearch.search(text);

            p.closeInventory();

            if (state.searchResults == null || state.searchResults.isEmpty()) {
                p.sendMessage("§c[GE] No encontré resultados para: §f" + text);
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> BuySearchGui.open(p, state));
            return;
        }

        // PRICE: para BUY/SELL cuando Step.PRICE
        if (title.equals(AnvilInputGui.PRICE_TITLE)) {
            if (state.step != WizardService.Step.PRICE) {
                p.closeInventory();
                return;
            }

            Double price = parseDouble(text);
            if (price == null || price <= 0) {
                p.sendMessage("§c[GE] Precio inválido. Escribe un número > 0.");
                return;
            }

            state.unitPrice = price;

            p.closeInventory();
            p.sendMessage("§a[GE] OK: " + state.mode + " | itemKey=" + state.itemKey
                    + " | qty=" + state.quantity + " | unitPrice=" + state.unitPrice);

            wizard.clear(p.getUniqueId());
        }
    }

    private Double parseDouble(String s) {
        try {
            return Double.parseDouble(s.replace(",", "."));
        } catch (Exception ex) {
            return null;
        }
    }
}

//ruta del archivo: app/src/main/java/cl/angel/ge/gui/BuySearchGui.java
package cl.angel.ge.gui;

import cl.angel.ge.wizard.WizardService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class BuySearchGui {

    public static final String TITLE_PREFIX = "GE Buscar: ";

    private BuySearchGui() {
    }

    public static void open(Player player, WizardService.State state) {
        player.openInventory(create(player, state));
    }

    public static Inventory create(Player player, WizardService.State state) {
        String title = TITLE_PREFIX + (state.searchQuery == null ? "" : state.searchQuery);
        Inventory inv = Bukkit.createInventory(player, 54, title);

        // Fondo en barra inferior
        ItemStack filler = item(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 45; i < 54; i++)
            inv.setItem(i, filler);

        // Botones
        inv.setItem(45, item(Material.ARROW, "§eAnterior", List.of("§7Página: " + (state.page + 1))));
        inv.setItem(49, item(Material.BARRIER, "§cCancelar", List.of("§7Cierra y cancela wizard")));
        inv.setItem(53, item(Material.ARROW, "§eSiguiente", List.of("§7Página: " + (state.page + 1))));

        // Render resultados
        List<Material> results = state.searchResults;
        int pageSize = 45;
        int start = state.page * pageSize;

        for (int slot = 0; slot < 45; slot++) {
            int idx = start + slot;
            if (results == null || idx >= results.size())
                break;

            Material mat = results.get(idx);
            inv.setItem(slot, item(mat, "§f" + pretty(mat.name()), List.of("§7Click para seleccionar")));
        }

        return inv;
    }

    private static String pretty(String enumName) {
        return enumName.toLowerCase().replace("_", " ");
    }

    private static ItemStack item(Material mat, String name) {
        return item(mat, name, null);
    }
    
    private static ItemStack item(Material mat, String name, List<String> lore) {
        ItemStack it = new ItemStack(mat);
        ItemMeta meta = it.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null)
                meta.setLore(lore);
            it.setItemMeta(meta);
        }
        return it;
    }
}

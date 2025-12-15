//ruta del archivo: app/src/main/java/cl/angel/ge/gui/QuantityGui.java
package cl.angel.ge.gui;

import cl.angel.ge.wizard.WizardService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class QuantityGui {

    public static final String TITLE = "GE Cantidad";
    public static final int SIZE = 27;

    private QuantityGui() {
    }

    public static void open(Player p, WizardService.State state, Material previewMat) {
        p.openInventory(create(p, state, previewMat));
    }

    public static Inventory create(Player p, WizardService.State state, Material previewMat) {
        Inventory inv = Bukkit.createInventory(p, SIZE, TITLE);

        ItemStack filler = item(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < SIZE; i++)
            inv.setItem(i, filler);

        inv.setItem(11, item(Material.REDSTONE, "§c-10", List.of("§7Reducir 10")));
        inv.setItem(12, item(Material.REDSTONE, "§c-1", List.of("§7Reducir 1")));

        inv.setItem(13, item(previewMat, "§fCantidad: §e" + state.quantity, List.of("§7Ajusta con botones")));

        inv.setItem(14, item(Material.EMERALD, "§a+1", List.of("§7Sumar 1")));
        inv.setItem(15, item(Material.EMERALD, "§a+10", List.of("§7Sumar 10")));

        inv.setItem(22, item(Material.LIME_CONCRETE, "§aConfirmar", List.of("§7Ir a precio")));
        inv.setItem(26, item(Material.BARRIER, "§cCancelar", List.of("§7Cancelar wizard")));

        return inv;
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

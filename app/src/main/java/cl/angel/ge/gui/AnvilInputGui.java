//ruta del archivo: app/src/main/java/cl/angel/ge/gui/AnvilInputGui.java
package cl.angel.ge.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class AnvilInputGui {

    public static final String SEARCH_TITLE = "GE Buscar";
    public static final String PRICE_TITLE  = "GE Precio";

    private AnvilInputGui() {}

    public static void openSearch(Player p) {
        Inventory inv = Bukkit.createInventory(p, org.bukkit.event.inventory.InventoryType.ANVIL, SEARCH_TITLE);
        inv.setItem(0, named(Material.PAPER, "escribe aqui"));
        p.openInventory(inv);
    }

    public static void openPrice(Player p) {
        Inventory inv = Bukkit.createInventory(p, org.bukkit.event.inventory.InventoryType.ANVIL, PRICE_TITLE);
        inv.setItem(0, named(Material.GOLD_NUGGET, "0"));
        p.openInventory(inv);
    }

    private static ItemStack named(Material mat, String name) {
        ItemStack it = new ItemStack(mat);
        ItemMeta meta = it.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            it.setItemMeta(meta);
        }
        return it;
    }
}

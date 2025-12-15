//ruta del archivo: app/src/main/java/cl/angel/ge/gui/GeGui.java
package cl.angel.ge.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class GeGui {

    public static final String TITLE = "Grand Exchange";
    public static final int SIZE = 27;

    private GeGui() {}

    public static Inventory create(Player player) {
        Inventory inv = Bukkit.createInventory(player, SIZE, TITLE);

        // Fondo
        ItemStack filler = item(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < SIZE; i++) inv.setItem(i, filler);

        // Botones principales
        inv.setItem(11, item(Material.EMERALD, "§aComprar", List.of("§7Crear orden de compra")));
        inv.setItem(13, item(Material.GOLD_INGOT, "§6Vender", List.of("§7Crear orden de venta")));
        inv.setItem(15, item(Material.BOOK, "§bMis órdenes", List.of("§7Ver tus órdenes activas")));

        // Acciones
        inv.setItem(22, item(Material.BARRIER, "§cCerrar", List.of("§7Cerrar menú")));

        return inv;
    }

    public static void open(Player player) {
        player.openInventory(create(player));
    }

    private static ItemStack item(Material mat, String name) {
        return item(mat, name, null);
    }

    private static ItemStack item(Material mat, String name, List<String> lore) {
        ItemStack it = new ItemStack(mat);
        ItemMeta meta = it.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null) meta.setLore(lore);
            it.setItemMeta(meta);
        }
        return it;
    }
}

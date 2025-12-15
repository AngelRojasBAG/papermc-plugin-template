//ruta del archivo: app/src/main/java/cl/angel/ge/market/MarketItemKey.java
package cl.angel.ge.market;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.TreeMap;

public final class MarketItemKey {
    public final Material material;
    public final Integer customModelData; // null si no aplica
    public final String displayName; // null si no tiene
    public final Map<String, Integer> enchants; // ordenado

    private MarketItemKey(Material material, Integer cmd, String name, Map<String, Integer> enchants) {
        this.material = material;
        this.customModelData = cmd;
        this.displayName = name;
        this.enchants = enchants;
    }

    public static MarketItemKey from(ItemStack item) {
        Material mat = item.getType();

        ItemMeta meta = item.getItemMeta();
        Integer cmd = null;
        String name = null;
        Map<String, Integer> ench = new TreeMap<>();

        if (meta != null) {
            if (meta.hasCustomModelData())
                cmd = meta.getCustomModelData();
            if (meta.hasDisplayName())
                name = meta.getDisplayName();
        }

        for (Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
            ench.put(e.getKey().getKey().toString(), e.getValue());
        }

        return new MarketItemKey(mat, cmd, name, ench);
    }

    public String asStringKey() {
        // Formato estable para DB / matching (simple)
        return material.name()
                + "|cmd=" + (customModelData == null ? "null" : customModelData)
                + "|name=" + (displayName == null ? "null" : displayName.replace("|", "/"))
                + "|ench=" + enchants.toString();
    }
}

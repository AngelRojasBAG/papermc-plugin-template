//ruta del archivo: app/src/main/java/cl/angel/ge/search/MaterialSearch.java
package cl.angel.ge.search;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MaterialSearch {

    private MaterialSearch() {
    }

    public static List<Material> search(String query) {
        String q = normalize(query);

        List<Material> out = new ArrayList<>();
        for (Material m : Material.values()) {
            if (m.isAir())
                continue;
            String name = normalize(m.name());
            if (name.contains(q))
                out.add(m);
        }

        out.sort(Comparator.comparing(Enum::name));
        return out;
    }

    private static String normalize(String s) {
        return s.toLowerCase().replace("_", "");
    }
}

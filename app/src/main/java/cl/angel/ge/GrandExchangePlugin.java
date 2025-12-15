//ruta del archivo: app/src/main/java/cl/angel/ge/GrandExchangePlugin.java
package cl.angel.ge;

import cl.angel.ge.command.GeCommand;
import cl.angel.ge.gui.GeGuiListener;
import cl.angel.ge.wizard.WizardService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import cl.angel.ge.gui.BuySearchGuiListener;
import cl.angel.ge.gui.AnvilInputListener;
import cl.angel.ge.gui.QuantityGuiListener;

public final class GrandExchangePlugin extends JavaPlugin {

    private final WizardService wizardService = new WizardService();

    @Override
    public void onEnable() {
        getLogger().info("Grand Exchange plugin iniciado");

        getCommand("ge").setExecutor(new GeCommand());

       Bukkit.getPluginManager().registerEvents(new GeGuiListener(wizardService), this);
        // Bukkit.getPluginManager().registerEvents(new
        // WizardChatListener(wizardService), this);
        Bukkit.getPluginManager().registerEvents(new BuySearchGuiListener(wizardService), this);
        Bukkit.getPluginManager().registerEvents(new QuantityGuiListener(this, wizardService), this);
        Bukkit.getPluginManager().registerEvents(new AnvilInputListener(this, wizardService), this);

        getLogger().info("Grand Exchange habilitado");
    }

    public WizardService wizard() {
        return wizardService;
    }

    @Override
    public void onDisable() {
        getLogger().info("Grand Exchange plugin detenido");
    }
}

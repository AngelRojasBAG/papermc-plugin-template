//ruta del archivo: app/src/main/java/cl/angel/ge/command/GeCommand.java
package cl.angel.ge.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cl.angel.ge.gui.GeGui;

public class GeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Solo jugadores pueden usar este comando.");
            return true;
        }
        GeGui.open(player);
        return true;
    }
}

package art.ryanstew.removeplayerdata;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RemovePlayerData extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();

        Objects.requireNonNull(getCommand("removeplayerdata")).setExecutor(new RPDCommand(this));
    }


    @Override
    public void onDisable() { }


    public void sendFormattedMessage(CommandSender sender, String message, boolean prefixed)
    {
        if (prefixed)
            message = getConfig().getString("prefix") + message;

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}

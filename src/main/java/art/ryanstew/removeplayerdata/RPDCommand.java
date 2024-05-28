package art.ryanstew.removeplayerdata;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class RPDCommand implements CommandExecutor
{

    private static final String RELOAD_PERMISSION = "removeplayerdata.reload";

    private final RemovePlayerData plugin;


    public RPDCommand(RemovePlayerData plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            plugin.sendFormattedMessage(sender, "&cIncorrect usage! Usage: /removeplayerdata <player name>.", true);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload"))
        {
            if (!sender.hasPermission(RELOAD_PERMISSION))
            {
                plugin.sendFormattedMessage(sender, "&cYou do not have permission to run that command!", false);
                return true;
            }

            plugin.reloadConfig();
            plugin.sendFormattedMessage(sender, "&aThe config has successfully been reloaded!", true);
            return true;
        }

        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[0]);

        if (offlinePlayer.isOnline())
        {
            Player player = offlinePlayer.getPlayer();
            assert player != null;

            String messageHexCode = plugin.getConfig().getString("kick-message-hex");
            player.kick(Component.text("You have been kicked because your player data is being reset!").color(TextColor.fromHexString(messageHexCode)));
        }

        boolean success = false;
        List<World> worlds = plugin.getServer().getWorlds();
        for (World world : worlds)
        {
            String serverFolderPath = plugin.getServer().getWorldContainer().getAbsolutePath();

            String uuidFileName = String.format("%s.dat", offlinePlayer.getUniqueId());
            Path playerDataPath = Paths.get(serverFolderPath, world.getName(), "playerdata", uuidFileName);

            if (!Files.exists(playerDataPath))
                continue;

            success = new File(playerDataPath.toString()).delete();
        }

        String message = success ? "&aSuccessfully deleted that player's data!" : "&cCould not delete that player's data!";
        plugin.sendFormattedMessage(sender, message, true);;

        return true;
    }
}

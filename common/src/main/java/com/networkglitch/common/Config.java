package com.networkglitch.common;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

public class Config {
    public static final String ModName = "joinleavemessages";
    public static final String ConfigLocation = "config/";
    private final Properties prop = new Properties();

    public Config() {
        try {
            LoadConfigFromFile();
            if (GetBool(Definitions.ConfigKeys.DEBUG)) Logging.EnableDebug();
        } catch (IOException e) {
            Logging.error("Unable to process config file");
            Logging.info(e.getMessage());
            Logging.info(e.getCause().toString());
            Logging.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public boolean BlockMessage(String key) {
        return switch (key) {
            case Definitions.MessageKeys.PlayerJoined,
                    Definitions.MessageKeys.PlayerLeft,
                    Definitions.MessageKeys.PlayerRenamed -> true;
            default -> false;
        };
    }


    public String SendPrivateMessage(String playerName) {
        return SendPrivateMessage(playerName, false);

    }

    public String SendPrivateMessage(String playerName, boolean isFirst) {
        if (isFirst && GetBool(Definitions.ConfigKeys.PrivateFirstJoinEnabled))
            return ReplaceName(GetString(Definitions.ConfigKeys.PrivateFirstJoinMessageText), playerName);

        if (GetBool(Definitions.ConfigKeys.PrivateJoinEnabled))
            return ReplaceName(GetString(Definitions.ConfigKeys.PrivateJoinMessageText), playerName);

        return null;
    }
    public Definitions.SendMessageResponse SendLeavingMessage(String playerName) {
        if (!GetBool(Definitions.ConfigKeys.PlayerLeftEnabled)) return new Definitions.SendMessageResponse(false);
        if (GetBool(Definitions.ConfigKeys.CustomLeaveMessageEnabled))
            return new Definitions.SendMessageResponse(null,
                    ReplaceName(GetString(Definitions.ConfigKeys.CustomLeaveMessageText), playerName));

        return new Definitions.SendMessageResponse(Definitions.MessageKeys.PlayerLeft, null);
    }

    public Definitions.SendMessageResponse SendJoinMessage(String playerName, String oldName) {
        if (oldName == null) {
            var firstJoinResponse = this.HandleFirstJoin(playerName);
            if (firstJoinResponse.getSendMessage()) return firstJoinResponse;
        }

        if (!playerName.equals(oldName) && oldName != null) {
            var renameResponse = this.HandleRename(playerName, oldName);
            if (renameResponse.getSendMessage()) return renameResponse;
        }

        if (!GetBool(Definitions.ConfigKeys.PlayerJoinedEnabled)) return new Definitions.SendMessageResponse(false);
        if (GetBool(Definitions.ConfigKeys.CustomJoinMessageEnabled))
            return new Definitions.SendMessageResponse(null,
                    ReplaceName(GetString(Definitions.ConfigKeys.CustomJoinMessageText), playerName));


        return new Definitions.SendMessageResponse(Definitions.MessageKeys.PlayerJoined, null);
    }

    private Definitions.SendMessageResponse HandleFirstJoin(String playerName) {
        if (!GetBool(Definitions.ConfigKeys.FirstPlayerJoinedEnabled))
            return new Definitions.SendMessageResponse(false);
        if (GetBool(Definitions.ConfigKeys.CustomFirstJoinMessageEnabled))
            return new Definitions.SendMessageResponse(null,
                    ReplaceName(GetString(Definitions.ConfigKeys.CustomFirstJoinMessageText), playerName));

        return new Definitions.SendMessageResponse(Definitions.MessageKeys.PlayerJoined, null);
    }

    private Definitions.SendMessageResponse HandleRename(String playerName, String oldName) {
        if (!GetBool(Definitions.ConfigKeys.PlayerRenamedEnabled))
            return new Definitions.SendMessageResponse(false);
        if (GetBool(Definitions.ConfigKeys.CustomRenameMessageEnabled))
            return new Definitions.SendMessageResponse(null,
                    ReplaceName(GetString(Definitions.ConfigKeys.CustomRenameMessageText), playerName, oldName));

        return new Definitions.SendMessageResponse(Definitions.MessageKeys.PlayerRenamed, null);
    }

    private static String ReplaceName(String message, String name) {
        return message.replace("%p", name);
    }
    private static String ReplaceName(String message, String name, String oldName) {
        if(oldName == null) return ReplaceName(message, name);
        return ReplaceName(message, name).replace("%o", oldName);
    }

    private Integer GetInteger(String key) {
        var keyValue = Get(key);
        if (keyValue == null) return 0;
        return Integer.parseInt(keyValue);
    }

    private String GetString(String key) {
        return Get(key);
    }

    private Boolean GetBool(String key) {
        var keyValue = Get(key);
        if (keyValue == null) return false;
        return Boolean.parseBoolean(keyValue);
    }

    private void LoadConfigFromFile() throws IOException {
        try {
            var file = new File(ConfigLocation + ModName + ".properties");
            InputStream inputStream = new FileInputStream(file.getPath());
            prop.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException exception) {
            LoadInternal();
            CopyInternal();
        }
    }

    private void CopyInternal() {
        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(ModName +".properties");
            if (stream == null) throw new Exception();

            Files.createDirectories(Paths.get(ConfigLocation));
            File targetFile = new File(ConfigLocation + ModName + ".properties");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(stream.readAllBytes());
            stream.close();
            outStream.close();
        } catch (Exception exception) {
            Logging.info("Unable to copy config file from jar to folder");
        }
    }

    private void LoadInternal() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream("joinleavemessages.properties");
            if (stream == null) throw new Exception();
            prop.load(stream);
            stream.close();
        } catch (Exception exception) {
            Logging.info("Unable to find config file in resource folder of jar");
        }
    }

    private String Get(String key) {
        if (!prop.containsKey(key)) {
            Logging.info("unable to find config key " + key);
            return null;
        }
        return prop.getProperty(key);
    }


}

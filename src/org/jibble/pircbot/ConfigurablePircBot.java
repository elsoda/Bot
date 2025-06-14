/*
Author: David Lazar
*/
package org.jibble.pircbot;

import java.io.File;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;

public abstract class ConfigurablePircBot extends PircBot {
    private Configuration configuration;

    /**
     * Gets the current configuration.
     *
     * @return The current {@link Configuration}.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Initializes the bot using a configuration file specified by its path.
     *
     * @param fileName The file path of the configuration.
     * @throws Exception If an error occurs while loading or initializing the bot.
     */
    public void initBot(String fileName) throws Exception {
        initBot(new File(fileName));
    }

    /**
     * Initializes the bot using a configuration file.
     *
     * @param file The configuration file.
     * @throws Exception If an error occurs while loading or initializing the bot.
     */
    public void initBot(File file) throws Exception {
        Configurations configs = new Configurations();
        PropertiesConfiguration config = configs.properties(file);

        // Disable list delimiter parsing for backward compatibility
        config.setListDelimiterHandler(new DefaultListDelimiterHandler('\0'));

        initBot(config);
    }

    /**
     * Initializes the bot using a {@link Configuration} object.
     *
     * @param config The configuration object.
     * @throws Exception If an error occurs while initializing the bot.
     */
    public void initBot(Configuration config) throws Exception {
        this.configuration = config;

        // Configure bot settings
        if (config.containsKey("Verbose")) {
            setVerbose(config.getBoolean("Verbose"));
        }
        if (config.containsKey("Nick")) {
            setName(config.getString("Nick"));
        }
        if (config.containsKey("UserName")) {
            setUserName(config.getString("UserName"));
        }
        if (config.containsKey("RealName")) {
            setRealName(config.getString("RealName"));
        }
        if (config.containsKey("Version")) {
            setVersion(config.getString("Version"));
        }
        if (config.containsKey("Finger")) {
            setFinger(config.getString("Finger"));
        }

        // Configure server connection settings
        if (config.containsKey("Server")) {
            ConnectionSettings connectionSettings = new ConnectionSettings(config.getString("Server"));

            if (config.containsKey("Port")) {
                connectionSettings.port = config.getInt("Port");
            }
            if (config.containsKey("SSL")) {
                connectionSettings.useSSL = config.getBoolean("SSL");
            }
            if (config.containsKey("VerifySSL")) {
                connectionSettings.verifySSL = config.getBoolean("VerifySSL");
            }
            if (config.containsKey("Password")) {
                connectionSettings.password = config.getString("Password");
            }

            connect(connectionSettings);

            // Join specified channels
            if (config.containsKey("Channels")) {
                String[] channels = config.getStringArray("Channels");
                for (String channel : channels) {
                    joinChannel(channel.trim());
                }
            }
        }
    }
}

package me.leoo.bedwars.mapselector.database;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.utils.BedwarsMode;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(ConfigManager config, BedwarsMode mode) {
        HikariConfig hikariConfig = new HikariConfig();

        String storage = config.getString("map-selector.storage");

        if (storage == null || storage.equalsIgnoreCase("sqlite")) {
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + new File("plugins/" + mode.getName() + "/Cache", "datbase.db"));
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
        } else {
            YamlConfiguration configuration = mode == BedwarsMode.BEDWARS ? BedWars.config.getYml() : BedWarsProxy.config.getYml();

            hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",
                    configuration.getString("database.host"),
                    configuration.getInt("database.port"),
                    configuration.getString("database.database")
            ));
            hikariConfig.setUsername(configuration.getString("database.user"));
            hikariConfig.setPassword(configuration.getString("database.pass"));
        }

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("MapSelector-Pool");
        hikariConfig.addDataSourceProperty("useSSL", config.getBoolean("mysql.ssl"));
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("useUnicode", true);
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        dataSource = new HikariDataSource(hikariConfig);

        MapSelector.get().getLogger().info("Connected to the database");

        createTables();
    }


    public void close() {
        dataSource.close();
        MapSelector.get().getLogger().info("Closed the database connection");
    }

    /**
     * Execute sql statement
     */
    protected void execute(String sql, ThrowingConsumer<PreparedStatement> consumer) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            consumer.accept(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Create the database tables if don't exist
     */
    public void createTables() {
        execute("CREATE TABLE IF NOT EXISTS map_selector (uuid VARCHAR(50), uses INT(10))", PreparedStatement::executeUpdate);
    }

    /**
     * @param uuid of the player to check
     * @return true if uuid is registered, false otherwise
     */
    public boolean isRegistered(UUID uuid) {
        AtomicBoolean register = new AtomicBoolean(false);

        execute("SELECT uuid FROM map_selector WHERE uuid = ?", preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());

            register.set(preparedStatement.executeQuery().next());
        });

        return register.get();
    }

    /**
     * Register a player in the database
     *
     * @param uuid of the player to check
     */
    public void register(UUID uuid) {
        execute("INSERT INTO map_selector (uuid, uses) VALUES(?, ?)", preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setInt(2, 0);

            preparedStatement.executeUpdate();
            MapSelector.get().debug("Registered player with uuid " + uuid);
        });
    }

    /**
     * Check if a player is stored into database
     */
    public void checkStored(UUID uuid) {
        if (!isRegistered(uuid)) {
            register(uuid);
        }
    }

    /**
     * Set the daily uses of a player
     *
     * @param uuid of the player
     * @param uses that will be set
     */
    public void setPlayerUses(UUID uuid, int uses) {
        execute("UPDATE map_selector SET uses = ? WHERE uuid = ?", preparedStatement -> {
            preparedStatement.setInt(1, uses);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();

            MapSelector.get().debug("Set player's with uuid " + uuid + " uses to " + uses);
        });
    }

    /**
     * Set the daily uses of all the registered players
     *
     * @param uses that will be set
     */
    public void setAllPlayersUses(int uses) {
        execute("UPDATE map_selector SET uses = ?", preparedStatement -> {
            preparedStatement.setInt(1, uses);
            preparedStatement.executeUpdate();

            MapSelector.get().debug("Set all players uses to " + uses);
        });
    }

    /**
     * @param uuid of the player to check
     * @return the uses of the player
     */
    public int getPlayerUses(UUID uuid) {
        AtomicInteger uses = new AtomicInteger(0);

        execute("SELECT uses FROM map_selector WHERE uuid = ?", preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                uses.set(resultSet.getInt("uses"));
            }
        });

        return uses.get();
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws SQLException;
    }

}

package me.leoo.bedwars.mapselector.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.leoo.bedwars.mapselector.MapSelector;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class Database {

    private Connection connection;

    /**
     * @param host     of the database
     * @param port     of the database
     * @param name     of the database
     * @param username of the database
     * @param password of the database
     * @param ssl      true if ssl enabled, false otherwise
     */
    public Database(String host, int port, String name, String username, String password, boolean ssl) throws Exception {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name);
        hikariConfig.setPoolName("[" + MapSelector.getPlugin().getDescription().getName() + "-MySQL]");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMaxLifetime(1800000L);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(ssl));
        hikariConfig.addDataSourceProperty("autoReconnect", "true");
        hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30L)));

        this.connection = new HikariDataSource(hikariConfig).getConnection();
        MapSelector.getPlugin().getLogger().info("Connected to the database");

        createTables();
    }

    /**
     * @param file name of the database
     */
    public Database(String file) throws Exception {
        Class.forName("org.sqlite.JDBC");

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);

        MapSelector.getPlugin().getLogger().info("Connected to the database");

        createTables();
    }


    /**
     * Closes the database's connection
     */
    public void close() {
        if (this.connection == null) return;
        try {
            getConnection().close();
            MapSelector.getPlugin().getLogger().info("Closed the database connection");
        } catch (Exception e) {
            e.printStackTrace();
            MapSelector.getPlugin().getLogger().info("Error while trying to close the database connection");
        }
        this.connection = null;
    }

    /**
     * @return the database's connection
     */
    @NotNull
    private Connection getConnection() {
        if (connection == null) {
            MapSelector.getPlugin().connectDatabase();
        }
        return connection;
    }

    /**
     * Create the database tables if don't exist
     */
    public void createTables() throws SQLException {
        getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS map_selector (uuid VARCHAR(50), uses INT(10))");
    }

    /**
     * @param uuid of the player to check
     * @return true if uuid is registered, false otherwise
     */
    public boolean isRegistered(UUID uuid) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT uuid FROM map_selector WHERE uuid = ?")) {
            preparedStatement.setString(1, String.valueOf(uuid));
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Register a player in the database
     *
     * @param uuid of the player to check
     */
    public void register(UUID uuid) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO map_selector (uuid, uses) VALUES(?, ?)")) {
            preparedStatement.setString(1, String.valueOf(uuid));
            preparedStatement.setInt(2, 0);
            preparedStatement.executeUpdate();
            MapSelector.getPlugin().debug("Registered a new player with the uuid " + uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a player is stored into database
     */
    public void checkStored(UUID uuid){
        if(!isRegistered(uuid)){
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
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE map_selector SET uses = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, uses);
            preparedStatement.setString(2, String.valueOf(uuid));
            preparedStatement.executeUpdate();
            MapSelector.getPlugin().debug("Set player's with uuid " + uuid + " uses to " + uses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the daily uses of all the registered players
     *
     * @param uses that will be set
     */
    public void setAllPlayersUses(int uses) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE map_selector SET uses = ?")) {
            preparedStatement.setInt(1, uses);
            preparedStatement.executeUpdate();
            MapSelector.getPlugin().debug("Set all players uses to " + uses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uuid of the player to check
     * @return the uses of the player
     */
    public int getPlayerUses(UUID uuid) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT uses FROM map_selector WHERE uuid = ?")) {
            preparedStatement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("uses");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

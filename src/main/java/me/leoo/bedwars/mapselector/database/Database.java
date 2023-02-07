package me.leoo.bedwars.mapselector.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.leoo.bedwars.mapselector.Main;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Database {

	private Connection connection;

	/**
	 *
	 * @param host of the database
	 * @param port of the database
	 * @param name of the database
	 * @param username of the database
	 * @param password of the database
	 * @param ssl true if ssl enabled, false otherwise
	 */
	public Database(String host, int port, String name, String username, String password, boolean ssl) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name);
		hikariConfig.setPoolName("[" + Main.plugin.getDescription().getName() + "-MySQL]");
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

		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		this.connection = hikariDataSource.getConnection();
		Bukkit.getConsoleSender().sendMessage("[" + Main.plugin.getDescription().getName() + "-MySQL] Connected to the database" );
		createTables();
	}

	/**
	 *
	 * @param file name of the database
	 */
	public Database(String file) throws Exception {
		Class.forName("org.sqlite.JDBC");
		this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
		Bukkit.getConsoleSender().sendMessage("[" + Main.plugin.getDescription().getName() + "-SQLite] Connected to the database" );
		createTables();
	}

	/**
	 * Close the database connection if exists
	 */
	public void close(){
		if(this.connection == null)
			return;
		try {
			this.connection.close();
			Bukkit.getConsoleSender().sendMessage("[" + Main.plugin.getDescription().getName() + "] Closed the database connection" );
		}catch (Exception e){
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[" + Main.plugin.getDescription().getName() + "] Error while trying to close the database connection" );
		}
		this.connection = null;
	}

	/**
	 * Create the database tables if don't exist
	 */
	public void createTables() throws SQLException {
		String t = "CREATE TABLE IF NOT EXISTS map_selector (uuid VARCHAR(50), uses INT(10))";
		Statement statement = this.connection.createStatement();
		statement.execute(t);
	}

	/**
	 *
	 * @param uuid of the player to check
	 * @return true if uuid is registered, false otherwise
	 */
	public boolean isRegistered(UUID uuid){
		try{
			if(this.connection.isClosed()){
				Main.connectDB();
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		try(PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT uuid FROM map_selector WHERE uuid = ?")){
			preparedStatement.setString(1, String.valueOf(uuid));
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Register a player in the database
	 * @param uuid of the player to check
	 */
	public void registerPlayer(UUID uuid){
		try{
			if(this.connection.isClosed()){
				Main.connectDB();
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		try(PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO map_selector (uuid, uses) VALUES(?, ?)")){
			preparedStatement.setString(1, String.valueOf(uuid));
			preparedStatement.setInt(2, 0);
			preparedStatement.executeUpdate();
			System.out.println("[" + Main.plugin.getDescription().getName() + "] Registered the player with uuid " + uuid.toString());
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Set the daily uses of a player
	 * @param uuid of the player
	 * @param uses that will be set
	 */
	public void setPlayerUses(UUID uuid, int uses){
		try{
			if(this.connection.isClosed()){
				Main.connectDB();
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		try(PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE map_selector SET uses = ? WHERE uuid = ?")){
			preparedStatement.setInt(1, uses);
			preparedStatement.setString(2, String.valueOf(uuid));
			preparedStatement.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param uuid of the player to check
	 * @return the uses of the player
	 */
	public int getPlayerUses(UUID uuid){
		try{
			if(this.connection.isClosed()){
				Main.connectDB();
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		try(PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT uses FROM map_selector WHERE uuid = ?")){
			preparedStatement.setString(1, String.valueOf(uuid));
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return resultSet.getInt("uses");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

}

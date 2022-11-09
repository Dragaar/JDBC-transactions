package com.epam.rd.java.basic.topic07.task02.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.epam.rd.java.basic.topic07.task02.db.entity.*;
import static com.epam.rd.java.basic.topic07.task02.db.DBConstants.*;

public class DBManager {

	private final static  DBManager INSTANCE = new DBManager();
	//-------------------
	InputStream appConfigPath = getClass().getResourceAsStream("app.properties");
	Properties appProps = new Properties();
	//-------------------
	private DBManager(){
		try {
			appProps.load(new FileInputStream("app.properties")); //загрузка файлу конфігурації app.properties
		} catch (IOException e){ e.printStackTrace();}
	}
	public static synchronized DBManager getInstance() {return INSTANCE;}

	public List<User> findAllUsers() throws DBException {
		List<User> users = new ArrayList<>();
		try(Connection con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS))
		{
			while (rs.next()){
				User user = User.createUser(rs.getString(USER_LOGIN));
				user.setId(rs.getInt(USER_ID));
				users.add(user);
			}

		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		}
		return users;
	}

	public boolean insertUser(User user) throws DBException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			stmt = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getLogin());

			int countSuccessfulInsertions = stmt.executeUpdate();
			if(countSuccessfulInsertions >0){
				try(ResultSet rs = stmt.getGeneratedKeys()) {
					if(rs.next()) {
						user.setId(rs.getInt(1));
					}
				}
			}
			stmt.close();
			con.close();
			if(countSuccessfulInsertions >0){return true; } else return false;
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
	}

	public boolean deleteUsers(User... users) throws DBException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			stmt = con.prepareStatement(DELETE_USERS);

			for(User user : users){
				stmt.setInt(1, user.getId());
				stmt.addBatch();
			}

			int[] DeletesResults = stmt.executeBatch();
			//перевірка кількості успішних видалень та видалення DAO користувачів
			for(int i = 0; i < DeletesResults.length; i++) {
				if (DeletesResults[i] > 0) {
					users[i] = null;
				} else return false;
			}
			con.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(con);
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}

	}

	public User getUser(String login) throws DBException {
		User user = null;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			stmt = con.prepareStatement(SELECT_USER);

			stmt.setString(1, login);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				user = User.createUser(rs.getString(USER_LOGIN));
				user.setId(rs.getInt(USER_ID));
			}
			stmt.close();
			con.close();
			return user;
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
	}

	public Team getTeam(String name) throws DBException {
		Team team = null;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			stmt = con.prepareStatement(SELECT_TEAM);
			stmt.setString(1, name);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				team = Team.createTeam(rs.getString(TEAM_NAME));
				team.setId(rs.getInt(TEAM_ID));
			}
			stmt.close();
			con.close();
			return team;
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
	}

	public List<Team> findAllTeams() throws DBException {
		List<Team> teams = new ArrayList<>();
		try(Connection con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_TEAMS))
		{
			while (rs.next()){
				Team team = Team.createTeam(rs.getString(TEAM_NAME));
				team.setId(rs.getInt(TEAM_ID));
				teams.add(team);
			}
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		}
		return teams;
	}

	public boolean insertTeam(Team team) throws DBException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			stmt = con.prepareStatement(INSERT_TEAM, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, team.getName());

			int countSuccessfulInsertions = stmt.executeUpdate();
			if(countSuccessfulInsertions >0){
				try(ResultSet rs = stmt.getGeneratedKeys()) {
					if(rs.next()) {
						team.setId(rs.getInt(1));
					}
				}
			}
			stmt.close();
			con.close();
			if(countSuccessfulInsertions >0){return true; } else return false;
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
	}

	public boolean setTeamsForUser(User user, Team... teams) throws DBException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			stmt = con.prepareStatement(INSERT_TEAM_FOR_USER);

			for(Team team : teams){
				stmt.setInt(1, user.getId());
				stmt.setInt(2, team.getId());
				stmt.addBatch();
			}

			int[] insertResults = stmt.executeBatch();
			//перевірка кількості успішних вставок
			for(int i = 0; i < insertResults.length; i++) {
				if (insertResults[i] <= 0) {
					return false;
				}
			}
			con.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(con);
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
	}

	public List<Team> getUserTeams(User user) throws DBException {
		List<Team> teams = new ArrayList<>();
		Connection con = null;
		PreparedStatement stmt = null;
		try{
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			//запит на вибірку id команд, які підв'язані до користувача
			stmt = con.prepareStatement(SELECT_USER_TEAMS);

			stmt.setInt(1, user.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				//запит на вибірку команди за знайденим id
				stmt = con.prepareStatement(SELECT_TEAM_BY_ID);
				stmt.setInt(1, rs.getInt(USER_TEAMS_ID));

				ResultSet rs2 = stmt.executeQuery();

				while (rs2.next()){
					Team team = Team.createTeam(rs2.getString(TEAM_NAME));
					team.setId(rs2.getInt(TEAM_ID));
					teams.add(team);
				}
			}
			con.commit();
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
		return teams;
	}

	public boolean deleteTeam(Team team) throws DBException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			stmt = con.prepareStatement(DELETE_TEAM);
			stmt.setInt(1, team.getId());

			int countSuccessfulInsertions = stmt.executeUpdate();

			stmt.close();
			con.close();
			if(countSuccessfulInsertions >0){ team = null; return true; } else return false;
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
	}

	public boolean updateTeam(Team team) throws DBException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DriverManager.getConnection(appProps.getProperty("connection.url"));
			stmt = con.prepareStatement(DELETE_TEAM);
			stmt.setInt(1, team.getId());

			int countSuccessfulInsertions = stmt.executeUpdate();


			if(countSuccessfulInsertions >0){ team = null; return true; } else return false;
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException();
		} finally {
			close(stmt);
			close(con);
		}
	}

	private void close( AutoCloseable stmt){
		if(stmt != null){
			try{
				stmt.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	private void rollback(Connection con) {
		if(con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

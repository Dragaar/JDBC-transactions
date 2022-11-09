package com.epam.rd.java.basic.topic07.task02.db;

public class DBConstants {

	public static final String USER_ID = "id";

	public static final String USER_LOGIN = "login";

	public static final String TEAM_ID = "id";

	public static final String TEAM_NAME = "name";

	public static final String USER_TEAMS_ID = "team_id";

	//-----------------------------
	public static final String SELECT_ALL_USERS = "SELECT * FROM users" ;
	public static final String SELECT_USER = "SELECT * FROM users WHERE login LIKE ?";

	public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?)" ;

	public static final String DELETE_USERS = "DELETE FROM users WHERE id IN ?" ;
	//-----------------------------
	public static final String SELECT_ALL_TEAMS= "SELECT * FROM teams";
	public static final String SELECT_TEAM = "SELECT * FROM teams WHERE name LIKE ?" ;
	public static final String SELECT_TEAM_BY_ID = "SELECT * FROM teams WHERE id = ?" ;

	public static final String INSERT_TEAM = "INSERT INTO teams VALUES (DEFAULT, ?)" ;

	public static final String DELETE_TEAM = "DELETE FROM teams WHERE id = ?" ;

	public static final String UPDATE_TEAM = "DELETE FROM teams WHERE id = ?" ;

	//-----------------------------

	public static final String INSERT_TEAM_FOR_USER = "INSERT INTO users_teams VALUES (?, ?)";

	public static final String SELECT_USER_TEAMS = "SELECT team_id FROM users_teams WHERE user_id = ?" ;
}

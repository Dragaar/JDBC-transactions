� �������� ���� ������ ������������ ����������� ��.
�� �������� ��� �������:
```
users (id, login)
teams (id, name)
users_teams (user_id, team_id)
```

���������� ������� �� ������ ����� ��������� ���������� (��. ��� ������ Demo)
� ����� ������� ������� sql � ��������� � ��� ������ �������� ���� ������ `db-create.sql`

������� � ����������� ���� ����� �������, ����� ��� ������� ������ Demo 
������������ ��������������� ����������������.
--------------------------------------------------
�����: `com.epam.rd.java.basic.topic07.task02`
������: 
Demo - �������� ������������ �����������, �������� ��� ���������.
��������� ������ ��. �����������.
--------------------------------------------------
���������� ������ Demo:
```
public class Demo {

	public static void main(String[] args) throws DBException {
		// users  ==> [ivanov petrov obama]
		// teams  ==> [teamA teamB teamC ]
		
		DBManager dbManager = DBManager.getInstance();

		User userPetrov = dbManager.getUser("petrov");
		User userIvanov = dbManager.getUser("ivanov");
		User userObama = dbManager.getUser("obama");

		Team teamA = dbManager.getTeam("teamA");
		Team teamB = dbManager.getTeam("teamB");
		Team teamC = dbManager.getTeam("teamC");

		// method setTeamsForUser must implement transaction!
		dbManager.setTeamsForUser(userIvanov, teamA);
		dbManager.setTeamsForUser(userPetrov, teamA, teamB);
		dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);

		for (User user : dbManager.findAllUsers()) {
			System.out.println((dbManager.getUserTeams(user)));
		}
		// teamA
		// teamA teamB
		// teamA teamB teamC
	}
}
```
(1) ����� DBManager#`setTeamsForUser(User, Team...)` ����������� � ������� 
����������: ���������� ������ ������� ������ ������������ ����� ��������� ���� 
��� ������, ���������� � ������ ����������, ���� �� ���� �� ���.

���� ����� ����� ������ ���: `setTeamsForUser(user, teamA, teamB, teamC)`,
�� � ������� ������ `users_teams` ������ ������ ���� ��������� ��������������� 
� ������� ��������� ����� � ������ ���������� ����� �������:
```
user_id, teamA_id
user_id, teamB_id 
user_id, teamC_id
```
���� ��������� ������ �� ����� ���� ���������, �� ������ ��� ����� �� ������ �������
� ���� ������.

(2) ����� DBManager#`getUserTeams` ���������� ������ `java.util.List<Team>`.

(3) ����� DBManager#`insertUser` ������ �������������� ���� id ������� User.

(4) ����� DBManager#`findAllUsers` ���������� ������ `java.util.List<User>`.

(5) ����� DBManager#`insertTeam` ������ �������������� ���� id ������� Team.

(6) ����� DBManager#`findAllTeams` ���������� ������ `java.util.List<Team>`.

##### ���������.

����� User ������ ���������:
- ����� `getLogin()`, ������� ���������� ����� ������������;
- ����� `toString()`, ������� ���������� ����� ������������;
- ���������� ������ `equals(Object obj)`, �������� ������� ��� ������� User 
����� ����� � ������ �����, ����� ��� ����� ���� �����;
- ����������� ����� `createUser(String login)`, ������� ������� ������ User �� 
������ (������������� ����� 0).

����� Team ������ ���������:
- ����� `getName()`, ������� ���������� �������� ������;
- ����� `toString()`, ������� ���������� �������� ������;
- ���������� ������ `equals(Object obj)`, �������� ������� ��� ������� Team 
����� ����� � ������ �����, ����� ��� ����� ���� ��������.
- ����������� ����� `createTeam(String name)`, ������� ������� ������ Team �� 
����� (������������� ����� 0).

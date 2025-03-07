package FootballApp.utility;

import FootballApp.databases.ManagerDB;
import FootballApp.databases.PlayerDB;
import FootballApp.databases.TeamDB;
import FootballApp.entities.Manager;
import FootballApp.entities.Observer;
import FootballApp.entities.Player;
import FootballApp.entities.Team;
import FootballApp.entities.attributes.TechnicalAttributes;
import FootballApp.enums.EPosition;

import java.io.*;
import java.util.*;

public class DataIO implements Observer {
	public static TeamDB teamDB = new TeamDB();
	public static ManagerDB managerDB = new ManagerDB();
	public static PlayerDB playerDB = new PlayerDB();
	
	static File file = new File("teams.txt");
	static File file2 = new File("managers.txt");
	static File file3 = new File("players.txt");
	
	public static void dataIOInitialize() {
		if (!file.exists() || file.length() == 0 && !file2.exists() || file2.length() == 0 && !file3.exists() || file3.length() == 0) {
			savePlayersToFile();
			saveTeamsToFile();
			saveManagersToFile();

		}
		generateTeams();
		generateManagers();
		generatePlayers();
	}
	
	public static void saveTeamsToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("teams.txt"))) {
			for (Team team : teamDB.listAll()) {
				writer.write(team.getTeamName()+ ","
				             +new ArrayList<>()+","
				             +team.getTeamLocation()+","
				             +team.getStadiumName()+","
				             +team.getTransferBudget()+","
				             +team.getWageBudget()+","
				             +"\n");
			}
		}
		catch (IOException e) {
			System.err.println("Error while writing teams.txt: " + e.getMessage());
		}
	}
	
	public static void saveManagersToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("managers.txt"))) {
			for (Manager manager : managerDB.listAll()) {
				writer.write(manager.getCurrentTeamID()+ ","
						+manager.getName() + ","
						+ manager.getSurName() + ","
						+ manager.getAge() + ","
						+ manager.getNationality() + ","
						+ manager.getManagerUserName() + ","
						+ manager.getManagerPassword()+","
						+ "\n");
				
			}
		}
		catch (IOException e) {
			System.err.println("Error while writing teams.txt: " + e.getMessage());
		}
	}
	
	public static void savePlayersToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("players.txt"))) {
			for (Player player : playerDB.listAll()) {
				TechnicalAttributes ta=player.getPlayerTechnicalAttributes();
				writer.write(player.getName() + ","
						             + player.getSurName() + ","
						             + player.getAge() + ","
						             + player.getNationality() + ","
						             + ta.getFinishing() + ","
									 + ta.getPass() + ","
						             + ta.getDribbling() + ","
						             + ta.getTackle() + ","
						             + ta.getShotPower() + ","
						             + player.getCurrentTeamID() + ","
						             + player.getPlayerValue() + ","
						             + player.getPlayerWage() + ","
						             + player.getPlayersPosition() + ","
						             + "\n");
			}
		}
		catch (IOException e) {
			System.err.println("Error while writing players.txt: " + e.getMessage());
		}
	}
	
	public static List<Player> generatePlayers() {
		List<Player> players = new ArrayList<>();
		try (Scanner sc = new Scanner(new FileReader("players.txt"))) {
			while (sc.hasNextLine()) {
				String satir = sc.nextLine();
				String[] split = satir.split(",");
				
				TechnicalAttributes ta = new TechnicalAttributes(
						Integer.parseInt(split[4]),
						Integer.parseInt(split[5]),
						Integer.parseInt(split[6]),
						Integer.parseInt(split[7]),
						Integer.parseInt(split[8])
				);
				
				Player player = new Player(
						split[0],
						split[1],
						Integer.parseInt(split[2]),
						split[3],
						ta,
						Integer.parseInt(split[9]),
						Double.parseDouble(split[10]),
						Double.parseDouble(split[11]),
						EPosition.valueOf(split[12])
				);
				players.add(player);
			}
			playerDB.saveAll(players);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error parsing player data: " + e.getMessage());
		}
		return players;
	}
	
	public static List<Team> generateTeams() {
		List<Team> teams = new ArrayList<>();
		try (Scanner sc = new Scanner(new FileReader("teams.txt"))) {
			while (sc.hasNextLine()) {
				String satir = sc.nextLine().trim();
				String[] split = satir.split(",");
				
				try {
					String teamName = split[0];
					String city = split[1];
					String stadiumName = split[2];
					
					double transferBudget = Double.parseDouble(split[3]);
					double wageBudget = Double.parseDouble(split[4]);
					
					Team team = new Team(
							teamName,
							city,
							stadiumName,
							transferBudget,
							wageBudget
					);
					teams.add(team);
				} catch (NumberFormatException e) {
					System.err.println("Error parsing number from line: " + satir);
				}
			}
			teamDB.saveAll(teams);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error parsing team data: " + e.getMessage());
		}
		return teams;
	}
	
	
	public static List<Manager> generateManagers() {
		List<Manager> managers = new ArrayList<>();
		try (Scanner sc = new Scanner(new FileReader("managers.txt"))) {
			while (sc.hasNextLine()) {
				String satir = sc.nextLine();
				String[] split = satir.split(",");
				
				
				Manager manager = new Manager(
						Integer.parseInt(split[0]),
						split[1],
						split[2],
						Integer.parseInt(split[3]),
						split[4],
						split[5],
						split[6]
				);
				managers.add(manager);
			}
			managerDB.saveAll(managers);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error parsing manager data: " + e.getMessage());
		}
		return managers;
	}
	
	
	@Override
	public void update() {
		savePlayersToFile();
		saveTeamsToFile();
		saveManagersToFile();
	}
}
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Server {

	private final static int port = 8888;

	public static void main(String[] args) {

		HashMap<String, HashMap<String, String>> games = new HashMap<String, HashMap<String, String>>(20);
		String message = null;
		String response = null;

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("I/O error: " + e.getMessage());
		}

		Socket clientSocket = null;

		PrintWriter out = null;
		BufferedReader in = null;

		while (true) {
			System.out.println("Ready");
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Client IP: " + clientSocket.getInetAddress());

				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				// Read message from server
				// Format: "command; game ID; playerID/(playerID1,
				// playerID2...)"
				message = in.readLine();
				System.out.println("Command received: " + message);

			} catch (IOException e) {
				System.out.println("I/O error: " + e.getMessage());
			}

			// "0" (start new game), "1" (continue - get target), or "2"
			// (continue - report death)
			String command = message.substring(0, message.indexOf(';'));

			// parse message to "game ID; playerID/playerList"
			message = message.substring(message.indexOf(';') + 2);

			// game ID
			String gameID = message.substring(0, message.indexOf(';'));

			// parse message to "playerID/playerList"
			message = message.substring(message.indexOf(';') + 2);

			// create a new game
			if (command.equals("0")) {
				if (games.containsKey(gameID))
					response = "This game ID is already in use. Please try again with a different one.";
				else {
					// split player list into array for this game
					String[] players = message.split(", ");

					// randomize array for this game
					Random r = new Random();
					for (int i = 0; i < players.length; i++) {
						int random = r.nextInt(players.length);
						String temp = players[i];
						players[i] = players[random];
						players[random] = temp;
					}

					// put array for this game into HashMap
					HashMap<String, String> tmp = new HashMap<String, String>(players.length);
					for (int i = 0; i < players.length; i++) {
						// for last entry, set target to first entry
						if (i == players.length - 1)
							tmp.put(players[players.length - 1], players[0]);

						// otherwise, set target to following entry
						else
							tmp.put(players[i], players[i + 1]);
					}

					// save HashMap for this game to HashMap of games
					games.put(gameID, tmp);

					response = "Your game has been created.";
				}
			}
			// continue game (get target's name or report death)
			else {
				// check if this game exists
				if (!games.containsKey(gameID)) {
					response = "No game exists with this ID. Please try again.";
					// game exists
				} else {
					// get HashMap for this game
					HashMap<String, String> tmp = games.get(gameID);

					// check if this player exists
					if (!tmp.containsKey(message))
						response = "No player exists with this name. Please try again.";
					// player exists
					else {
						// get player's target
						if (command.equals("1")) {
							// check if no targets remain
							if (tmp.size() == 1) {
								response = "Congratulations! You are the winner.";
								games.remove(gameID);
							} else
								response = "Your target is: " + tmp.get(message);
						}
						// remove player from game
						else if (command.equals("2")) {
							// find killer in HashMap
							String killer = null;
							for (Entry<String, String> entry : tmp.entrySet()) {
								if (entry.getValue().equals(message)) {
									killer = entry.getKey();
								}
							}
							// replace killer's target with player's target
							tmp.replace(killer, tmp.get(message));
							// remove player from HashMap
							tmp.remove(message);
							// update saved game data
							games.replace(gameID, tmp);

							response = "Thanks for playing.";
						}
					}
				}
			}
			System.out.println("Sending response...");
			out.write(response);
			System.out.println("Response sent: " + response);
			
			// Make sure socket is closed
			if (clientSocket != null && !clientSocket.isClosed()) {
				try {
					clientSocket.close();
				} catch (IOException e) {
					System.out.println("I/O error: " + e.getMessage());
				}
			}
		}
	}
}
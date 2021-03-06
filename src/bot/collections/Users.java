package bot.collections;

import bot.botType.Command;
import bot.botType.Message;
import bot.log.Log;
import bot.functions.Sender;
import bot.botType.User;
import bot.translation.Sentences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Paolo on 13/10/2015.
 * List that contains all users that write to bot.
 */
public class Users
{
	private static File usersFile = new File("config/users.json");
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static HashMap<Long, User> users = new HashMap<>();

	/**
	 * Add User to users list
	 *
	 * @param usr
	 * @return true if it has been added
	 */
	public static void addUser(User usr)
	{
		users.put(usr.getSenderId(), usr);
	}

	/**
	 * Return user from userList
	 *
	 * @param senderId, unique id for sender
	 * @return User if it has been found
	 */
	public static User getUser(long senderId)
	{
		return users.get(senderId);
	}

	/**
	 * Create a new file for users
	 */
	public static void createUserFile()
	{
		if (usersFile.exists()) return;
		JSONObject obj = new JSONObject();
		try
		{
			FileWriter outFile = new FileWriter(usersFile);
			outFile.write(gson.toJson(obj));
			Log.info("Creato file Users");
			outFile.flush();
			outFile.close();
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	/**
	 * Add user to external file
	 *
	 * @param usr
	 */
	public static void addUserToFile(User usr)
	{
		if (!usersFile.exists()) createUserFile();
		if (userExist(usr)) return;
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			obj = (JSONObject) parser.parse(new FileReader(usersFile));
			JSONObject users = (JSONObject) obj.getOrDefault(usr.getSenderId(), new JSONObject());
			users.put("First_Name", usr.getFirst_name());
			users.put("Last_Name", usr.getLast_name());
			users.put("Username", usr.getUsername());
			users.put("Ban", usr.isBan());
			users.put("Conditions", usr.getTimeFromLastTerms());
			users.put("Subscribed", usr.isSubscrived());
			obj.put(usr.getSenderId(), users);

			FileWriter outFile = new FileWriter(usersFile);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
		}
	}

	public static boolean userExist(User usr)
	{
		return users.containsKey(usr.getSenderId());
	}

	/**
	 * Load users from saved file
	 *
	 * @return true if users have been loaded
	 */
	public static boolean loadUsers()
	{
		if (!usersFile.exists()) createUserFile();
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try
		{
			int i = 0;
			obj = (JSONObject) parser.parse(new FileReader(usersFile));
			ArrayList<String> keyList = new ArrayList<String>(obj.keySet());
			for (; i < keyList.size(); i++)
			{
				JSONObject user = (JSONObject) obj.get(keyList.get(i));
				String firstName = (String) user.get("First_Name");
				String lastName = (String) user.get("Last_Name");
				String username = (String) user.get("Username");
				Long conditions = (Long) user.get("Conditions");
				boolean ban = (Boolean) user.get("Ban");
				boolean subscribed = (Boolean) user.get("Subscribed");
				User newUser = new User(Long.parseLong(keyList.get(i)),firstName,lastName,username);
				newUser.setBan(ban);
				newUser.setSubscrived(subscribed);
				newUser.setTimeFromLastTerms(conditions);
				addUser(newUser);
			}
			Log.info("Users[" + i + "] have been loaded");
		}
		catch (IOException | ParseException e)
		{
			Log.stackTrace(e.getStackTrace());
			return false;
		}
		return true;
	}

	/**
	 * Save all users in file
	 * @return true if users have been saved
	 */
	public static boolean saveUsers()
	{
		try
		{
			JSONObject obj = new JSONObject();
			ArrayList<Long> keyList = new ArrayList<Long>(users.keySet());
			for(int i=0; i<keyList.size();i++)
			{
				User usr = users.get(keyList.get(i));
				JSONObject users = new JSONObject();
				users.put("First_Name", usr.getFirst_name());
				users.put("Last_Name", usr.getLast_name());
				users.put("Username", usr.getUsername());
				users.put("Ban", usr.isBan());
				users.put("Conditions", usr.getTimeFromLastTerms());
				users.put("Subscribed", usr.isSubscrived());
				obj.put(usr.getSenderId(), users);
			}

			FileWriter outFile = new FileWriter(usersFile,false);
			outFile.write(gson.toJson(obj));
			outFile.flush();
			outFile.close();
			Log.info(Sentences.USERS_SAVED.getSentence());
			return true;
		}
		catch (IOException e)
		{
			Log.stackTrace(e.getStackTrace());
			Log.error(Sentences.USERS_NOT_SAVED.getSentence());
			return false;
		}
	}

	/**
	 * Load some commands for users management
	 */
	public static void loadUsersCommand()
	{
		Command ban = new Command("ban", "bot.collections.Users", "banUser");
		ban.setHidden(true);
		Commands.addCommand(ban);
	}

	/**
	 * Ban user from this bot
	 * @param msg in field text of message, there should be the sender id of the user to ban.
	 */
	public static void banUser(Message msg)
	{
		if (msg.getText() != null && Owners.isOwner(msg.getUserFrom().getSenderId()))
		{
			String[] text = msg.getText().split(" ");
			if (text.length >= 2)
			{
				User usr = getUser(Long.parseLong(text[1]));
				if (usr != null)
				{
					usr.setBan(!usr.isBan());
					Log.info("User has been banned: " + usr.getSenderId());
				}
				else if (msg.getChat().getType().equals("group"))
				{
					Sender.sendMessage(msg.getChat().getId(), "User doesn't exist", msg.getUserFrom().getSenderId());
				}
				else
				{
					Sender.sendMessage(msg.getChat().getId(), "User doesn't exist");
				}
			}
			else
			{
				if (msg.getChat().getType().equals("group"))
				{
					Sender.sendMessage(msg.getChat().getId(), "Bad parameter", msg.getUserFrom().getSenderId());
				}
				else
				{
					Sender.sendMessage(msg.getChat().getId(), "Bad parameter");
				}
			}

		}
	}
}

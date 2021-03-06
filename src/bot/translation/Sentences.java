package bot.translation;

import bot.Setting;
import org.omg.CORBA.UNKNOWN;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Paolo on 31/10/2015.
 */
public enum Sentences
{
	NULL("?"),
	MESSAGE_RECEIVED("message_received"),
	FROM("from"),
	NEW_USER("new_user"),
	HAS_CONNECTED("has_connected"),
	CONDITIONS("conditions1"),
	CONDITION_REQUEST("conditions_request"),
	MESSAGE_NOT_SENT("message_not_sent"),
	USERS_NOT_SAVED("users_not_saved"),
	MESSAGES_NOT_SAVED("messages_not_saved"),
	USERS_SAVED("users_saved"),
	MESSAGES_SAVED("messages_saved"),
	BOT_IS_IN_MAINTENANCE("bot_is_maintenance"),
	EMPTY_MESSAGE("empty_message"),
	UNKNOWN_COMMAND("unknown_command"),
	NOT_WAITING_PARAM("not_waiting_param"),
	WAITING_FOR_OCOMMAND("waiting_for_Ocommand"),
	OPERATION_DELETED("operation_deleted");

	private String sentence = "";
	private static HashMap<String, String> sentences = new HashMap<String, String>();

	Sentences(String x)
	{
		sentence = x;
	}
	public String getSentence()
	{
		return sentences.get(sentence);
	}

	public static HashMap<String, String> getSentences()
	{
		return sentences;
	}
}

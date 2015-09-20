package bot;

import java.io.IOException;

import bot.functions.Keyboard;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Sender
{
	/**
	 * @param chatId  Chat id del destinatario
	 * @param message Messaggio da inviare
	 * @return true se l'invio � stato effettuato con successo, altrimenti false
	 * @deprecated * Metodo per inviare messaggi
	 */
	public static boolean sendMessage(int chatId, String message)
	{
		return sendMessage((long) chatId, message);
	}

	/**
	 * @param chatId
	 * @param message
	 * @param keyboard
	 * @return
	 * @deprecated
	 */
	public static boolean sendMessage(int chatId, String message, Keyboard keyboard)
	{
		return sendMessage((long) chatId, message + "&reply_markup=" + keyboard.toJSONString());
	}

	/**
	 * Send message with customized keyboard
	 *
	 * @param chatId
	 * @param message
	 * @param keyboard
	 * @return true if message has been sent
	 */
	public static boolean sendMessage(long chatId, String message, Keyboard keyboard)
	{
		return sendMessage((long) chatId, message + "&reply_markup=" + keyboard.toJSONString());
	}

	/**
	 * Metodo per inviare messaggi
	 *
	 * @param chatId  Chat id del destinatario
	 * @param message Messaggio da inviare
	 * @return true se l'invio � stato effettuato con successo, altrimenti false
	 */
	public static boolean sendMessage(long chatId, String message)
	{
		try
		{
			Document doc = Jsoup.connect(Main.getUrl() + "/sendMessage" + "?chat_id=" + chatId + "&text=" + message).ignoreContentType(true).post();
			if (doc.text().contains("\"ok\":true"))
			{
				if (message != null)
					Log.info("Messaggio inviato: " + message.split("&")[0]);
				else
					Log.warn("Message not sent, text is empty");
				return true;
			}
			else
			{
				Log.error("Messaggio non inviato");
				return false;
			}
		}
		catch (IOException e)
		{
			Log.error("Messaggio non inviato");
			return false;
		}
	}
}

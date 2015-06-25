import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Sender 
{
	/**
	 * Metodo per inviare messaggi
	 * @param chatId - Chat id del destinatario
	 * @param message - Messaggio da inviare
	 * @return true se l'invio � stato effettuato con successo, altrimenti false
	 */
	public static boolean sendMessage(int chatId, String message)
	{
		try {
			Document doc = Jsoup.connect(Main.getUrl() + "/sendMessage" + "?chat_id=" + chatId + "&text=" + message).ignoreContentType(true).post();
			if(doc.text().contains("\"ok\":true")) return true;
			else return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}

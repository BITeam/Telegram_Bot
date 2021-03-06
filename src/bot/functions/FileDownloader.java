package bot.functions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import bot.log.DownloadedFileLogger;
import bot.Main;
import org.apache.commons.io.FileUtils;
import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.vhs.YouTubeMPGParser;
import bot.log.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FileDownloader
{

	/**
	 * Download a file from url
	 *
	 * @param url - File to be downloaded
	 * @return name of file, it download fails return null
	 */
	public static File downloadFile(String url)
	{
		final String urlF = url;
		try
		{
			String[] urlPart = urlF.split("/");
			String fileName = urlPart[urlPart.length - 1];
			URL link = new URL(urlF);
			File file = new File("tmp/" + fileName);
			FileUtils.copyURLToFile(link, file);
			Log.info("Download Finished");
			return file;
		}
		catch (IOException e)
		{
			Log.error("Download Error");
			Log.stackTrace(e.getStackTrace());
			return null;
		}
	}

	/**
	 * Download video from youtube
	 *
	 * @param url Youtube url
	 * @return name of video
	 */
	public static File downloadVideo(String url)
	{
		try
		{
			VGet v = new VGet(new URL(url), new File("tmp/"));
			VGetParser user = new YouTubeMPGParser();
			v.download(user);
			DownloadedFileLogger.addYoutubeLink(url, "tmp/" + v.getTarget().getName());
			return v.getTarget();
		}
		catch (Exception | NoClassDefFoundError e)
		{
			Log.stackTrace(e.getStackTrace());
			return null;
		}
	}

	/**
	 * Download a file form telegram with the given fileId
	 *
	 * @param fileId file to download
	 * @return File name
	 */
	public static File downloadFileFormTelegram(String fileId)
	{
		return downloadFile(Main.getUrl() + "/" + getDownloadLink(fileId));
	}

	/**
	 * Get the download link for a telegram file to download
	 *
	 * @param fileId file id
	 * @return file_path
	 */
	public static String getDownloadLink(String fileId)
	{
		String receivedJSON = "";
		JSONParser jsonParser = new JSONParser();
		try
		{
			Document doc = Jsoup.connect(Main.getUrl() + "/getFile" + "?file_id=" + fileId).ignoreContentType(true).post();
			receivedJSON = doc.text();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(receivedJSON);
			return jsonObject.get("file_path").toString();
		}
		catch (Exception e)
		{
			Log.stackTrace(e.getStackTrace());
		}

		return null;
	}

}


package bot.telegramType;

/**
 * Created by Utente on 13/10/2015.
 */
public class ReplyKeyboardHide
{
	private boolean selective = true;

	public ReplyKeyboardHide(boolean selective)
	{
		 this.selective = selective;
	}

	public boolean isSelective()
	{
		return selective;
	}

	public void setSelective(boolean selective)
	{
		this.selective = selective;
	}

	public boolean isHIDEKEYBOARD()
	{
		return true;
	}
}

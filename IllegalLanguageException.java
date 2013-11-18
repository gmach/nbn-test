

public class IllegalLanguageException extends Exception
{

	private static final long	serialVersionUID	= 4092306642235387697L;
	
	public IllegalLanguageException(String languageName, String word)
	{
		super(languageName + " failed for " + word + " : ");

	}

	public IllegalLanguageException(String word2)
	{
	}

}

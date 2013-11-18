import java.util.ArrayList;
import java.util.List;

/*
 * Author: Gavin Mach
 */
public class Language
{
	private List<String> dictionary;
	private String languageName;
	
	public Language(String languageName)
	{
		this.languageName = languageName;
		dictionary = new ArrayList<String>();
	}
	
	public void addWords(String word) throws IllegalLanguageException {
		if (word.contains(LanguageIdentifier.ILLEGAL_CHARS)) {
			throw new IllegalLanguageException(languageName, word);
		}
		word = word.toLowerCase();
		if (!dictionary.contains(word)) {
			dictionary.add(word);
		}
	}
	
	boolean includes(String word) {
		return dictionary.contains(word.toLowerCase());
	}
	
	public String getLanguageName() {
		return languageName;
	}

}

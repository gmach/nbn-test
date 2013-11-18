import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Author: Gavin Mach
 */
public class Languages
{
	private Map<String, Language> languages;
	private static final String SEED_FILENAME_REGEX = "[A-Za-z]+.\\d";

	public void buildWithSeeds(String languageFilesLoc)
	{
		languages = new HashMap<String, Language>();
		File dir = new File(languageFilesLoc);
		File[] seeds = dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.matches(SEED_FILENAME_REGEX);
			}
		});
		
		for (File file: seeds) {
			try {
				addWordsFromFile(file);
			} catch (IllegalLanguageException ile) {
				System.err.println("Error : Seed File " + file.getName() + " has some illegal characters!");
			}
		}
		
	}

	private void addWordsFromFile(File file) throws IllegalLanguageException
	{
		int posExt = file.getName().indexOf(".");
		String languageName = file.getName().substring(0, posExt);
		addWordsToLanguage(languageName, file);		
	}

	private void addWordsToLanguage(String languageName, File file) throws IllegalLanguageException
	{
		Language language = null;
		if (languages.containsKey(languageName)) {
			language = languages.get(languageName);
		} else {
			language = new Language(languageName);
		}
		
		BufferedReader br = null;
		String line = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while ( (line = br.readLine()) != null) {
				String[] words = line.split(LanguageIdentifier.DELIMITERS);
				for (String word: words) {
			       	word = word.toLowerCase();
			        if (word.equals("")) {
			       		continue;
			       	}
			   		if (word.contains(LanguageIdentifier.ILLEGAL_CHARS)) {
			   			throw new IllegalLanguageException(word);
			   		} 
			   		language.addWords(word);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}       

        languages.put(languageName, language);
	}
	
	public Map<String, Language> getLanguages() {
		return languages;
	}

}

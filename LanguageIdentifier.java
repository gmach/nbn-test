import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/*
 * Author: Gavin Mach
 */
public class LanguageIdentifier
{
	public static final String LANGUAGE_FILES_LOC =  "seeds";
	public static final String ILLEGAL_CHARS = "[^A-Za-z.,:;\\s]+";
	public static final String DELIMITERS = "[.,:;\\s]";
	Languages languages;

	public static void main(String [] args)
	{
		if (args.length != 1) {
			System.out.println("Must specify filename to guess !");
			System.exit(-1);
		}
		try
		{
			new LanguageIdentifier().guessLanguage(args[0]);
		}
		catch (IllegalLanguageException e)
		{
			System.err.println("Error : Input File " + args[0] + " has some illegal characters!");
		}

	}
	
	public LanguageIdentifier() {
		initSeeds();
	}
	
	private void initSeeds() {
		languages = new Languages();
		languages.buildWithSeeds(LANGUAGE_FILES_LOC);
	}
	
	private void guessLanguage(String fileName) throws IllegalLanguageException 
	{
		Map<Language, Integer> languageWeights = new HashMap<Language, Integer>();
		Map<String, Language> languageMap = languages.getLanguages();
		for (Map.Entry<String, Language> entry : languageMap.entrySet()) {
			Language language = entry.getValue();
			languageWeights.put(language, 0);
		}
		
		BufferedReader br = null;
		String line = null;
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
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
			   		for (Map.Entry<String, Language> entry : languageMap.entrySet()) {
			   			Language language = entry.getValue();
			   			if (language.includes(word)) {
			   				int numOccurences = languageWeights.get(language);
			   				numOccurences++;
			   				//System.out.println(language.getLanguageName() + " has " + word + " " + numOccurences + " times");
			   				languageWeights.put(language, numOccurences);
			   			}
			   		}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}  
		        
        int max = 0;
        Language languageHighestOccurence = null;
		for (Map.Entry<Language, Integer> entry : languageWeights.entrySet()) {
			int numOccurences = entry.getValue();
			if (numOccurences > max) {
				max = numOccurences;
				languageHighestOccurence = entry.getKey();
			}
		}

		System.out.println("The language of the file " + fileName + " is " + (max > 0 ? languageHighestOccurence.getLanguageName():"UNKNOWN"));
	}

}

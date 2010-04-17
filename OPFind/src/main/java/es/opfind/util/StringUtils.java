package es.opfind.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String buildLuceneAndQuery(String query) {
		char[] charArray = query.toCharArray();

		ArrayList<Integer> integers = new ArrayList<Integer>();
		ArrayList<Integer> quotes = new ArrayList<Integer>();
		Boolean quote = false;

		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (c == ' ' && quote == false)
				integers.add(i + (integers.size() * 5));
			else if (c == '"') {
				quote = !quote;
				quotes.add(i);
			}
		}

		StringBuffer stringBuffer = new StringBuffer(query);
		if (quote) {
			stringBuffer.deleteCharAt(quotes.get(quotes.size() - 1)).toString();
		}

		for (Integer integer : integers) {
			stringBuffer.insert(integer, " AND ");
		}

		return stringBuffer.toString();
	}

	public static String lettersOnly(String s) {
		return s.replaceAll("[^a-zA-Z\\s]", "");
	}

	public static Integer initOrFifty(Integer match) {
		if (match - 50 < 0)
			return 0;
		else
			return (match - 50);

	}

	public static Integer endOrfiFifty(String text, Integer match) {
		if ((match + 50) > text.length())
			return text.length();
		else
			return (match + 50);
	}

	public static String getSummary(String search, String fullHtml) {
		String foundText;
		String[] words = search.split("AND");
		foundText = "";
		int j = 4;
		String fullTextAux = fullHtml.replaceAll("\\<.*?>", "");
		while (j > 0) {
			boolean found = false;
			for (int i = 0; i < words.length; i++) {
				if (j > 0) {
					String word = words[i];

					Pattern pattern = Pattern.compile("." + (word.trim().replaceAll("\"", "")) + ".",
							Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					Matcher matcher = pattern.matcher(fullTextAux);

					try {

						if (matcher.find()) {
							String before = fullTextAux.substring(StringUtils.initOrFifty(matcher.start()), matcher
									.start());
							String after = fullTextAux.substring(matcher.start(), StringUtils.endOrfiFifty(fullTextAux,
									matcher.end()));
							foundText = foundText + " [...] " + before + after + " [...] ";
							fullTextAux = matcher.replaceFirst("");
							j--;
							found = true;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			if (!found)
				j--;
		}

		return foundText;

	}
}

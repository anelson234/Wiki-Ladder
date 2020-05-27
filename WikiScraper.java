import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Adam Nelson
 * 
 * Purpose: The purpose of this class is to build a WikiScraper which is used to
 * produce a Set of all the valid links contained on any wikipedia page and has only one
 * public method called findWikiLinks which does just that. This class also utilizes 
 * memoization to speed up the function process by storing results into our "memo" hashmap.
 * 
 * 
 */

public class WikiScraper {
	
	private static HashMap<String,Set<String>> memo = new HashMap<String,Set<String>>();
			
	/**
	 * This method is used to find all of the links on the page of any link passed in the parameter. 
	 * Method fetchs the link's HTML and then computes all of the links of the page. Also uses memoization 
	 * to store the function calls of each link
	 * @param link
	 * @return Set<String>
	 */
	public static Set<String> findWikiLinks(String link) {
		if (!memo.containsKey(link)) {
			String html = fetchHTML(link);
			Set<String> links = scrapeHTML(html);
			memo.put(link, links);
			return links;
		}else {
			return memo.get(link);
		}
	}
	
	/**
	 * This method takes in a string of a link, gets the URL for it and then reads in all of the
	 * HTML code from that that website into a string to be processed in the findWikiLinks method.
	 * @param link
	 * @return String of HTML per website
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	/**
	 * This method returns the wikipedia link of any string passed in so that our
	 * fetchHTML may use it to find the HTML on that specific website.
	 * @param link
	 * @return String
	 */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	/**
	 * This method is used to find any valid URL from an HTML source, a valid URL is 
	 * one that contains no ":" or "#" character in it. Once a valid URL is found
	 * method will add to a Set of links per that page. This method also uses recursion
	 * to find all of the links on the page.
	 * @param html
	 * @return Set<String>
	 */
	private static Set<String> scrapeHTML(String html) {
		Set<String> setLinks = new HashSet<String>();
		String[] lines = html.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("<a href=\"/wiki/")) {
				int begOfCurr = lines[i].indexOf("<a href=\"/wiki/");
				String currSplitLine = lines[i].substring(begOfCurr);
				int endOfCurr = currSplitLine.indexOf(">");
				Set<String> otherLinks = scrapeHTML(currSplitLine.substring(endOfCurr));
				for (String validLinks : otherLinks) {
					setLinks.add(validLinks);
				}
				String[] firstSplit = currSplitLine.split("/");
				String[] secondSplit = firstSplit[2].split("\"");
				if (!secondSplit[0].contains(":") && !secondSplit[0].contains("#")) {
					setLinks.add(secondSplit[0]);
				}
			}
		}
		return setLinks;
	}
	
}

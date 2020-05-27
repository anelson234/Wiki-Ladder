import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Adam Nelson
 * 
 * Purpose: The purpose of this project is to simulate a wiki racer program.
 * The program accepts two inputs into console, a start page and an end page.
 * The program will compute the fastest paths of links from start to end if there
 * exists a path. This project contains one method findWikiLadder which constructs
 * the desired path, by also using two separate classes, one being a binary max-heap
 * called MaxPQ to obtain the path and the other called WikiScraper which processes 
 * and returns a set of links per website.
 * 
 *  
 */

public class WikiRacer {
	
	private static Set<String> memo = new HashSet<String>();

	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	/**
	 * The purpose of this method is to take in a start page and an end page
	 * as input. Method creates a ladder of viable paths to the desired end page
	 * and does this by using a binary min-heap (MaxPQ) and the WikiScraper classes.
	 * This method also utilizes memoization by making sure we do not visit a link
	 * twice. 
	 * 
	 * @param start
	 * @param end
	 * @return List<String>
	 */
	private static List<String> findWikiLadder(String start, String end) {
		List<String> ladder = new ArrayList<String>();
		ladder.add(start);
		MaxPQ pq = new MaxPQ();
		pq.enqueue(ladder, 0);
		memo.add(start);
		while (!pq.isEmpty()) {
			List<String> current = pq.dequeue();
			Set<String> currentPage = WikiScraper.findWikiLinks(current.get(current.size()-1));
			if (currentPage.contains(end)) {
				current.add(end);
				return current;
			}else {
				currentPage.parallelStream().forEach(link -> {
					WikiScraper.findWikiLinks(link);
					});
				for (String link : currentPage) {
					if (!memo.contains(link)) {
						Set<String> linkSet = WikiScraper.findWikiLinks(link);
						Set<String> endSet = WikiScraper.findWikiLinks(end);
						linkSet.retainAll(endSet);
						int priority = linkSet.size();
						List<String> tempList = new ArrayList<String>();
						for (int i = 0; i < current.size(); i++) {
							tempList.add(i, current.get(i));
						}
						tempList.add(link);
						pq.enqueue(tempList, priority);
					}
				}
			}
		}
		return new ArrayList<String>();
	}

}

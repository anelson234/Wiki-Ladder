import java.util.List;

/**
 * @author Adam Nelson
 * 
 * Purpose: The purpose of this class is to make a Ladder object which
 * holds a List<String> as the ladder that gets assigned to priority.
 * Priority of a ladder is determined by how many links in common the 
 * last element of the List<String> has with the desired endPage.
 * 
 * 
 *
 */

class Ladder {
	public List<String> ladder;
	public int priority;
	
	public Ladder(List<String> ladder, int priority) {
		this.ladder = ladder;
		this.priority = priority;
	}
}

/**
 * @author Adam Nelson
 * 
 * Purpose: The purpose of this class is to simulate a priority
 * queue backed by an array using a binary max-heap as the implementation strategy.
 * This data structure contains methods such as enqueue, dequeue, isEmpty and toString. 
 * Data structure is useful for finding ladders (paths) between a wikipedia start 
 * page and end page. Ladders with the highest priority will be pulled from the queue first.
 * 
 * 
 */

public class MaxPQ {
	
	private final static int INIT_CAP = 10;
	
	private Ladder[] queue;
	private int size;
	private int index;
	private int capacity;
	
	public MaxPQ() {
		queue = new Ladder[INIT_CAP];
		size = 0;
		index = 1;
		capacity = INIT_CAP;
	}
	
/**
 * This method is used to enqueue a new ladder to the priority queue by
 * giving a List<String> of links and a priority. Will add the ladder to the 
 * end of the queue and will bubble it up to the correct position.
 * 
 * @param ladder
 * @param priority
 */
	public void enqueue(List<String> ladder, int priority) {
        Ladder newLadder = new Ladder(ladder, priority);
        enqueue(newLadder);
    }
	
/**
 * This method is used by constructor chaining and adds a new ladder object
 * to the queue and does the same operation as the above method.
 * 
 * @param ladder
 */
	public void enqueue(Ladder ladder) {
		if (index < capacity) {
			queue[index] = ladder;
			bubbleUp(index);
			size += 1;
			index += 1;
		}else {
			capacity = capacity * 2;
			Ladder[] tempQueue = new Ladder[capacity];
			for (int i = 1; i < queue.length; i++) {
                tempQueue[i] = queue[i];
            }
		    queue = tempQueue;
            queue[index] = ladder;
            bubbleUp(index);
            size += 1;
            index += 1;
		}
	}
	
/**
 * This private method is used to bubble up our Ladder to the correct
 * heap ordering. Parents of the object trying to be bubbled up can be found
 * at index i/2.
 * 
 * @param index
 */
	private void bubbleUp(int index) {
		if (index > 1) {
			if (queue[index].priority > queue[index / 2].priority) {
				Ladder child = queue[index];
                Ladder parent = queue[index / 2];
                queue[index] = parent;
                queue[index / 2] = child;
                bubbleUp(index / 2);
            }
        }
	}
	
/**
 * This method returns the left most child of a parent
 * 
 * @param index
 * @return int
 */
	private int leftChild(int index) {
	    return index * 2;
	}
	
/**
 * This method returns the right most child of a parent
 * 
 * @param index
 * @return int
 */
	private int rightChild(int index) {
        return index * 2 + 1;
    }
	
/**
 * This method returns true if the left child exists, false otherwise.
 * 
 * @param index
 * @return true or false
 */
	private boolean hasLeftChild(int index) {
        return leftChild(index) < size;
    }
	
/**
 * This method returns true if the right child exists, false otherwise.
 * 
 * @param index
 * @return true or false
 */
	private boolean hasRightChild(int index) {
        return rightChild(index) < size;
    }
	
/**
 * This method dequeues the very front most highest priority ladder from the queue.
 * Once an element is removed, the very last element swaps where the first had been
 * and simply bubbles the element down so that it may maintain correct heap
 * ordering.
 * 
 * @return List<String> with highest priority
 */
    public List<String> dequeue() {
        try {
            Ladder firstPatient = queue[1];
            queue[1] = queue[index - 1];
            queue[index - 1] = null;
            if (size > 1) {
                bubbleDown();
            }
            size -= 1;
            index -= 1;
            return firstPatient.ladder;
        } catch (NullPointerException exception) {
            throw new NullPointerException(
                    "No such element exists. Program terminating");
        }

    }
    
/**
 * This method takes the very first indexed ladder object and
 * tries to bubble the object down the array if it's priority is
 * smaller than any of it's children's priority, they swap places.
 */
    private void bubbleDown() {
        int index = 1;
        while (hasLeftChild(index)) {
            Ladder parent = queue[index];
            Ladder highPriChild = null;
            int childIndex = 0;
            Ladder firstChild = queue[leftChild(index)];
            if (hasRightChild(index)) {
                Ladder secondChild = queue[rightChild(index)];
                if (firstChild.priority < secondChild.priority) {
                    highPriChild = secondChild;
                    childIndex = rightChild(index);
                }else {
                    highPriChild = firstChild;
                    childIndex = leftChild(index);
                }
            } else {
                highPriChild = firstChild;
                childIndex = leftChild(index);
            }

            if (parent.priority < highPriChild.priority) {
                queue[index] = highPriChild;
                queue[childIndex] = parent;
                index = childIndex;
            } else {
                break;
            }
        }
    }
    
/**
 * This method tells us if the queue is empty
 * 
 * @return true or false
 */
    public boolean isEmpty() {
        return size == 0;
    }
    
/**
 * The toString representation of my queue should look like:
 * 
 * {[Milkshake, Barley] (20), [Milkshake, Milk] (14)}
 */
    public String toString() {
        String result = "{";
        for (int i = 1; i < size + 1; i++) {
            if (i < size) {
                result += queue[i].ladder + " (" + queue[i].priority + "), ";
            } else {
                result += queue[i].ladder + " (" + queue[i].priority + ")";
            }
        }
        result += "}";
        return result;
    }

}

package pack1024;

import java.util.ArrayList;

/*-------------------------------------------------------------------
 * The following four methods are necessary for the NumberGame class.
 *           1. reverse
 *           2. slideLeft
 *           3. combineLeft1024
 *           4.    combineRight1024
 * ------------------------------------------------------------------
 */
public class Sequence {

    public int[] numbers;
	public static ArrayList <Integer> scores = new ArrayList<Integer>();
	public int combined;
	public int num = 0;
	public static boolean scoreChanged = false;

    public Sequence(int[] numbers) {
        this.numbers = numbers;
    }
    
    public void setUp() {
		//scores = new ArrayList<Integer>();
		if (scores.isEmpty()) {
			scores.add(0,0);
		}
	}

    public boolean equals(Sequence s) {
        if (this.numbers.length != s.numbers.length) {
            return false;
        }

        for (int n = 0; n < s.numbers.length; n++) {
            if (this.numbers[n] != s.numbers[n]) {
                return false;
            }
        }

        return true;
    }

    public boolean equals( Object s )
    {
        if (s instanceof Sequence)
        {
            return this.equals( (Sequence) s );
        }
        else
        {
            return false;
        }
    }

    public int[] reverse() {

        int[] helper = new int[this.numbers.length];

        for (int n = 0; n < this.numbers.length; n++) {
            helper[n] = this.numbers[this.numbers.length - 1 - n];
        }
        return helper;
    }

    public int[] slideLeft() {
        int[] helper = new int[this.numbers.length];
        int x = 0;
        for (int n = 0; n < this.numbers.length; n++) {
            if (this.numbers[n] > 0) {
                helper[x] = this.numbers[n];
                x++;
            }
        }
        return helper;
    }

    public int[] combineLeft1024() {
        Sequence s = new Sequence( this.numbers );
        s.numbers = s.slideLeft();
        for (int j = 0; j < s.numbers.length-1; j++) {
            if (s.numbers[j] == s.numbers[j + 1]) {
                s.numbers[j] += s.numbers[j + 1];
                if(s.numbers[j] > 0) {
					setUp();
				scores.add(s.numbers[j]);
                }
                s.numbers[j + 1] = 0;
            }
        }

        return s.slideLeft();
    }

    public int[] combineRight1024()
    {
        Sequence s = new Sequence( this.numbers );        
        s.numbers = s.reverse();
        s.numbers = s.combineLeft1024();
        return s.reverse();
    }

    
    public static int scores() {
		int sum = 0;
		for(Integer i: scores) {
			sum += i;
		}
		return sum;
	}
	
	public static void clearScores() {
		scores.clear();
	}
	
	public static void undoScore() {
		if(scores.size() > 1) {
		scores.remove(scores.size()-1);
		}
		
	}
	
	public static int undo() {
		
		int sum = 0;
		for (int i = 1; i < scores.size(); i ++) {
			sum += scores.get(i);
			
		}
		
		return sum;
	}
    // -----------    
    // also works
    // -----------    
    //    public int[] combineRight1024()
    //    {
    //        return this.inReverse().combinedLeftJustified().inReverse().numbers;
    //    }

    public String toString() {
        
        String str = "  ";
        for (int n = 0; n < this.numbers.length; n++) {
            str += this.numbers[n] + ", ";
        }
        return str.substring(0, str.length() - 2);
    }

    /*-------------------------------------------------------------------
     * The following methods are not necessary for the NumberGame class.
     *           1. inReverse
     *           2. leftJustified
     *           3. combineLeftJustified
     *           4.    combineRightJustified
     * ------------------------------------------------------------------
     */    

    public Sequence inReverse() {
        return new Sequence(this.reverse());
    }

    public Sequence leftJustified() {
        return new Sequence(slideLeft());
    }

    public Sequence combinedLeftJustified() {
        return new Sequence( this.combineLeft1024() );
    }

    public Sequence combinedRightJustified() {
        return new Sequence( this.combineRight1024() );
    }

    public static void main(String[] args) {
        int[] numbers = { 2, 0, 2, 4, 0, 4, 0, 0 };

        Sequence sequence = new Sequence(numbers);
        System.out.println(" Sequence");
        System.out.println("\t" + sequence.toString());
        int [] afterSlidingLeft = sequence.slideLeft();
        System.out.println("After sliding left the first time: ");
        for (int i : afterSlidingLeft) {
            System.out.print(i+" ");
        }
        System.out.println();
        sequence = new Sequence(afterSlidingLeft);
        int [] afterCombiningLeft = sequence.combineLeft1024();
        System.out.println("After combining left (slides left internally): ");
        for (int i : afterCombiningLeft) {
            System.out.print(i+" ");
        }
        System.out.println();

    }
}
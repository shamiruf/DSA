
import java.util.Arrays;

interface Mergesort { 

    // returns an unassorted copy of the first half (upper part) of the array
    int[] getFirstHalfOf(int[] array); 

    // returns an unassorted copy of the second half (bottom whole part) of the array array
    int[]getSecondHalfOf(int[] array); 

    // merges the elements in firstHalf and secondHalf into one field and returns it
    int[]merge(int[] firstHalf, int[] secondHalf); 

    // returns a assorted copy of the array
    int[]mergesort(int[] array); 

}


// Class MergeSortClass, which implements the MergeSort interface
public class MergeSortClass implements Mergesort {

    @Override
	public int[] getFirstHalfOf(int[] array) {
        int midle;
        if (array.length <= 1) {
            return array;
        } else {
            if (array.length %2 == 0) {
                midle = (int) Math.ceil(array.length / 2);
            } else {
                midle = (int) Math.ceil(array.length / 2 + 1);
            }
            return Arrays.copyOfRange(array, 0, midle);
        }

        
    }

    @Override
	public int[] getSecondHalfOf(int[] array) {
        int midle;
        if (array.length <= 1) {
            return new int[0];
        } else {
            if (array.length %2 == 0) {
                midle = (int) Math.ceil(array.length / 2);
            } else {
                midle = (int) Math.ceil(array.length / 2 + 1);
            }
            return Arrays.copyOfRange(array, midle, array.length);
        }
        
    }

    @Override
	public int[] merge(int[] firstHalf, int[] secondHalf) {
         
        int[] array = new int [firstHalf.length + secondHalf.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < firstHalf.length || j < secondHalf.length) {
            if (i < firstHalf.length && j < secondHalf.length) {
                if (firstHalf[i] > secondHalf[j]) {
                    array[k] = secondHalf[j];
                    k++;
                    j++;
                } else {
                    array[k] = firstHalf[i];
                    k++;
                    i++;
                }
            } else if (j < secondHalf.length) {
                array[k] = secondHalf[j];
                j++;
                k++;
            } else if (i < firstHalf.length) {
                array[k] = firstHalf[i];
                i++;
                k++;
            }

        }
        
        return array;
    }

    @Override
	public int[] mergesort(int[] array) {

        if (array.length < 2) {
            return array;
        }
        int[] first = getFirstHalfOf(array);
        int[] second = getSecondHalfOf(array);

        int[] result;
        int midle = 0;
        if (array.length %2 == 0) {
            midle = (int) Math.ceil(array.length / 2);
        } else {
            midle = (int) Math.ceil(array.length / 2 + 1);
        }

        for (int i = 0; i < first.length; i++) {
            first[i] = array[i];
        }
        
        for (int j = 0; j < second.length; j++) {
            second[j] = array[midle + j];
        }

        first = mergesort(first);
        second = mergesort(second);

        result = merge(first, second);

        return result;
    }


    public static void main(String[] args) {
        int[] array = {4, 1, 3, 5, 1, 4};
        Mergesort mRes = new MergeSortClass();   
        int[] firstRes = mRes.getFirstHalfOf(array);
        int[] secondRes = mRes.getSecondHalfOf(array);
        int[] res = mRes.mergesort(array);
        System.out.println(Arrays.toString(firstRes));
        System.out.println(Arrays.toString(secondRes));
        System.out.println(Arrays.toString(res));
    }
}


interface InterSearch {
    /* Using interpolation search, returns the index of the what 
    element found between the first and last indexes of the data 
    field, or -1 if it is not there. The method will be recursive 
    and should be resistant to incorrectly entered parameters (in 
    case of an error it will return -1 again). Use the Math.round ()
     method to round to integers.
    */
    public int search(int first, int last, int what, int[] data);
}

public class Homework2 implements InterSearch {

    @Override
    public int search(int first, int last, int what, int[] data) {

        if (first < 0 || last >= data.length || last < 0 || first >= data.length) {
            return -1;
        }

        if (first > last) {
            return -1;
        }

        if (first == last || data[first] == data[last]) {
            return -1;
        }

        if (data.length == 0) {
            return -1;
        }

        if (data[first] == what) {
            return first;
        }
        
        if (data[last] == what) {
            return last;
        }

        int midPos = last - first;
        double midData = (double)(data[last] - data[first]);

        int pos = (int)Math.round(first + (midPos * ((double)(what - data[first]) / midData)));

        if (pos >= data.length || pos < first || pos > last) {
            return -1;
        }

        if (data[pos] > what) {
            return search(first, pos - 1, what, data);
        } else if (data[pos] < what) {
            return search(pos + 1, last, what, data);
        } else {
            return pos;
        }
    }

    public static void main(String[] args) {
        int[] array = {0, 1, 1, 2, 2, 3};
        InterSearch intRes = new Homework2();   
        int result = intRes.search(0, 5, 2, array);
        System.out.println(result);
    }
}

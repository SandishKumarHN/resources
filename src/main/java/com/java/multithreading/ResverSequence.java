package com.java.multithreading;

public class ResverSequence {

    public void reverseWordss(char[] arr, int i, int j) {
      while(i < j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        j--;
        i++;
      }
    }

    public char[] reversWords(char[] arr) {
      int length = arr.length;
      reverseWordss(arr, 0, length - 1);
      int j = 0;
      for(int i = 0; i < length; i++) {
        if(arr[i] == ' ' ) {
          reverseWordss(arr, j, i - 1);
          j = i + 1;
        }
      }

      reverseWordss(arr, j, length - 1);

      return arr;
    }



    public static void main(String[] args) {
      char arr[] = new char[]{'p', 'e', 'r', 'f', 'e', 'c', 't', ' ', 'm', 'a', 'k', 'e', 's', ' ','p', 'r', 'a', 'c', 't', 'i', 'c', 'e'};
      ResverSequence sl = new ResverSequence();
      sl.reversWords(arr);
      for(int i = 0; i < arr.length; i++) {
        System.out.println(arr[i] + ",");
      }
    }

//
// output array ['practice']
// O(n)
// [ 'p', 'e', 'r', 'f', 'e', 'c', 't', '  ', 'm', 'a', 'k', 'e', 's', '  ','p', 'r', 'a', 'c', 't', 'i', 'c', 'e' ]
// O(n)
// for int i = 0; i < n; i++
//    if char == ' '
//        for n calls
//       reverse at start and end
// ['e', 'c', 'i', 't', 'c', 'a', 'r', 'p', ' ', 's', 'e', 'k', a, m, ' ', t, c, e, f, r, e, p]
//     O(n/3)                                       O(n/3)                  O(n/3)

// O(n)

}

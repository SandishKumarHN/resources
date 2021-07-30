package com.java.multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ListToMap {


  static String[] input = new String[] {"happy", "rise", "for", "set", "sunrise", "sun", "su", "nset", "sunset", "mind", "happymind", "n", "rise", "happysunrise"};

  // n
  // su
  // sun -> n su
  // set
  // for
  // nset -> n set
  // rise
  // sunset -> sun nset
  static Map<String, List<String>> dict = new HashMap<>();
  static Set<String> set = new HashSet<>();
  Queue<Integer> q = new ArrayList<Integer>();
  static List<String> res = new ArrayList<>();

  public static void main(String[] args) {
    set.addAll(Arrays.asList(input));

    //findWord("happysunrise");
    //findWord("happysunrise");
    set.remove("happysunrise");
    findWord("happysunrise");

    for(String str: res) {
      System.out.println(str);
    }
//    for(String str: input) {
//      set.remove(str);
//      res = new ArrayList<>();
//      dict.put(str, findWord(str, 0));
//      set.add(str);
//    }

    for(Map.Entry<String, List<String>> en : dict.entrySet()) {
      for (String str : en.getValue()) {
        System.out.println(en.getKey() + ":" + str);
      }
    }
  }

  public static boolean findWord(String str) {
    int left = 0, right = 0, mid = 0;

        //happysunrise
    //
    while(right <= str.length()) {

      String str1 = str.substring(left, mid + 1);
      String str2 = str.substring(mid + 1, right + 1);
      String str3= str.substring(left, right + 1);

      if(set.contains(str1)) {
        res.add(str1);
        left = mid + 1;
      }

      if(set.contains(str2)){
        res.add(str2);
        mid = right + 1;
      }

      if(set.contains(str3)) {
        res.add(str3);
        mid = right + 1;
      }

      right++;
    }

    return false;
  }
}
//sunset
//su
//n
//set
//sun
//

//sunset
//nset
//set
//s

//sunrise
//nrise


//set.addAll(Arrays.asList(input));
//
//    for(int i = 0; i < input.length; i++) {
//
//    String str = input[i];
//    List<String> sub = new ArrayList<>();
//    set.remove(str);
//    int left = 0, right = 0;
//
//    while(right < str.length()) {
//    if(set.contains(str.substring(left, right + 1))) {
//    List<String> remaining = findWord(str.substring(right + 1), set);
//    if(!remaining.isEmpty()) {
//    sub.add(str.substring(left, right + 1));
//    sub.addAll(remaining);
//    }
//    }
//
//    right++;
//    }
//    set.add(str);
//    res.put(str, sub);
//    }
//
//    for(Map.Entry<String, List<String>> en : res.entrySet()) {
//    for(String str : en.getValue()) {
//    System.out.println(en.getKey() + ":" + str);
//    }
//    }


//"nrise"


//    output = {
//  “happymind” : [[“happy”, “mind]],
//  “sunrise” : [[“su”, “n”, “rise”], [“sun”, “rise”]],
//  “sunset” : [[“sun”, “set”], [“su”, “n”, “set]],
//  “happysunrise” : [[“happy”, “sunrise”], [“happy”, “sun”, “rise”], [“happy”, “su”, “n”,
//   “rise”]]


// “sunrise”


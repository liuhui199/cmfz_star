package com.baizhi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ArrayTest {
    public static void main(String[] args) {
        /*String s = "dsdsgswgddscfeaw";
        char[] chars = s.toCharArray();
        HashMap<Character, Integer> map = new HashMap<>();
        for (char c : chars) {
            //如果包含这个字符个数就+1
            if(map.containsKey(c)){
                map.put(c,map.get(c)+1);
            }else {
                map.put(c,1);
            }
        }
        System.out.println(map);*/


        List<String> num = Arrays.asList("4", "6", "3", "1", "5", "2");
        for(int i = 0;i<num.size()-1;i++){
           for(int j = 0;j<num.size()-1-i;j++){
               //字符串转换int类型比较
               int a = Integer.parseInt(num.get(j));
               int b = Integer.parseInt(num.get(j + 1));
               if(a > b){
                   int temp = a;
                   a = b;
                   b = temp;
                   System.out.println(a+"" +b);
               }
           }
        }
        for (String s : num) {
            System.out.println(s);
        }
    }
}

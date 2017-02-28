package org.maestromedia.folders;

import java.util.Comparator;

public class TvShowSort implements Comparator<String> {
    
    public static int getComparison(String s1, String s2) {
        
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        
        for(int i=0; i<c1.length &&i<c2.length; i++) {
            
            if(isNumeric(c1[i]) && isNumeric(c2[i])) {
                int start = i;
                int end1=start+1;
                int end2 = start+1;
                while(end1<c1.length && isNumeric(c1[end1])) end1++;
                while(end2<c2.length && isNumeric(c2[end2])) end2++;
                int n1 = Integer.parseInt(s1.substring(start, end1));
                int n2 = Integer.parseInt(s2.substring(start, end2));
                if(n1==n2) {
                    i=end1;
                } else {
                    return n1-n2;
                }
            } else if(c1[i]!=c2[i]) {
                return s1.compareTo(s2);
            }
            
        }
        return 0;
    }
    
    public static boolean isNumeric(char c) {
        return c>='0'&&c<='9';
    }

    @Override
    public int compare(String o1, String o2) {
        return getComparison(o1, o2);
    }
}

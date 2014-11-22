package org.maestromedia.services.cores.mappers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TvShowShortTest {
    
    public TvShowShortTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testEnsureDoubleDigitsAreLater() {
        System.out.println("compare");
        String s1 = "Season 10";
        String s2 = "Season 2";
        int result = TvShowSort.getComparison(s1, s2);
        assertTrue(result>0);
        int reverseResult = TvShowSort.getComparison(s2, s1);
        assertEquals(result, -reverseResult);
    }
    
    @Test
    public void testEnsureSingleDigitCompare() {
        System.out.println("compare");
        String s1 = "Season 8";
        String s2 = "Season 2";
        int result = TvShowSort.getComparison(s1, s2);
        assertTrue(result>0);
        
        int reverseResult = TvShowSort.getComparison(s2, s1);
        assertEquals(result, -reverseResult);
    }
    
    @Test
    public void testEnsureEquality() {
        System.out.println("compare");
        String s1 = "Season 10 of something";
        String s2 = "Season 10 of something";
        int result = TvShowSort.getComparison(s1, s2);
        assertTrue(result==0);
        
    }
    
    @Test
    public void testEnsureOrderWithSuffix() {
        System.out.println("compare");
        String s1 = "Season 1 of other";
        String s2 = "Season 10 of other";
        int result = TvShowSort.getComparison(s1, s2);
        assertTrue(result<0);    
    }
    
    @Test
    public void testEnsureNormalCompareStillSafe() {
        System.out.println("compare");
        String s1 = "Season x 10";
        String s2 = "Season z 1";
        int result = TvShowSort.getComparison(s1, s2);
        assertTrue(result<0);
        
    }
    
}

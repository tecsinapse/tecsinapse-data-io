package br.com.tecsinapse.dataio.util;

import static br.com.tecsinapse.dataio.util.CommonUtils.*;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CommonUtilsTest {

    @Test(dataProvider = "isNullOrEmptyTestDs")
    public void isNullOrEmptyTest(String str, boolean expected) {
        assertEquals(isNullOrEmpty(str), expected);
    }

    @Test(dataProvider = "nullToEmptyTestDs")
    public void nullToEmptyTest(String str, String expected) {
        assertEquals(nullToEmpty(str), expected);
    }

    @Test(dataProvider = "repeatTestDs")
    public void repeatTest(String str, int size, String expected) {
        assertEquals(repeat(str, size), expected);
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void checkNotNullExceptionTest() {
        checkNotNull(null);
    }

    @Test
    public void checkNotNullTest() {
        checkNotNull("Abc");
    }

    @DataProvider(name = "isNullOrEmptyTestDs")
    public Object[][] isNullOrEmptyTestDs() {
        return new Object[][]{
                {"abc", false},
                {"", true},
                {null, true},
        };
    }

    @DataProvider(name = "nullToEmptyTestDs")
    public Object[][] nullToEmptyTestDs() {
        return new Object[][]{
                {"abc", "abc"},
                {"", ""},
                {null, ""},
        };
    }

    @DataProvider(name = "repeatTestDs")
    public Object[][] repeatTestDs() {
        return new Object[][]{
                {null, 50, null},
                {"abc", 0, ""},
                {"abc", 1, "a"},
                {"abc", 2, "ab"},
                {"abc", 3, "abc"},
                {"abc", 4, "abca"},
                {"abc", 5, "abcab"},
                {"abc", 9, "abcabcabc"},
        };
    }
}
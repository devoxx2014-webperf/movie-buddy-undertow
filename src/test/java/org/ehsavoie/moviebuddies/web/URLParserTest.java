/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ehsavoie.moviebuddies.web;

import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class URLParserTest {

    public URLParserTest() {
    }


    /**
     * Test of parse method, of class URLParser.
     */
    @Test
    public void testParse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/movies/search/actors/alaindelon/7");
        String[] expResult = new String[]{"search", "actors", "alaindelon", "7"};
        String[] result = URLParser.parse("movies/", request);
        assertArrayEquals(expResult, result);
    }


    @Test
    public void testPrefixParse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/users");
        String[] expResult = new String[]{""};
        String[] result = URLParser.parse("users", request);
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSimpleParse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/125");
        String[] expResult = new String[]{"125"};
        String[] result = URLParser.parse("", request);
        assertArrayEquals(expResult, result);
    }

}

/* $RCSfile$
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 2003  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 * 
 */
package org.openscience.cdk.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.openscience.cdk.Atom;
import org.openscience.cdk.LonePair;

/**
 * Checks the funcitonality of the LonePair class.
 *
 * @see org.openscience.cdk.LonePair
 */
public class LonePairTest extends TestCase {

    public LonePairTest(String name) {
        super(name);
    }

    public void setUp() {}

    public static Test suite() {
        return new TestSuite(LonePairTest.class);
    }
    
    public void testLonePair() {
        LonePair lp = new LonePair();
        assertTrue(lp.getAtom() == null);
        assertEquals(2, lp.getElectronCount());
    }
    
    public void testLonePair_Atom() {
        Atom atom = new Atom("N");
        LonePair lp = new LonePair(atom);
        assertEquals(2, lp.getElectronCount());
        assertTrue(lp.getAtom().compare(atom));
        assertTrue(lp.contains(atom));
    }
    
    public void testSetAtom() {
        Atom atom = new Atom("N");
        LonePair lp = new LonePair();
        assertTrue(lp.getAtom() == null);
        lp.setAtom(atom);
        assertTrue(lp.getAtom().compare(atom));
    }
    
    /** Test for RFC #9 */
    public void testToString() {
        LonePair lp = new LonePair();
        String description = lp.toString();
        for (int i=0; i< description.length(); i++) {
            assertTrue(description.charAt(i) != '\n');
            assertTrue(description.charAt(i) != '\r');
        }
    }
}

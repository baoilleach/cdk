/*  Copyright (C) 2004-2007  Yap Chun Wei <yapchunwei@users.sourceforge.net>
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.qsar.descriptors.atomic;

import java.io.IOException;


import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.config.ElementPTFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.AtomValenceTool;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IAtomicDescriptor;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.periodictable.PeriodicTable;

/**
 * Kier-Hall (relative) electronegativity of an atom.
 * There must not be any implicit hydrogens for all the atoms.<p>
 * <p/>
 * This descriptor uses these parameters:
 * <tableborder="1">
 * <p/>
 * <tr>
 * <p/>
 * <td>
 * Name
 * </td>
 * <p/>
 * <td>
 * Default
 * </td>
 * <p/>
 * <td>
 * Description
 * </td>
 * <p/>
 * </tr>
 * <p/>
 * <tr>
 * <p/>
 * <td>
 * <p/>
 * </td>
 * <p/>
 * <td>
 * <p/>
 * </td>
 * <p/>
 * <td>
 * no parameters
 * </td>
 * <p/>
 * </tr>
 * <p/>
 * </table>
 *
 * @author Yap Chun Wei
 * @cdk.created 2010-05-01
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.keyword electronegativity
 * @cdk.keyword Kier and Hall
 * @cdk.keyword descriptor
 * @cdk.dictref qsar-descriptors:kierHallElectronegativity
 */
@TestClass(value = "org.openscience.cdk.qsar.descriptors.atomic.kierHallElectronegativityDescriptorTest")
public class KierHallElectronegativityDescriptor implements IAtomicDescriptor {

    private static final String[] names = {"KierHallElectronegativity"};

    /**
     * Constructor for the KierHallElectronegativityDescriptor object
     *
     * @throws IOException            Description of the Exception
     * @throws ClassNotFoundException Description of the Exception
     */
    public KierHallElectronegativityDescriptor() throws IOException, ClassNotFoundException {
    }


    /**
     * Gets the specification attribute of the KierHallElectronegativityDescriptor
     * object
     *
     * @return The specification value
     */
    @TestMethod(value = "testGetSpecification")
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "kierHallElectronegativityDescriptor",
                this.getClass().getName(),
                "$Id: KierHallElectronegativityDescriptor.java 1 2008-06-10 10:12:38Z yapchunwei $",
                "PaDEL");
    }


    /**
     * This descriptor does have any parameter.
     */
    @TestMethod(value = "testSetParameters_arrayObject")
    public void setParameters(Object[] params) throws CDKException {
    }


    /**
     * Gets the parameters attribute of the KierHallElectronegativityDescriptor
     * object
     *
     * @return The parameters value
     * @see #setParameters
     */
    @TestMethod(value = "testGetParameters")
    public Object[] getParameters() {
        return null;
    }

    /**
     * Gets the names of descriptors
     *
     * @return Names of descriptors
     */
    @TestMethod(value = "testNamesConsistency")
    public String[] getDescriptorNames() {
        return names;
    }

    /**
     * Get dummy descriptor values when error occurs.
     *
     * @param e Exception that prevents the proper calculation of the descriptors
     * @return Dummy descriptor values
     */
    private DescriptorValue getDummyDescriptorValue(Exception e) {
        int ndesc = getDescriptorNames().length;
        DoubleArrayResult results = new DoubleArrayResult(ndesc);
        for (int i = 0; i < ndesc; i++) results.add(Double.NaN);
        return new DescriptorValue(getSpecification(), getParameterNames(),
                getParameters(), results, getDescriptorNames(), e);
    }

    /**
     * There must not be any implicit hydrogens for all the atoms.
     *
     * @param atom The IAtom for which the DescriptorValue is requested
     * @param ac   AtomContainer
     * @return a double with Kier and Hall electronegativity of the heavy atom
     * @throws CDKException if the atomic number or period cannot be found for any atom in the supplied AtomContainer
     */
    @TestMethod(value = "testCalculate_IAtomContainer")
    public DescriptorValue calculate(IAtom atom, IAtomContainer ac) {
        try {
            double RKHE = 0.0;
            int atomicNumber = atom.getAtomicNumber();
            if (atomicNumber != 1) {
                int maxBondedHydrogens = AtomContainerManipulator.countHydrogens(ac, atom);
                double delta = ac.getConnectedBondsCount(atom) - maxBondedHydrogens;
                int Zv = AtomValenceTool.getValence(atom);
                double deltaV = (double) (Zv - maxBondedHydrogens) / (double) (atomicNumber - Zv - 1);
                int period = Integer.valueOf(PeriodicTable.getPeriod(atom.getSymbol()));

                RKHE = (deltaV - delta) / (period * period);
            }
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), new DoubleResult(RKHE), names);
        }
        catch (Exception e) {
            return getDummyDescriptorValue(new CDKException("Error in ElementPTFactory: " + e.getMessage()));
        }
    }

    /**
     * Gets the parameterNames attribute of the KierHallElectronegativityDescriptor  object.
     *
     * @return The parameterNames value
     */
    @TestMethod(value = "testGetParameterNames")
    public String[] getParameterNames() {
        return new String[0];
    }


    /**
     * Gets the parameterType attribute of the KierHallElectronegativityDescriptor object.
     *
     * @param name Description of the Parameter
     * @return An Object of class equal to that of the parameter being requested
     */
    @TestMethod(value = "testGetParameterType_String")
    public Object getParameterType(String name) {
        return null;
    }
}


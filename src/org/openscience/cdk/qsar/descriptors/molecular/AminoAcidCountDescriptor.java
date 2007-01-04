/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2006-2007  The Chemistry Development Kit (CDK) project
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.qsar.descriptors.molecular;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAminoAcid;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.IntegerArrayResult;
import org.openscience.cdk.templates.AminoAcids;

import java.util.List;


/**
 * Class that returns the number of each amino acid in an atom container.
 * 
 * <p>This descriptor uses these parameters:
 * <table border="1">
 *   <tr>
 *     <td>Name</td>
 *     <td>Default</td>
 *     <td>Description</td>
 *   </tr>
 *   <tr>
 *     <td></td>
 *     <td></td>
 *     <td>no parameters</td>
 *   </tr>
 * </table>
 *
 * Returns 20 values with names of the form <i>nX</i>, where <i>X</i> is the short versio
 * of the amino acid name
 *
 * @author      egonw
 * @cdk.created 2006-01-15
 * @cdk.module  qsar-pdb
 * @cdk.set     qsar-descriptors
 * @cdk.dictref qsar-descriptors:aminoAcidsCount
 */
public class AminoAcidCountDescriptor implements IMolecularDescriptor {

    private IAtomContainerSet substructureSet;

    /**
     *  Constructor for the AromaticAtomsCountDescriptor object.
     */
    public AminoAcidCountDescriptor() {
        IAminoAcid[] aas = AminoAcids.createAAs();
        substructureSet = aas[0].getBuilder().newAtomContainerSet();
        for (int i=0; i<aas.length; i++) {
            substructureSet.addAtomContainer(aas[i]);
        }
    }

    /**
     * Returns a <code>Map</code> which specifies which descriptor
     * is implemented by this class. 
     *
     * These fields are used in the map:
     * <ul>
     * <li>Specification-Reference: refers to an entry in a unique dictionary
     * <li>Implementation-Title: anything
     * <li>Implementation-Identifier: a unique identifier for this version of
     *  this class
     * <li>Implementation-Vendor: CDK, JOELib, or anything else
     * </ul>
     *
     * @return An object containing the descriptor specification
     */
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#aminoAcidsCount",
                this.getClass().getName(),
                "$Id$",
                "The Chemistry Development Kit");
    }


    /**
     * Sets the parameters attribute of the AminoAcidsCountDescriptor object.
     * 
     * @param  params            The new parameters value
     * @exception  CDKException  if more than one parameter or a non-Boolean parameter is specified
     * @see #getParameters
     */
    public void setParameters(Object[] params) throws CDKException {
        // no parameters exist
    }


    /**
     * Gets the parameters attribute of the AminoAcidsCountDescriptor object.
     *
     * @return    The parameters value
     * @see #setParameters
     */
    public Object[] getParameters() {
        return new Object[0];
    }


    /**
     * Determine the number of amino acids groups the supplied {@link IAtomContainer}.
     *
     * @param  ac           The {@link IAtomContainer} for which this descriptor is to be calculated
     * @return              the number of aromatic atoms of this AtomContainer
     * @throws CDKException if there is a problem in atomaticity detection
     *
     * @see #setParameters
     */
    public DescriptorValue calculate(IAtomContainer ac) throws CDKException {
        int resultLength = substructureSet.getAtomContainerCount();
        IntegerArrayResult results = new IntegerArrayResult(resultLength);

        IAtomContainer substructure;
        for (int i=0; i<resultLength; i++) {
            substructure = substructureSet.getAtomContainer(i);
            List maps = UniversalIsomorphismTester.getSubgraphMaps(ac, substructure);
            if (maps != null) {
                results.add(maps.size());
            }
        }

        String[] names = new String[substructureSet.getAtomContainerCount()];
        IAminoAcid[] aas = AminoAcids.createAAs();
        for (int i = 0; i < aas.length; i++) names[i] = "n"+aas[i].getProperty(AminoAcids.RESIDUE_NAME_SHORT);

        return new DescriptorValue(getSpecification(), getParameterNames(),
            getParameters(), results, names);
    }

    /**
     * Gets the parameterNames attribute of the AromaticAtomsCountDescriptor object.
     *
     * @return    The parameterNames value
     */
    public String[] getParameterNames() {
        return new String[0];
    }

    /**
     * Gets the parameterType attribute of the AromaticAtomsCountDescriptor object.
     *
     * @param  name  Description of the Parameter
     * @return       An Object of class equal to that of the parameter being requested
     */
    public Object getParameterType(String name) {
        return new Object[0];
    }
}


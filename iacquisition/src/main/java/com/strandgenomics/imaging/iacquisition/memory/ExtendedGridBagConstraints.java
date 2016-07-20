/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*

   Copyright 2000-2001  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
//package org.apache.batik.util.gui;
package com.strandgenomics.imaging.iacquisition.memory;

import java.awt.GridBagConstraints;

/**
 * This class extends the java.awt.GridBagConstraints in order to
 * provide some utility methods.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: ExtendedGridBagConstraints.java 6418 2005-05-11 15:03:27Z anand $
 */
public class ExtendedGridBagConstraints extends GridBagConstraints {

    /**
     * Modifies gridx, gridy, gridwidth, gridheight.
     *
     * @param x The value for gridx.
     * @param y The value for gridy.
     * @param width The value for gridwidth.
     * @param height The value for gridheight.
     */
    public void setGridBounds(int x, int y, int width, int height) {
        gridx = x;
        gridy = y;
        gridwidth = width;
        gridheight = height;
    }

    /**
     * Modifies the weightx and weighty.
     *
     * @param weightx The value for weightx
     * @param weighty The value for weighty
     */
    public void setWeight(double weightx, double weighty) {
        this.weightx = weightx;
        this.weighty = weighty;
    }
}

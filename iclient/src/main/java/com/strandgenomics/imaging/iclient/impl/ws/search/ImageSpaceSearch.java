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

package com.strandgenomics.imaging.iclient.impl.ws.search;

public interface ImageSpaceSearch extends java.rmi.Remote {
    public long[] search(java.lang.String in0, java.lang.String in1, java.lang.String[] in2, com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[] in3, int in4) throws java.rmi.RemoteException;
    public long[] findRecords(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[] in2) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.search.SearchField[] getNavigableFields(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.search.SearchField[] getAvailableUserAnnotations(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.search.SearchNode[] getNavigationBins(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[] in2, com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition in3) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.search.SearchField[] getAvailableDynamicMetaData(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
}

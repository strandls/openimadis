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
 * ImgMetaDataStore.java
 *
 * AVADIS Image Management System
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.bioformats.custom;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import loci.formats.meta.IMetadata;
import loci.formats.ome.OMEXMLMetadata;
import ome.units.UNITS;
import ome.units.quantity.ElectricPotential;
import ome.units.quantity.Frequency;
import ome.units.quantity.Length;
import ome.units.quantity.Power;
import ome.units.quantity.Pressure;
import ome.units.quantity.Temperature;
import ome.units.quantity.Time;
import ome.xml.meta.MetadataRoot;
import ome.xml.model.AffineTransform;
import ome.xml.model.MapPair;
import ome.xml.model.enums.AcquisitionMode;
import ome.xml.model.enums.ArcType;
import ome.xml.model.enums.Binning;
import ome.xml.model.enums.ContrastMethod;
import ome.xml.model.enums.Correction;
import ome.xml.model.enums.DetectorType;
import ome.xml.model.enums.DimensionOrder;
import ome.xml.model.enums.ExperimentType;
import ome.xml.model.enums.FilamentType;
import ome.xml.model.enums.FillRule;
import ome.xml.model.enums.FilterType;
import ome.xml.model.enums.FontFamily;
import ome.xml.model.enums.FontStyle;
import ome.xml.model.enums.IlluminationType;
import ome.xml.model.enums.Immersion;
import ome.xml.model.enums.LaserMedium;
import ome.xml.model.enums.LaserType;
import ome.xml.model.enums.LineCap;
import ome.xml.model.enums.Marker;
import ome.xml.model.enums.Medium;
import ome.xml.model.enums.MicrobeamManipulationType;
import ome.xml.model.enums.MicroscopeType;
import ome.xml.model.enums.NamingConvention;
import ome.xml.model.enums.PixelType;
import ome.xml.model.enums.Pulse;
import ome.xml.model.primitives.Color;
import ome.xml.model.primitives.NonNegativeInteger;
import ome.xml.model.primitives.NonNegativeLong;
import ome.xml.model.primitives.PercentFraction;
import ome.xml.model.primitives.PositiveFloat;
import ome.xml.model.primitives.PositiveInteger;
import ome.xml.model.primitives.Timestamp;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;

/**
 * place holder for img meta data
 * @author arunabha
 *
 */
public class BIFMetaDataStore implements OMEXMLMetadata {
	
	
	@Override
	public int getChannelCount(int seriesNo)
	{
		return 3;
	}

	@Override
	public Length getChannelEmissionWavelength(int series, int channel)
	{
		return null;
	}

	@Override
	public String getChannelName(int seriesNo, int channelNo) 
	{
		return null;
	}

	public String getImageAcquiredDate(int seriesNo) 
	{
		return null;
	}

	@Override
	public Length getPixelsPhysicalSizeX(int seriesNo) 
	{
		return null;
	}

	@Override
	public Length getPixelsPhysicalSizeY(int seriesNo) 
	{
		return null;
	}

	@Override
	public Length getPixelsPhysicalSizeZ(int seriesNo) 
	{
		return null;
	}

	@Override
	public int getPlaneCount(int seriesNo) 
	{
		return 3;
	}

	@Override
	public Time getPlaneDeltaT(int seriesNo, int planeIndex) 
	{
		return null;
	}

	@Override
	public Time getPlaneExposureTime(int seriesNo, int planeIndex)
	{
		return null;
	}

	@Override
	public Length getPlanePositionX(int seriesNo, int planeIndex)
	{
		return null;
	}

	@Override
	public Length getPlanePositionY(int seriesNo, int planeIndex)
	{
		return null;
	}

	@Override
	public Length getPlanePositionZ(int seriesNo, int planeIndex) 
	{
		return null;
	}

	@Override
	public String getArcID(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArcLotNumber(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArcManufacturer(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArcModel(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Power getArcPower(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArcSerialNumber(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArcType getArcType(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBinaryFileFileName(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBinaryFileMIMEType(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeLong getBinaryFileSize(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArcAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBinaryOnlyMetadataFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBinaryOnlyUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBooleanAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getBooleanAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooleanAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBooleanAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getBooleanAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooleanAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooleanAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getBooleanAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AcquisitionMode getChannelAcquisitionMode(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChannelAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getChannelColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContrastMethod getChannelContrastMethod(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getChannelExcitationWavelength(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelFilterSetRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelFluor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IlluminationType getChannelIlluminationType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PercentFraction getChannelLightSourceSettingsAttenuation(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelLightSourceSettingsID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getChannelLightSourceSettingsWavelength(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getChannelNDFilter(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getChannelPinholeSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getChannelPockelCellSetting(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getChannelSamplesPerPixel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCommentAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommentAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommentAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCommentAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommentAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommentAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommentAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommentAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDatasetAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDatasetAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDatasetCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDatasetDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDatasetExperimenterGroupRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDatasetExperimenterRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDatasetID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDatasetImageRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDatasetImageRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDatasetName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDatasetRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Double getDetectorAmplificationGain(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetectorAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDetectorAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDetectorCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Double getDetectorGain(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetectorID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetectorLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetectorManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetectorModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDetectorOffset(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetectorSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Binning getDetectorSettingsBinning(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDetectorSettingsGain(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetectorSettingsID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getDetectorSettingsIntegration(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDetectorSettingsOffset(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Frequency getDetectorSettingsReadOutRate(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElectricPotential getDetectorSettingsVoltage(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDetectorSettingsZoom(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DetectorType getDetectorType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElectricPotential getDetectorVoltage(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDetectorZoom(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDichroicAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDichroicAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDichroicCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDichroicID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDichroicLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDichroicManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDichroicModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDichroicSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoubleAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDoubleAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDoubleAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoubleAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDoubleAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDoubleAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDoubleAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDoubleAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEllipseAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getEllipseFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getEllipseFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getEllipseFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getEllipseFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getEllipseFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEllipseID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getEllipseLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getEllipseLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getEllipseRadiusX(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getEllipseRadiusY(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getEllipseStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEllipseStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getEllipseStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEllipseText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getEllipseTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getEllipseTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getEllipseTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getEllipseTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getEllipseVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getEllipseX(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getEllipseY(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getExperimentCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getExperimentDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimentExperimenterRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimentID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExperimentType getExperimentType(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getExperimenterAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getExperimenterCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getExperimenterEmail(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterFirstName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterGroupAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getExperimenterGroupAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getExperimenterGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getExperimenterGroupDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterGroupExperimenterRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getExperimenterGroupExperimenterRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getExperimenterGroupID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterGroupLeader(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterGroupName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterInstitution(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterLastName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterMiddleName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimenterUserName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilamentAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilamentID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilamentLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilamentManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilamentModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Power getFilamentPower(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilamentSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilamentType getFilamentType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFileAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFileAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFileAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFileAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilterAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFilterCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFilterFilterWheel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilterSetCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFilterSetDichroicRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterSetEmissionFilterRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilterSetEmissionFilterRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFilterSetExcitationFilterRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilterSetExcitationFilterRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFilterSetID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterSetLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterSetManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterSetModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilterSetSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterType getFilterType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGenericExcitationSourceAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGenericExcitationSourceID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGenericExcitationSourceLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGenericExcitationSourceManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MapPair> getGenericExcitationSourceMap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGenericExcitationSourceModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Power getGenericExcitationSourcePower(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGenericExcitationSourceSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getImageAcquisitionDate(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getImageAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getImageCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getImageDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageExperimentRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageExperimenterGroupRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageExperimenterRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageInstrumentRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageMicrobeamManipulationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImageROIRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getImageROIRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Pressure getImagingEnvironmentAirPressure(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PercentFraction getImagingEnvironmentCO2Percent(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PercentFraction getImagingEnvironmentHumidity(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MapPair> getImagingEnvironmentMap(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Temperature getImagingEnvironmentTemperature(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstrumentAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInstrumentAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInstrumentCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getInstrumentID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLabelFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getLabelFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getLabelFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getLabelFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getLabelFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getLabelLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLabelLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLabelStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getLabelStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getLabelTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getLabelTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getLabelTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getLabelTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLabelVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getLabelX(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getLabelY(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLaserAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getLaserFrequencyMultiplication(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLaserID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LaserMedium getLaserLaserMedium(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLaserLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLaserManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLaserModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLaserPockelCell(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Power getLaserPower(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pulse getLaserPulse(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLaserPump(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Frequency getLaserRepetitionRate(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLaserSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLaserTuneable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LaserType getLaserType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getLaserWavelength(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLeaderCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLightEmittingDiodeAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLightEmittingDiodeID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLightEmittingDiodeLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLightEmittingDiodeManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLightEmittingDiodeModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Power getLightEmittingDiodePower(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLightEmittingDiodeSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLightPathAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLightPathAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLightPathDichroicRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLightPathEmissionFilterRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLightPathEmissionFilterRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLightPathExcitationFilterRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLightPathExcitationFilterRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLightSourceAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLightSourceCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLightSourceType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLineAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLineFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getLineFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getLineFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getLineFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getLineFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLineID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getLineLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLineLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marker getLineMarkerEnd(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marker getLineMarkerStart(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLineStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLineStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getLineStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLineText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getLineTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getLineTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getLineTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getLineTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLineVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getLineX1(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getLineX2(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getLineY1(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getLineY2(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getListAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getListAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getListAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getListAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getListAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getListAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getListAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLongAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLongAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLongAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLongAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getLongAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMapAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMapAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMapAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMapAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMapAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMapAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMapAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MapPair> getMapAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaskAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getMaskFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getMaskFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getMaskFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getMaskFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getMaskFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getMaskHeight(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaskID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getMaskLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getMaskLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getMaskStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaskStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getMaskStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMaskText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getMaskTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getMaskTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getMaskTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getMaskTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getMaskVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getMaskWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getMaskX(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getMaskY(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMicrobeamManipulationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMicrobeamManipulationDescription(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMicrobeamManipulationExperimenterRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMicrobeamManipulationID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PercentFraction getMicrobeamManipulationLightSourceSettingsAttenuation(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMicrobeamManipulationLightSourceSettingsCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMicrobeamManipulationLightSourceSettingsID(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getMicrobeamManipulationLightSourceSettingsWavelength(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMicrobeamManipulationROIRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMicrobeamManipulationROIRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMicrobeamManipulationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MicrobeamManipulationType getMicrobeamManipulationType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMicroscopeLotNumber(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMicroscopeManufacturer(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMicroscopeModel(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMicroscopeSerialNumber(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MicroscopeType getMicroscopeType(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getObjectiveAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Double getObjectiveCalibratedMagnification(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Correction getObjectiveCorrection(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getObjectiveCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getObjectiveID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Immersion getObjectiveImmersion(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getObjectiveIris(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getObjectiveLensNA(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveLotNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveManufacturer(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveModel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getObjectiveNominalMagnification(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveSerialNumber(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getObjectiveSettingsCorrectionCollar(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveSettingsID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Medium getObjectiveSettingsMedium(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getObjectiveSettingsRefractiveIndex(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getObjectiveWorkingDistance(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPixelsBigEndian(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPixelsBinDataBigEndian(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPixelsBinDataCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DimensionOrder getPixelsDimensionOrder(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPixelsID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPixelsInterleaved(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPixelsSignificantBits(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPixelsSizeC(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPixelsSizeT(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPixelsSizeX(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPixelsSizeY(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPixelsSizeZ(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getPixelsTimeIncrement(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PixelType getPixelsType(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlaneAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlaneAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPlaneHashSHA1(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPlaneTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPlaneTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPlaneTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateAcquisitionAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlateAcquisitionAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlateAcquisitionCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPlateAcquisitionDescription(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getPlateAcquisitionEndTime(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateAcquisitionID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPlateAcquisitionMaximumFieldCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateAcquisitionName(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getPlateAcquisitionStartTime(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateAcquisitionWellSampleRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlateAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NamingConvention getPlateColumnNamingConvention(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPlateColumns(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlateCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPlateDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateExternalIdentifier(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPlateFieldIndex(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlateRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NamingConvention getPlateRowNamingConvention(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveInteger getPlateRows(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlateStatus(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPlateWellOriginX(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPlateWellOriginY(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPointAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getPointFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getPointFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getPointFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPointFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getPointFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPointID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getPointLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPointLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getPointStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPointStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPointStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPointText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPointTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPointTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPointTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getPointTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPointVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getPointX(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getPointY(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolygonAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getPolygonFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getPolygonFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getPolygonFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPolygonFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getPolygonFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolygonID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getPolygonLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPolygonLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolygonPoints(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getPolygonStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolygonStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPolygonStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolygonText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPolygonTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPolygonTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPolygonTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getPolygonTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPolygonVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolylineAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getPolylineFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getPolylineFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getPolylineFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPolylineFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getPolylineFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolylineID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getPolylineLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPolylineLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marker getPolylineMarkerEnd(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marker getPolylineMarkerStart(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolylinePoints(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getPolylineStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolylineStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getPolylineStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPolylineText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPolylineTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPolylineTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getPolylineTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getPolylineTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getPolylineVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getProjectAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getProjectCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getProjectDatasetRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectExperimenterGroupRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectExperimenterRef(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getROIAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getROIAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getROICount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getROIDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getROIID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getROIName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getROINamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReagentAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getReagentAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReagentCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getReagentDescription(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReagentID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReagentName(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReagentReagentIdentifier(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRectangleAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getRectangleFillColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FillRule getRectangleFillRule(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontFamily getRectangleFontFamily(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getRectangleFontSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontStyle getRectangleFontStyle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getRectangleHeight(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRectangleID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineCap getRectangleLineCap(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getRectangleLocked(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getRectangleStrokeColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRectangleStrokeDashArray(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getRectangleStrokeWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRectangleText(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getRectangleTheC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getRectangleTheT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getRectangleTheZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getRectangleTransform(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getRectangleVisible(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getRectangleWidth(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getRectangleX(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getRectangleY(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRightsRightsHeld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRightsRightsHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScreenAnnotationRefCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScreenCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getScreenDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenPlateRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenProtocolDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenProtocolIdentifier(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenReagentSetDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenReagentSetIdentifier(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenType(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getShapeAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShapeCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getShapeType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStageLabelName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getStageLabelX(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getStageLabelY(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getStageLabelZ(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTagAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTagAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTagAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTagAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTermAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTermAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTermAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTermAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTermAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTermAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTermAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTermAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTiffDataCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NonNegativeInteger getTiffDataFirstC(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getTiffDataFirstT(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getTiffDataFirstZ(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getTiffDataIFD(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getTiffDataPlaneCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimestampAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTimestampAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimestampAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimestampAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTimestampAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimestampAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimestampAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestampAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getTransmittanceRangeCutIn(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getTransmittanceRangeCutInTolerance(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getTransmittanceRangeCutOut(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getTransmittanceRangeCutOutTolerance(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PercentFraction getTransmittanceRangeTransmittance(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUUIDFileName(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUUIDValue(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWellAnnotationRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWellAnnotationRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getWellColor(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getWellColumn(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWellCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getWellExternalDescription(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWellExternalIdentifier(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWellID(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWellReagentRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getWellRow(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWellSampleCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getWellSampleID(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWellSampleImageRef(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NonNegativeInteger getWellSampleIndex(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getWellSamplePositionX(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getWellSamplePositionY(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWellSampleRefCount(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Timestamp getWellSampleTimepoint(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWellType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getXMLAnnotationAnnotationCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getXMLAnnotationAnnotationRef(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXMLAnnotationAnnotator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getXMLAnnotationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getXMLAnnotationDescription(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXMLAnnotationID(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXMLAnnotationNamespace(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXMLAnnotationValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createRoot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MetadataRoot getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setArcAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArcID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArcLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArcManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArcModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArcPower(Power arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArcSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setArcType(ArcType arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryFileFileName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryFileMIMEType(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryFileSize(NonNegativeLong arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryOnlyMetadataFile(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryOnlyUUID(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBooleanAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBooleanAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBooleanAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBooleanAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBooleanAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBooleanAnnotationValue(Boolean arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelAcquisitionMode(AcquisitionMode arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelContrastMethod(ContrastMethod arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelEmissionWavelength(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelExcitationWavelength(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelFilterSetRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelFluor(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelIlluminationType(IlluminationType arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelLightSourceSettingsAttenuation(PercentFraction arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelLightSourceSettingsID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelLightSourceSettingsWavelength(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelNDFilter(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelName(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelPinholeSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelPockelCellSetting(Integer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChannelSamplesPerPixel(PositiveInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCommentAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCommentAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCommentAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCommentAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCommentAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCommentAnnotationValue(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDatasetAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDatasetDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDatasetExperimenterGroupRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDatasetExperimenterRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDatasetID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDatasetImageRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDatasetName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorAmplificationGain(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorGain(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorOffset(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsBinning(Binning arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsGain(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsIntegration(PositiveInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsOffset(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsReadOutRate(Frequency arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsVoltage(ElectricPotential arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorSettingsZoom(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorType(DetectorType arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorVoltage(ElectricPotential arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDetectorZoom(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDichroicAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDichroicID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDichroicLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDichroicManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDichroicModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDichroicSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDoubleAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDoubleAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDoubleAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDoubleAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDoubleAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDoubleAnnotationValue(Double arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseRadiusX(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseRadiusY(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseX(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEllipseY(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimentDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimentExperimenterRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimentID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimentType(ExperimentType arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterEmail(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterFirstName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterGroupAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterGroupDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterGroupExperimenterRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterGroupID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterGroupLeader(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterGroupName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterInstitution(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterLastName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterMiddleName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExperimenterUserName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentPower(Power arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilamentType(FilamentType arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterFilterWheel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetDichroicRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetEmissionFilterRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetExcitationFilterRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterSetSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilterType(FilterType arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourceAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourceID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourceLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourceManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourceMap(List<MapPair> arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourceModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourcePower(Power arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGenericExcitationSourceSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageAcquisitionDate(Timestamp arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageExperimentRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageExperimenterGroupRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageExperimenterRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageInstrumentRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageMicrobeamManipulationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageROIRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImagingEnvironmentAirPressure(Pressure arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImagingEnvironmentCO2Percent(PercentFraction arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImagingEnvironmentHumidity(PercentFraction arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImagingEnvironmentMap(List<MapPair> arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImagingEnvironmentTemperature(Temperature arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInstrumentAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInstrumentID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelX(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabelY(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserFrequencyMultiplication(PositiveInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserLaserMedium(LaserMedium arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserPockelCell(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserPower(Power arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserPulse(Pulse arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserPump(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserRepetitionRate(Frequency arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserTuneable(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserType(LaserType arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaserWavelength(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightEmittingDiodeAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightEmittingDiodeID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightEmittingDiodeLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightEmittingDiodeManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightEmittingDiodeModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightEmittingDiodePower(Power arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightEmittingDiodeSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightPathAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightPathDichroicRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightPathEmissionFilterRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightPathExcitationFilterRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineMarkerEnd(Marker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineMarkerStart(Marker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineX1(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineX2(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineY1(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLineY2(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLongAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLongAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLongAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLongAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLongAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLongAnnotationValue(Long arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMapAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMapAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMapAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMapAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMapAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMapAnnotationValue(List<MapPair> arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskBinData(byte[] arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskHeight(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskWidth(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskX(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaskY(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationDescription(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationExperimenterRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationLightSourceSettingsAttenuation(PercentFraction arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationLightSourceSettingsID(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationLightSourceSettingsWavelength(Length arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationROIRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicrobeamManipulationType(MicrobeamManipulationType arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicroscopeLotNumber(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicroscopeManufacturer(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicroscopeModel(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicroscopeSerialNumber(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMicroscopeType(MicroscopeType arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveCalibratedMagnification(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveCorrection(Correction arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveImmersion(Immersion arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveIris(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveLensNA(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveLotNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveManufacturer(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveModel(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveNominalMagnification(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveSerialNumber(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveSettingsCorrectionCollar(Double arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveSettingsID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveSettingsMedium(Medium arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveSettingsRefractiveIndex(Double arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectiveWorkingDistance(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsBigEndian(Boolean arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsBinDataBigEndian(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsDimensionOrder(DimensionOrder arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsInterleaved(Boolean arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsPhysicalSizeX(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsPhysicalSizeY(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsPhysicalSizeZ(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsSignificantBits(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsSizeC(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsSizeT(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsSizeX(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsSizeY(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsSizeZ(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsTimeIncrement(Time arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixelsType(PixelType arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaneAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaneDeltaT(Time arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaneExposureTime(Time arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaneHashSHA1(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlanePositionX(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlanePositionY(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlanePositionZ(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaneTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaneTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaneTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionDescription(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionEndTime(Timestamp arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionMaximumFieldCount(PositiveInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionName(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionStartTime(Timestamp arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAcquisitionWellSampleRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateColumnNamingConvention(NamingConvention arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateColumns(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateExternalIdentifier(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateFieldIndex(NonNegativeInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateRowNamingConvention(NamingConvention arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateRows(PositiveInteger arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateStatus(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateWellOriginX(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlateWellOriginY(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointX(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointY(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonPoints(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolygonVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineMarkerEnd(Marker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineMarkerStart(Marker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylinePoints(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolylineVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProjectAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProjectDatasetRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProjectDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProjectExperimenterGroupRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProjectExperimenterRef(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProjectID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProjectName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setROIAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setROIDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setROIID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setROIName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setROINamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReagentAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReagentDescription(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReagentID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReagentName(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReagentReagentIdentifier(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleFillColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleFillRule(FillRule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleFontFamily(FontFamily arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleFontSize(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleFontStyle(FontStyle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleHeight(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleLineCap(LineCap arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleLocked(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleStrokeColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleStrokeDashArray(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleStrokeWidth(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleText(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleTheC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleTheT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleTheZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleTransform(AffineTransform arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleVisible(Boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleWidth(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleX(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRectangleY(Double arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRightsRightsHeld(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRightsRightsHolder(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoot(MetadataRoot arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenPlateRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenProtocolDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenProtocolIdentifier(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenReagentSetDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenReagentSetIdentifier(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScreenType(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStageLabelName(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStageLabelX(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStageLabelY(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStageLabelZ(Length arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTagAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTagAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTagAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTagAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTagAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTagAnnotationValue(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermAnnotationValue(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTiffDataFirstC(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTiffDataFirstT(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTiffDataFirstZ(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTiffDataIFD(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTiffDataPlaneCount(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestampAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestampAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestampAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestampAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestampAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimestampAnnotationValue(Timestamp arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransmittanceRangeCutIn(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransmittanceRangeCutInTolerance(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransmittanceRangeCutOut(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransmittanceRangeCutOutTolerance(Length arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransmittanceRangeTransmittance(PercentFraction arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUUID(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUUIDFileName(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUUIDValue(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellAnnotationRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellColor(Color arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellColumn(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellExternalDescription(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellExternalIdentifier(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellID(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellReagentRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellRow(NonNegativeInteger arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellSampleID(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellSampleImageRef(String arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellSampleIndex(NonNegativeInteger arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellSamplePositionX(Length arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellSamplePositionY(Length arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellSampleTimepoint(Timestamp arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWellType(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXMLAnnotationAnnotationRef(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXMLAnnotationAnnotator(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXMLAnnotationDescription(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXMLAnnotationID(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXMLAnnotationNamespace(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXMLAnnotationValue(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String dumpXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int resolveReferences() {
		// TODO Auto-generated method stub
		return 0;
	}


}

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
 * TestData.java
 *
 * AVADIS Image Management System
 * Web Service Test programs
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
package com.strandgenomics.imaging.iclient.test;

public interface TestData {

   	static final String[][] users = {
			{"barbour", "barbour123", "Anna.Barbour@ijm.univ-paris-diderot.fr", "Anna Babour", "TeamMember"},
			{"sbardin", "bardin123", "sabine.bardin@curie.fr", "Sabine Bardin", "TeamMember"},
			{"berthault", "berthault123", "nathalie.berthault@curie.fr", "Nathalie Berthault", "TeamMember"},
			
			{"jboulang", "boulanger123", "jerome.boulanger@curie.fr", "Jerome Boulanger", "TeamLeader"},
			{"boyarchu", "boyarchuk123", "Ekaterina.Boyarchuk@curie.fr", "Ekatarina Boyarchuk", "TeamMember"},
			{"scantalo", "cantaloube123", "sylvain.cantaloube@curie.fr", "Sylvain Cantaloube", "TeamMember"},
			
			{"chabosse", "chabosseau123", "pauline.chabosseau@curie.fr", "Pauline Chabosseau", "TeamLeader"},
			{"ocochet", "cochet123", "olivier.cochet@curie.fr", "Olivier Cochet", "TeamMember"},
			{"fab", "cordelieres123", "fabrice.cordelieres@curie.fr", "Fabrice Cordelieres", "TeamLeader"},
			
//			{"acroset", "croset123", "amelie.croset@curie.fr", "Am�lie Croset", "TeamMember"},
			{"delevoye", "delevoye123", "cedric.delevoye@curie.fr", "Cedric Delevoye", "TeamMember"},
			{"escamill", "escamilla123", "martin.escamilla@curie.fr", "Martin Escamilla", "TeamMember"},
			{"fraisier", "fraisier123", "vincent.fraisier@curie.fr", "Vincent Fraisier", "TeamMember"},
			
			{"ppaulgi", "perrine123", "perrine.paul-gilloteaux@curie.fr", "Perrine Gilloteaux", "TeamLeader"},
			{"lgiorget", "giorgetti123", "luca.giorgetti@curie.fr", "Luca Giorgetti", "TeamMember"},
			{"jgrossie", "grossier123", "Jean-Philippe.Grossier@curie.fr", "Jean-Philippe Grossier", "TeamMember"},
			{"mirondel", "irondelle123", "marie.irondelle@curie.fr", "Marie Irondelle", "TeamMember"},
			{"ekotula", "kotula123", "ewa.kotula@curie.fr", "Ewa Kotula", "TeamMember"},
			
			{"lebaccon", "patricia123", "patricia.le-baccon@curie.fr", "Patricia Le Baccon", "TeamLeader"},
			{"lecorgne", "lecorgne123", "tristan.lecorgne@curie.fr", "Tristan Lecorgne", "TeamMember"},
			
			{"oleroy", "leroy123", "olivier.leroy@curie.fr", "Olivier Leroy", "TeamLeader"},
			{"pmaiuri", "maiuri123", "paolo.maiuri@curie.fr", "Paolo Maiuri", "TeamMember"},
			{"messaoud", "messaoudi123", "cedric.messaoudi@curie.fr", "Cedric Messaoudi", "TeamMember"},
			
			{"tpiolot", "piolot123", "tristan.piolot@curie.fr", "Tristan Piolot", "TeamLeader"},
			{"tpollex", "pollex123", "tim.pollex@curie.fr", "Tim Pollex", "TeamMember"},
			{"lprender", "prendergast123", "lisa.prendergast@curie.fr", "Lisa Prendergast", "TeamMember"},
			{"orenaud", "renaud123", "olivier.renaud@curie.fr", "Olivier Renaud", "TeamLeader"},
			
			{"broelens", "roelens123", "baptiste.roelens@curie.fr", "Baptiste Roelens", "TeamMember"},
			{"kromeo", "romeo123", "kelly.romeo@curie.fr", "Kelly Romeo", "TeamMember"},
			{"trubi", "roudot123", "philippe.roudot@inria.fr", "Philippe Roudot", "TeamMember"},
			{"trubin", "rubin123", "thomas.rubin@curie.fr", "Thomas Rubin", "TeamMember"},
			
			{"salamero", "salamero123", "salamero@curie.fr", "Jean Salamero", "FacilityManager"},
			{"kschauer", "schauer123", "kristine.Schauer@curie.fr", "Kristine Schauer", "TeamMember"},
			{"sengmani", "sengmanivong123", "lucie.sengmanivong@curie.fr", "Lucie Sengmanivong", "TeamLeader"},
			
			{"ihurbain", "urbain123", "ilse.urbain@curie.fr", "Ilse Urbain", "TeamMember"},
//			{"fwaharte", "waharte123", "francois.waharte@curie.fr", "Fran�ois Waharte", "TeamMember"},
			{"anup", "anup123", "anup@strandls.com", "Anup Kulkarni", "TeamMember"},
			{"arunabha", "arunabha123", "arunabha@strandls.com", "Arunabha Ghosh", "TeamMember"},
			{"santhosh", "santhosh123", "santhosh@strandls.com", "Santhosh Kumar", "TeamMember"},
			{"nimisha", "nimisha123", "nimisha@strandls.com", "Nimisha Gupta", "TeamMember"},
			{"varun", "varun123", "varun@strandls.com", "Varun Agarwal", "TeamMember"}
		};
   	
  	static final String[] projects = {
			"CDYL",
			"CENP proteines - Humain",
			"Dbait",
//			"Drosophile - prot�ines r�plication",
			"ES_FRAP",
			"ES_LIVE",
			"exocytose:recyclage",
			"FLIMDynamique",
			"Germarium",
			"Histone H2Az - souris",
			"HP1",
			"HR_SIM",
			"LSD 1",
			"LSM_Photo",
			"Micropattern",
			"Paolo's project",
			"PICT@BDD",
			"PICT@Lhomond",
			"PreClem",
//			"Projet Am�lie",
			"Projet Ewa",
			"Projet Fab",
			"Projet Nathalie",
			"Projet Pauline",  
			"Projet PICT@Orsay", 
			"Projet PICT@Pasteur",
//			"Proteines nucl�aires chez la Levure",
			"SENM7",
			"testAPI",
			"Tests FLIM",
			"TomoJeol",
			"traffic intra-cellulaire",
			"Strand Test",
			"Strand Sample"
  	};
	
	static final String[][] projectUsers = {
		{"escamill", "oleroy", "orenaud"},
		{"lprender", "lebaccon"},
		{"berthault", "acroset", "ekotula", "fab", "chabosse"},
		{"broelens", "lebaccon"},
		{"oleroy", "orenaud"},
		{"lgiorget", "tpiolot", "tpollex"},
		{"sbardin", "salamero", "ppaulgi"},
		{"jboulang", "ppaulgi", "lecorgne", "trubi", "fwaharte"},
		{"trubin", "oleroy", "tpiolot", "orenaud"},
		{"boyarchu", "lebaccon"},
		{"scantalo", "lebaccon"},
		{"oleroy", "tpiolot", "orenaud"},
		{"oleroy", "orenaud"},
		{"ocochet", "oleroy", "orenaud"},
		{"kschauer", "jgrossie", "lebaccon"},
		{"pmaiuri", "ppaulgi"},
		{"oleroy", "orenaud"},
		{"jboulang", "fraisier", "ppaulgi", "sengmani", "fwaharte", "salamero"},
		{"jboulang", "delevoye", "ppaulgi", "ihurbain"},
		{"acroset", "chabosse", "fab"},
		{"ekotula", "chabosse", "fab"},
		{"fab"},
		{"berthault", "chabosse", "fab"},
		{"chabosse"},
		{"chabosse", "fab"},
		{"lebaccon"},
		{"barbour", "ppaulgi",  "salamero", "sengmani"},
		{"kromeo", "lebaccon"},
		{"jboulang", "fab", "ppaulgi", "lecorgne", "trubi"},
		{"chabosse"},
		{"messaoud", "chabosse", "fab"},
		{"mirondel","ppaulgi"},
		{"anup","varun","nimisha","arunabha","santhosh"},
		{"anup","varun","nimisha","arunabha","santhosh"}
	};
}

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

package com.strandgenomics.imaging.iserver.services.impl.web;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.SearchCondition;

/**
 * Some util methods to parse the request strings
 * 
 * @author santhosh
 * 
 */
public class ParseUtil {

    /**
     * gson instance to use for parsing
     */
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    
    /**
     * Parse a conditions list
     * 
     * @param conditionsList
     * @return set of search conditions
     */
    public static Set<SearchCondition> decodeConditions(List<Map<String, String>> conditionsList) {
        Set<SearchCondition> ret = new HashSet<SearchCondition>();
        for (Map<String, String> map : conditionsList) {
            SearchCondition searchCondition = getSearchCondition(map);
            if (searchCondition != null)
                ret.add(getSearchCondition(map));
        }
        return ret;
    }

    /**
     * Parse search condition
     * 
     * @param map
     * @return search condition
     */
    private static SearchCondition getSearchCondition(Map<String, String> map) {
        String fieldName = map.get("fieldName");
        String min = map.get("min");
        String max = map.get("max");

        SearchCondition condition = null;
        AnnotationType type = AnnotationType.valueOf(map.get("fieldType"));
        switch (type) {
        case Integer:
            condition = new SearchCondition(fieldName, safeGetLong(min), safeGetLong(max));
            break;
        case Real:
            condition = new SearchCondition(fieldName, safeGetDouble(min), safeGetDouble(max));
            break;
        case Text:
            condition = new SearchCondition(fieldName, min, max);
            break;
        case Time:
            condition = new SearchCondition(fieldName, safeGetTimeStamp(min), safeGetTimeStamp(max));
            break;
        }
        return condition;
    }

    /**
     * Decode string based on {@link AnnotationType} provided
     * 
     * @param string
     * @param type
     * @return object is parse successful. <code>null</code> otherwise
     */
    public static Object decodeAnnotationObject(String string, AnnotationType type) {
        if (string == null || string.length() == 0)
            return null;
        switch (type) {
        case Integer:
            return Long.parseLong(string);
        case Real:
            return Double.parseDouble(string);
        case Text:
            return string;
        case Time:
            return gson.fromJson('"'+ string + '"', Timestamp.class);
        }
        return null;
    }

    /**
     * null safe long parse
     * 
     * @param string
     * @return
     */
    private static Long safeGetLong(String string) {
        return (string == null) ? null : Long.parseLong(string);
    }

    /**
     * null safe double parse
     * 
     * @param string
     * @return
     */
    private static Double safeGetDouble(String string) {
        return (string == null) ? null : Double.parseDouble(string);
    }

    /**
     * null safe timestamp parse
     * 
     * @param string
     * @return
     */
    private static Timestamp safeGetTimeStamp(String string) {
        if (string == null)
            return null;
        return gson.fromJson('"'+ string + '"', Timestamp.class);
    }
}

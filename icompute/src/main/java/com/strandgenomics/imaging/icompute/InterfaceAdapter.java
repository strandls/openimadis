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

package com.strandgenomics.imaging.icompute;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context)
	{
		final JsonObject wrapper = new JsonObject();
		wrapper.addProperty("class", object.getClass().getName());
		wrapper.add("data", context.serialize(object));
		return wrapper;
	}

	public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject wrapper = (JsonObject) elem;
		final JsonElement typeName = get(wrapper, "class");
		final JsonElement data = get(wrapper, "data");
		final Type actualType = typeForName(typeName);
		return (T)context.deserialize(data, actualType);
	}

	private Type typeForName(final JsonElement typeElem)
	{
		try
		{
			return Class.forName(typeElem.getAsString());
		}
		catch (ClassNotFoundException e)
		{
			throw new JsonParseException(e);
		}
	}

	private JsonElement get(final JsonObject wrapper, String memberName)
	{
		final JsonElement elem = wrapper.get(memberName);
		if (elem == null)
			throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
		return elem;
	}
}
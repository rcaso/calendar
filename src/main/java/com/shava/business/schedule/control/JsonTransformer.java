package com.shava.business.schedule.control;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalTime;

import javax.enterprise.context.ApplicationScoped;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.shava.business.schedule.entity.User;

@ApplicationScoped
public class JsonTransformer {

	public JsonTransformer() {
		// TODO Auto-generated constructor stub
	}

	public JsonDocument convertoToJson(User user) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);
		return JsonDocument.create("user_" + user.getId(), JsonObject.fromJson(json));
	}

	public User convertFromJson(JsonDocument json) throws IOException {
		Type listOfObject = new TypeToken<User>() {
		}.getType();
		// Creates the json object which will manage the information received
		GsonBuilder builder = new GsonBuilder();
		// Register an adapter to manage the date types as long values
		builder.registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
			public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				int hour = json.getAsJsonObject().get("hour").getAsInt();
				int minute = json.getAsJsonObject().get("minute").getAsInt();
				int second = json.getAsJsonObject().get("second").getAsInt();
				return LocalTime.of(hour, minute, second);
			}
		});
		Gson gson = builder.create();
		User user = gson.fromJson(json.content().toString(), listOfObject);
		return user;
	}

}

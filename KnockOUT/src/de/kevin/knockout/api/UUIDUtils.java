package de.kevin.knockout.api;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UUIDUtils {
	
	static int i = 0;

	public static String getName(String uuid) throws org.json.simple.parser.ParseException, ParseException {
		String url = "https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names";
		try {
			String nameJson = IOUtils.toString(new URL(url));
			JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
			String playerSlot = nameValue.get(nameValue.size() - 1).toString();
			JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
			return nameObject.get("name").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "FEHLER";
	}
	
	public static List<String> getNames(List<String> list) {
		List<String> names = new ArrayList<String>();
		list.forEach(name -> {
			try {
				names.add(getName(list.get(i)));
				i++;
			} catch (org.json.simple.parser.ParseException | ParseException e) {
			}
		});
		i = 0;
		return names;
	}

}

package com.nazdaq.payrms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nazdaq.payrms.model.Company;

class ExecutorTest {
	public static void main(String[] args) throws IOException {
		URL url = new URL("http://127.0.0.1:8080/payrms/companyListAndroid");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		BufferedReader reader = null;
		StringBuffer buffer = new StringBuffer();

		InputStream stream = connection.getInputStream();

		reader = new BufferedReader(new InputStreamReader(stream));

		String line = "";

		while ((line = reader.readLine()) != null) {
			buffer.append(line + "\n");

		}
		ObjectMapper mapper = new ObjectMapper();
		// ObjectMapper mapper = new ObjectMapper();
		Map map = mapper.readValue(buffer.toString(), Map.class);
		List lists = (List) map.get("companyInfo");
		List<Company> companies = new ArrayList<>();
		int i = 0;
		for (Object object : lists) {
			Map mapl = (Map) lists.get(i);
			Company company = new Company();
			company.setId(Integer.parseInt(mapl.get("id").toString()));
			company.setName(mapl.get("name").toString());
			company.setEmail(mapl.get("email").toString());
			companies.add(company);
			i++;
		}
		
		int is=0;

	}

}
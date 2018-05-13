package com.finder.service;

import com.finder.model.Site;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileCache implements Cache {
    @Override
    public List<Site> get() {
        String jsonData = readFile("cache.json");

        if (jsonData == null) {
            return new ArrayList<Site>();
        }

        JSONArray jsonSites = new JSONArray(jsonData);
        List<Site> sites = new ArrayList<Site>();

        for(int i = 0; i < jsonSites.length(); i++) {
            Site site = new Site();

            String id = jsonSites.getJSONObject(i).getString("id");
            String name = jsonSites.getJSONObject(i).getString("name");
            site.setName(name);
            site.setId(id);

            sites.add(site);
        }

        return sites;
    }

    @Override
    public void update(List<Site> sites) {
        JSONArray jsonSites = new JSONArray(sites);

        try {
            FileWriter fileWriter = new FileWriter("cache.json");
            fileWriter.write(jsonSites.toString());
            fileWriter.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

    public static String readFile(String filename) {
        String result = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            return result;
        }

        return result;
    }
}

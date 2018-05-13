package com.finder.service;

import com.finder.model.Site;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

@Service
public class SiteService {

    @Autowired
    private Cache cache;

    public List<Site> searchSites(String keyword) throws IOException {
        List<Site> sites = cache.get();;

        if (sites.size() == 0) {
            sites = this.updateCache();
        }

        List<Site> foundSites = this.findKeyword(sites, keyword);

        if (foundSites.size() == 0) {
            sites = this.updateCache();

            foundSites = this.findKeyword(sites, keyword);
        }

        return foundSites;
    }

    private  List<Site> updateCache() throws IOException {
        List<Site> sites = this.fetchSites();
        cache.update(sites);

        return sites;
    }

    private List findKeyword(List<Site> sites, String keyword) throws IOException {
        List<Site> matchedSites = new ArrayList<Site>();

        for(Site site : sites) {
            String id = site.getId();
            String name = site.getName();

            Boolean isMatched = id.indexOf(keyword) > -1 || name.indexOf(keyword) > -1;

            if (isMatched) {
                matchedSites.add(site);
            }

        }

        return matchedSites;
    }

    public List<Site> fetchSites() throws IOException {
        JSONArray jsonSites = null;
        List<Site> sites = new ArrayList<Site>();

        try {
            JSONObject json = this.getSites();
            jsonSites =  json.getJSONArray("sites");
        } catch (IOException e) {
            throw new IOException(e);
        }

        for (int i = 0; i < jsonSites.length(); i++)  {
            Site site = new Site();
            String id = jsonSites.getJSONObject(i).getString("cuid");
            String name = jsonSites.getJSONObject(i).getString("name");

            site.setId(id);
            site.setName(name);

            sites.add(site);
        }

        return sites;
    }

    private JSONObject getSites() throws IOException {
        InputStream is = new URL("").openStream();
        JSONObject json;

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            json = new JSONObject(sb.toString());
        } finally {
            is.close();
        }

        return json;
    }
}
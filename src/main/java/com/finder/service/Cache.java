package com.finder.service;

import com.finder.model.Site;

import java.util.List;

public interface Cache {
    public List<Site> get();
    public void update(List<Site> sites);
}

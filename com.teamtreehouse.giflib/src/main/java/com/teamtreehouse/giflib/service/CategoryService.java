package com.teamtreehouse.giflib.service;

import com.teamtreehouse.giflib.model.Category;
import sun.util.resources.cldr.ebu.CalendarData_ebu_KE;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();
    public Category findById(Long id);
    public void save(Category category);
    public void delete(Category category);
}

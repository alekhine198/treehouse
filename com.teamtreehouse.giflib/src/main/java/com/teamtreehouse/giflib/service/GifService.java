package com.teamtreehouse.giflib.service;

import com.teamtreehouse.giflib.model.Gif;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GifService {
    public List<Gif> findAll();
    public Gif findById(Long id);
    public void save(Gif gif, MultipartFile file);
    public void toggleFavorite(Gif gif);
    public void delete(Gif gif);
}

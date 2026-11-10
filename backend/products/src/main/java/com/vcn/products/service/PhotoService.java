package com.vcn.products.service;

import com.vcn.products.model.Photo;

import java.util.List;

public interface PhotoService {
    List<Photo> saveAll(List<Photo> photos);
}

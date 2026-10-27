package com.vcn.products.service;

import com.vcn.products.model.Photo;
import com.vcn.products.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    @Transactional
    public List<Photo> saveAll(List<Photo> photos) {
        return photoRepository.saveAll(photos);
    }
}

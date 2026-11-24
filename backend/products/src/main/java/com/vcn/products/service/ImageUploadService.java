package com.vcn.products.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vcn.products.repository.PhotoRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.cloudinary.*;

@Service
public class ImageUploadService {

    Dotenv dotenv = Dotenv.configure().directory("products").load();
    Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    Map options = ObjectUtils.asMap(
            "use_filename", true,
            "unique_filename", true,
            "overwrite", true
    );

    private final PhotoRepository photoRepository;

    public ImageUploadService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Map<String, String> uploadImage(@NonNull MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            Map<String, Object> uploadResult = cloudinary.uploader().upload(tempFile.toFile(), ObjectUtils.emptyMap());
            Files.delete(tempFile);
            Map<String, String> result = new HashMap<>();
            result.put("url", (String) uploadResult.get("url"));
            result.put("publicId", (String) uploadResult.get("public_id"));
            return result;
        } catch (IOException exception) {
            throw new RuntimeException("No ha sido posible subir la imagen", exception);
        }
    }

    public String deleteImageByPublicId(String publicId) {
        try {
            photoRepository.deleteByPublicId(publicId);
            Map<String, String> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return (String) result.get("result");
        } catch (IOException exception) {
            throw new RuntimeException("Could not delete image", exception);
        }
    }

}

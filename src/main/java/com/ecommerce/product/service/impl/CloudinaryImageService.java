package com.ecommerce.product.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.product.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageService implements ImageService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) {

        try {

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap());

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed.");
        }
    }

    @Override
    public void delete(String imageUrl) {

        try {

            String publicId = extractPublicId(imageUrl);

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

        } catch (Exception e) {
            throw new RuntimeException("Unable to delete image.");
        }
    }

    private String extractPublicId(String imageUrl) {

        String[] parts = imageUrl.split("/");

        String filename = parts[parts.length - 1];

        return filename.substring(0, filename.lastIndexOf("."));
    }
}

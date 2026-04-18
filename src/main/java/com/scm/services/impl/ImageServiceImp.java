package com.scm.services.impl;

import java.io.IOException;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.scm.services.ImageService;

@Service
public class ImageServiceImp implements ImageService {

    private Cloudinary cloudinary;

    public ImageServiceImp(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage) {

        String fileName = UUID.randomUUID().toString();

        try {

            // ✅ FIX 1: proper way to get bytes
            byte[] data = contactImage.getBytes();

            // ✅ FIX 2: SAME public_id use karo
            cloudinary.uploader().upload(data, Map.of(
                    "public_id", fileName
            ));

            // ✅ FIX 3: semicolon missing tha
            return this.getURLFromPublicId(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getURLFromPublicId(String publicId) {

        return cloudinary.url()
                .transformation(
                        new com.cloudinary.Transformation()
                                .width(100)
                                .height(100)
                                .crop("fill")
                                .gravity("face")
                )
                .generate(publicId);
    }
}
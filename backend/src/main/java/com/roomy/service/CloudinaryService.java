package com.roomy.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    
    @Autowired
    private Cloudinary cloudinary;
    
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "auto"
            )
        );
        
        return (String) uploadResult.get("secure_url");
    }
    
    public String uploadDocument(MultipartFile file, Long userId) throws IOException {
        String folder = "roomy/documents/" + userId;
        return uploadFile(file, folder);
    }
    
    public String uploadRoomImage(MultipartFile file, Long roomId) throws IOException {
        String folder = "roomy/rooms/" + roomId;
        return uploadFile(file, folder);
    }
    
    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
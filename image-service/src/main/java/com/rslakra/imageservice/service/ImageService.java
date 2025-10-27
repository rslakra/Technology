package com.rslakra.imageservice.service;

import com.rslakra.imageservice.exception.ImageServiceException;
import com.rslakra.imageservice.exception.ImageNotFoundException;
import com.rslakra.imageservice.domain.Image;
import com.rslakra.imageservice.repository.ImageRepository;
import com.rslakra.imageservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    /**
     * @param file
     * @return
     */
    public Image saveImage(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new ImageServiceException("Invalid file name:" + fileName);
            }

            Image image = new Image(fileName, new Date(), file.getContentType(), file.getBytes(), file.getSize());

            return imageRepository.save(image);
        } catch (IOException ex) {
            throw new ImageServiceException("Unable to save file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * @param id
     * @return
     */
    public Image getImage(Integer id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("File not found with id:" + id));
    }

    /**
     * Returns all images from the database.
     *
     * @return
     */
    public List<Image> getAllImage() {
        return imageRepository.findAll();
    }
}

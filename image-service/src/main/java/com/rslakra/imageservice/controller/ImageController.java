package com.rslakra.imageservice.controller;

import com.rslakra.imageservice.domain.Image;
import com.rslakra.imageservice.exception.ImageServiceException;
import com.rslakra.imageservice.payload.ImageResponse;
import com.rslakra.imageservice.service.ImageService;
import com.rslakra.imageservice.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Image Controller to handle image requests.
 */
@RestController
//@RequestMapping("/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final HashSet<String> allowedExtensions = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));


    @Autowired
    private ImageService imageService;

    /**
     * Returns the file download path for the image.
     *
     * @param image
     * @return
     */
    private final String getDownloadUrl(Image image) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(String.valueOf(image.getId()))
                .toUriString();
    }

    /**
     * @param file
     * @return
     */
    @PostMapping(path = "/upload", produces = {"application/json;charset=UTF-8"})
    public ImageResponse upload(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty() && file.getOriginalFilename().contains(".")) {
            final String extension = Utils.getExtension(file);
            if (!allowedExtensions.contains(extension))
                throw new ImageServiceException("Invalid file type:" + file.getOriginalFilename());
        }

        Image image = imageService.saveImage(file);
        String fileDownloadUri = getDownloadUrl(image);
        return new ImageResponse(image.getTitle(), image.getCreatedOn(), file.getContentType(), file.getSize(), fileDownloadUri);
    }

    /**
     * @param files
     * @return
     */
    @PostMapping(path = "/uploadImages", produces = {"application/json;charset=UTF-8"})
    public List<ImageResponse> uploadImages(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> upload(file))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/download/{id}", produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<Resource> download(@PathVariable Integer id) {
        // Load file from database
        Image image = imageService.getImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getTitle() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }


    /**
     * Returns all images.
     *
     * @return
     */
    @GetMapping(path = "/loadAllImages", produces = {"application/json;charset=UTF-8"})
    public List<ImageResponse> loadAllImages() {
        List<Image> allImages = imageService.getAllImage();
        final List<ImageResponse> allImagesResponse = new ArrayList<>();
        allImages.forEach(image -> allImagesResponse.add(new ImageResponse(image.getTitle(), image.getCreatedOn(), image.getType(), image.getSize(), getDownloadUrl(image))));

        return allImagesResponse;
    }

}

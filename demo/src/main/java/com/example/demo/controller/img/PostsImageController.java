package com.example.demo.controller.img;

import com.example.demo.dto.PostsImageDto;
import com.example.demo.service.post.PostsImageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
@RequiredArgsConstructor
@RestController
public class PostsImageController {

    private final PostsImageService photoService;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @CrossOrigin
    @GetMapping(
            value = "/thumbnail/{id}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE}
    )
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) throws IOException {

        // 이미지가 저장된 절대 경로 추출
        String absolutePath =
                new File("").getAbsolutePath() + File.separator + File.separator;
        String path;


        if(id != 0) {  // 전달되어 온 이미지가 기본 썸네일이 아닐 경우
            PostsImageDto postsImageDto = photoService.findByFileId(id);
            path = postsImageDto.getFilePath();
        }
        else {  // 전달되어 온 이미지가 기본 썸네일일 경우
            path = "images" + File.separator + "thumbnail" + File.separator + "thumbnail.png";
        }

        System.out.println("path = " + path);
        System.out.println("absolutePath =" + absolutePath);
        // FileInputstream의 객체를 생성하여
        // 이미지가 저장된 경로를 byte[] 형태의 값으로 encoding
        InputStream imageStream = new FileInputStream(absolutePath + path);

        byte[] imageByteArray = imageStream.readAllBytes();
        imageStream.close();
        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(
            value = "/image/{id}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE}
    )
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws IOException {
        PostsImageDto photoDto = photoService.findByFileId(id);
        String absolutePath
                = new File("").getAbsolutePath() + File.separator + File.separator;
        String path = photoDto.getFilePath();

        System.out.println("path = " + path);
        System.out.println("absolutePath =" + absolutePath);
        InputStream imageStream = new FileInputStream(absolutePath + path);

        byte[] imageByteArray = imageStream.readAllBytes();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}

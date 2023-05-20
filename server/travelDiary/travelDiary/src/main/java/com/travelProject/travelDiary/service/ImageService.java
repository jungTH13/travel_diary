package com.travelProject.travelDiary.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class ImageService {

    public MultipartFile createThumbnail(MultipartFile image)throws Exception {
        int orientation = getOrientation(image);
        BufferedImage bufferedImage = convertMultipartFileToBufferedImage(image);
        BufferedImage thumbnailImage = resizeImage(bufferedImage,240,240, orientation);
        MultipartFile multipartFile = convertBufferedImageToMultipartFile(thumbnailImage,image.getOriginalFilename().split("\\.")[0]);

        return multipartFile;
    }

    private BufferedImage convertMultipartFileToBufferedImage(MultipartFile originalFile)throws Exception{
        InputStream inputStream = new ByteArrayInputStream(originalFile.getBytes());
        BufferedImage originalImage = ImageIO.read(inputStream);

        return originalImage;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight,int orientation) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .keepAspectRatio(true)
                .outputFormat("JPEG")
                .outputQuality(1)
                .rotate(360-orientation)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }


    private MultipartFile convertBufferedImageToMultipartFile(BufferedImage image,String name)throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", out);

        byte[] bytes = out.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());

        return new CustomMultipartFile(bytes, inputStream, name, name+".jpeg", "jpeg", bytes.length);
    }

    private int getOrientation(MultipartFile imageMultiFile) throws MetadataException, IOException {
        int orientation = 1; // 회전정보, 1. 0도, 3. 180도, 6. 270도, 8. 90도 회전한 정보

        Metadata metadata; // 이미지 메타 데이터 객체
        Directory directory; // 이미지의 Exif 데이터를 읽기 위한 객체

        try (InputStream inputStream = imageMultiFile.getInputStream()) {
            metadata = ImageMetadataReader.readMetadata(inputStream);
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if(directory != null){
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
            }
        } catch (Exception e) {
            orientation = 1;
        }

        if(orientation == 1) return 0;
        if(orientation == 8) return 90;
        if(orientation == 3) return 180;
        return 270;
    }

    public static class CustomMultipartFile implements MultipartFile {

        private final byte[] bytes;
        String name;
        String originalFilename;
        String contentType;
        boolean isEmpty;
        long size;

        InputStream inputStream;

        public CustomMultipartFile(byte[] bytes, InputStream inputStream , String name, String originalFilename, String contentType,
                                   long size) {
            this.bytes = bytes;
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.size = size;
            this.isEmpty = false;
            this.inputStream = inputStream;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return isEmpty;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return bytes;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public void transferTo(File dest) throws IllegalStateException {
        }

    }
}

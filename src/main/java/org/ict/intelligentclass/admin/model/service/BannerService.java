package org.ict.intelligentclass.admin.model.service;

import lombok.RequiredArgsConstructor;
import org.ict.intelligentclass.admin.jpa.entity.BannerEntity;
import org.ict.intelligentclass.admin.jpa.repository.BannerRepository;
import org.ict.intelligentclass.admin.model.dto.BannerDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    // 이미지 저장 경로 설정
    private final Path imageStorageLocation = Paths.get("D:/intell2/public/images/banner");

    public List<BannerDto> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BannerDto getLatestBanner() {
        Optional<BannerEntity> banner = bannerRepository.findTopByOrderByIdDesc();
        return banner.map(this::convertToDto).orElse(null);
    }

    public BannerEntity getBannerEntityById(Long id) {
        return bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Banner not found"));
    }

    public BannerDto getBannerById(Long id) {
        return bannerRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    public BannerEntity saveBanner(BannerEntity bannerEntity) {
        return bannerRepository.save(bannerEntity);
    }

    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    public String storeImage(MultipartFile imageFile) throws IOException {
        if (Files.notExists(imageStorageLocation)) {
            Files.createDirectories(imageStorageLocation);
        }

        String fileName = imageFile.getOriginalFilename();
        Path targetLocation = imageStorageLocation.resolve(fileName);

        int counter = 1;
        String newFileName = fileName;
        while (Files.exists(targetLocation)) {
            int dotIndex = fileName.lastIndexOf('.');
            String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
            newFileName = baseName + "_" + counter + extension;
            targetLocation = imageStorageLocation.resolve(newFileName);
            counter++;
        }

        Files.copy(imageFile.getInputStream(), targetLocation);

        return "/images/banner/" + newFileName;
    }

    public BannerDto convertToDto(BannerEntity bannerEntity) {
        BannerDto bannerDto = new BannerDto();
        bannerDto.setId(bannerEntity.getId());
        bannerDto.setTitle(bannerEntity.getTitle());
        bannerDto.setImageUrl(bannerEntity.getImageUrl());
        bannerDto.setLinkUrl(bannerEntity.getLinkUrl());
        return bannerDto;
    }

    public BannerEntity convertToEntity(BannerDto bannerDto) {
        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setId(bannerDto.getId());
        bannerEntity.setTitle(bannerDto.getTitle());
        bannerEntity.setImageUrl(bannerDto.getImageUrl());
        bannerEntity.setLinkUrl(bannerDto.getLinkUrl());
        return bannerEntity;
    }
}

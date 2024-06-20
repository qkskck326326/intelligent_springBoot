package org.ict.intelligentclass.admin.model.service;

import lombok.RequiredArgsConstructor;
import org.ict.intelligentclass.admin.jpa.entity.BannerEntity;
import org.ict.intelligentclass.admin.jpa.repository.BannerRepository;
import org.ict.intelligentclass.admin.model.dto.BannerDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<BannerDto> getAllBanners() {
        return bannerRepository.findAll().stream().map(BannerEntity::toDto).collect(Collectors.toList());
    }

    public BannerDto createBanner(BannerDto bannerDto) {
        BannerEntity bannerEntity = BannerEntity.fromDto(bannerDto);
        bannerRepository.save(bannerEntity);
        return bannerEntity.toDto();
    }

    public BannerDto updateBanner(int id, BannerDto bannerDto) {
        BannerEntity bannerEntity = bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Banner not found"));
        bannerEntity.updateFromDto(bannerDto);
        bannerRepository.save(bannerEntity);
        return bannerEntity.toDto();
    }

    public void deleteBanner(int id) {
        bannerRepository.deleteById(id);
    }
}

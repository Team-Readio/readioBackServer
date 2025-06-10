// ✅ ProfileService.java
package com.team.teamreadioserver.profile.service;

import com.team.teamreadioserver.profile.dto.ProfileRequestDTO;
import com.team.teamreadioserver.profile.dto.ProfileResponseDTO;
import com.team.teamreadioserver.profile.entity.Profile;
import com.team.teamreadioserver.profile.entity.ProfileImg;
import com.team.teamreadioserver.profile.enums.PrivateStatus;
import com.team.teamreadioserver.profile.repository.ProfileImgRepository;
import com.team.teamreadioserver.profile.repository.ProfileRepository;
import com.team.teamreadioserver.user.entity.User;
import com.team.teamreadioserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileImgRepository profileImgRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional
    public void upsertProfile(ProfileRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Profile profile = profileRepository.findByUser_UserId(user.getUserId()).orElse(null);

        boolean isNew = (profile == null);
        String penName;

        if (dto.getPenName() == null || dto.getPenName().isEmpty()) {
            penName = generateDefaultPenName();
        } else {
            penName = dto.getPenName();
            boolean isDuplicate = profileRepository.existsByPenName(penName);

            // 본인의 기존 필명은 허용
            if (isDuplicate && (isNew || !penName.equals(profile.getPenName()))) {
                throw new IllegalArgumentException("이미 사용 중인 필명입니다.");
            }
        }

        if (isNew) {
            profile = Profile.builder()
                    .user(user)
                    .penName(penName)
                    .biography(dto.getBiography())
                    .isPrivate(parsePrivateStatus(dto.getIsPrivate()))
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            profile.setPenName(penName);
            profile.setBiography(dto.getBiography());
            profile.setIsPrivate(parsePrivateStatus(dto.getIsPrivate()));
        }

        profileRepository.save(profile);
        saveProfileImageIfExists(dto.getImage(), user, profile);
    }


    private PrivateStatus parsePrivateStatus(String status) {
        return "PRIVATE".equalsIgnoreCase(status) ? PrivateStatus.PRIVATE : PrivateStatus.PUBLIC;
    }

    private void saveProfileImageIfExists(MultipartFile image, User user, Profile profile) {
        if (image == null || image.isEmpty()) return;

        String originalName = image.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String saveName = user.getUserId() + "_IMG" + extension;
        String projectRoot = System.getProperty("user.dir") + File.separator + "readioBackServer";
        String saveFolder = projectRoot + File.separator + "src" + File.separator + "main"
                + File.separator + "resources" + File.separator + "static"
                + File.separator + "img" + File.separator + "profile";
//        String saveFolder = new File("/src/main/resources/static/img/profile").getPath();
        String savePath = saveFolder + File.separator + saveName;

        try {
            image.transferTo(new File(savePath));
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }

        profileImgRepository.deleteByProfile(profile);

        ProfileImg profileImage = ProfileImg.builder()
                .profile(profile)
                .originalName(originalName)
                .saveName(saveName)
                .build();

        profileImgRepository.save(profileImage);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Profile profile = profileRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));

        ProfileImg img = profileImgRepository.findByProfile(profile).orElse(null);

        return ProfileResponseDTO.builder()
                .profileId(profile.getProfileId())
                .penName(profile.getPenName())
                .biography(profile.getBiography())
                .isPrivate(profile.getIsPrivate().name())
                .imageUrl(img != null ? "/img/profile/" + img.getSaveName() : null)
                .build();
    }

    @Transactional
    public void deleteProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Profile profile = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다."));

        deleteProfileImageFile(profile);
        profileRepository.delete(profile);
    }

    @Transactional
    public void deleteProfileImage(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Profile profile = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다."));

        deleteProfileImageFile(profile);
    }

    private void deleteProfileImageFile(Profile profile) {
        ProfileImg img = profileImgRepository.findByProfile(profile).orElse(null);
        if (img == null) return;

        profileImgRepository.delete(img);
        File file = new File("src/main/resources/static/img/profile/" + img.getSaveName());
        if (file.exists()) file.delete();
    }

    private String generateDefaultPenName() {
        int suffix = 1;
        String base = "Readio 기본 필명 ";
        while (profileRepository.existsByPenName(base + suffix)) {
            suffix++;
        }
        return base + suffix;
    }
    public boolean isPenNameTaken(String penName) {
        return profileRepository.existsByPenName(penName);
    }

}

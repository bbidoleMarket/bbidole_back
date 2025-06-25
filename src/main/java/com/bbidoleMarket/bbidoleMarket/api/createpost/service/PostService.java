package com.bbidoleMarket.bbidoleMarket.api.createpost.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.image.enums.ImageFolder;
import com.bbidoleMarket.bbidoleMarket.api.image.service.UploadImageService;
import com.bbidoleMarket.bbidoleMarket.api.createpost.dto.CreatePostDto;
import com.bbidoleMarket.bbidoleMarket.api.createpost.repository.PostRepository;
import com.bbidoleMarket.bbidoleMarket.api.login.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UploadImageService uploadImageService;
    private final UserRepository userRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Post createPost(CreatePostDto dto, MultipartFile image, String id) {

        Long userId = Long.parseLong(id);
        Optional<User> user = userRepository.findById(userId);
        try {
            // 이미지 업로드
            String imageUrl = uploadImageService.uploadImage(image, ImageFolder.LIST);

            // Post 엔티티 생성
            Post post = Post.createPost(
                    dto.getTitle(),
                    dto.getPrice(),
                    imageUrl,
                    dto.getDescription(),
                    user.orElse(null)
            );

            // 저장 및 반환
            return postRepository.save(post);

        } catch (IOException e) {
            throw new BadRequestException(ErrorStatus.FAIL_UPLOAD_EXCEPTION.getMessage());
        }
    }

    /**
     * 게시글 요청 유효성 검사
     */
    private void validatePostRequest(CreatePostDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BadRequestException("제목을 입력해주세요.");
        }

        if (dto.getPrice() == null || dto.getPrice() < 0) {
            throw new BadRequestException("올바른 가격을 입력해주세요.");
        }

        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new BadRequestException("상품 설명을 입력해주세요.");
        }
    }
}
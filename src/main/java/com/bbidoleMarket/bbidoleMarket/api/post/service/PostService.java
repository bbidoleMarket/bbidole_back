package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.image.enums.ImageFolder;
import com.bbidoleMarket.bbidoleMarket.api.image.service.UploadImageService;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSimpleDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostUpdateReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.PostRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BaseException;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.exception.UnauthorizedException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UploadImageService uploadImageService;

    @Transactional(readOnly = true)
    public PostDetailResDto findById(Long postId, String jwtId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
            .orElseThrow(
                () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));

        try {
            Long userId = Long.parseLong(jwtId);
            return PostDetailResDto.fromPost(post, isWriter(post.getUser().getId(), userId));
        } catch (NumberFormatException ne) {
            log.info(ne.toString());
            return PostDetailResDto.fromPost(post, false);
        }
    }

    public void update(PostUpdateReqDto dto, MultipartFile image, String id) {
        Long userId = Long.parseLong(id);
        Post post = postRepository.findById(dto.getPostId())
            .orElseThrow(
                () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));

        // 작성자와 수정자가 동일한지 체크
        if (!isWriter(post.getUser().getId(), userId)) {
            throw new UnauthorizedException(
                ErrorStatus.OTHERS_USER_INFO_NOT_ALLOWED_EXCEPTION.getMessage());
        }

        if (image == null || image.isEmpty()) {
            post.updatePost(dto.getTitle(), dto.getPrice(), dto.getDescription());
            return;
        }

        try {
            String imageUrl = uploadImageService.uploadImage(image, ImageFolder.LIST);
            post.updatePost(dto.getTitle(), dto.getPrice(), dto.getDescription(), imageUrl);
        } catch (IOException e) {
            throw new BaseException(
                ErrorStatus.FAIL_UPLOAD_EXCEPTION.getHttpStatus(),
                "이미지 업로드에 실패했습니다.(이미지 수정)"
            );
        }
    }


    @Transactional(readOnly = true)
    public List<PostSimpleDto> findByUserId(Long userId) {
        // isDeleted = false 조건 추가
        List<Post> posts = postRepository.findByUserIdAndIsDeletedFalse(userId, Pageable.unpaged())
            .getContent();
        return posts.stream().map(PostSimpleDto::fromPost).toList();
    }

    // TODO User Entity의 List<Post> 찾는거하고 Post Entity에서 찾는거 중에 어떤게 빠른지 검증 필요
    @Transactional(readOnly = true)
    public PageResDto<PostSimpleDto> findByUserId(Long userId, int page, int size) {

//        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
//        Page<Post> postPage = postRepository.findByUserIdAndIsDeletedFalse(userId, pageable);

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(
            userId, pageable);
        Page<PostSimpleDto> postSimpleDto = postPage.map(PostSimpleDto::fromPost);
        return new PageResDto<>(postSimpleDto);
    }

    private boolean isWriter(Long writerId, Long userId) {
        return writerId.equals(userId);
    }

    @Transactional
    public void softDeletePost(Long postId, String id) {
        Long userId = Long.parseLong(id);
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
            .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("게시글 삭제 권한이 없습니다.");
        }

        post.markAsDeleted();

    }
}

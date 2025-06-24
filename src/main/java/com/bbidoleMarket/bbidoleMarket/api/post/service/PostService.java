package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.image.enums.ImageFolder;
import com.bbidoleMarket.bbidoleMarket.api.image.service.UploadImageService;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSaveReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSimpleDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostUpdateReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.PostRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final UploadImageService uploadImageService;

    @Transactional(readOnly = true)
    public PostDetailResDto findById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));
        return PostDetailResDto.fromPost(post);
    }

    public void update(PostUpdateReqDto dto, MultipartFile image) {
        Post post = postRepository.findById(dto.getPostId())
            .orElseThrow(
                () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));

        // 작성자와 수정자가 동일한지 체크
        if (post.getUser().getId() != dto.getUserId()) {
            throw new UnauthorizedException(
                ErrorStatus.OTHERS_USER_INFO_NOT_ALLOWED_EXCEPTION.getMessage());
        }

        if (image.isEmpty()) {
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

    // TODO 삭제
    public Long save(PostSaveReqDto dto) {
        User writer = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new NotFoundException("게시물을 작성하려면 회원가입/로그인이 필요합니다."));

        Post post = Post.createPost(dto.getTitle(), dto.getPrice(), dto.getImageUrl(),
            dto.getDescription(), writer);
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public List<PostSimpleDto> findByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(post -> {
            PostSimpleDto postSimpleDto = new PostSimpleDto();
            postSimpleDto.setId(post.getId());
            postSimpleDto.setTitle(post.getTitle());
            postSimpleDto.setPrice(post.getPrice());
            postSimpleDto.setImageUrl(post.getImageUrl());

            return postSimpleDto;
        }).toList();
    }

    // TODO User Entity의 List<Post> 찾는거하고 Post Entity에서 찾는거 중에 어떤게 빠른지 검증 필요
    @Transactional(readOnly = true)
    public PageResDto<PostSimpleDto> findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> postPage = postRepository.findByUserId(userId, pageable);
        Page<PostSimpleDto> postSimpleDto = postPage.map(PostSimpleDto::fromPost);
        return new PageResDto<>(postSimpleDto);
    }
}

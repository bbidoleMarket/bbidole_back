package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSaveReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSimpleDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostUpdateReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    // TODO 삭제
    @Test
    public void 게시물_저장_및_조회_테스트() throws Exception {
        // given
        SigninDto userDto = signinOne();
        Long userId = userService.signin(userDto);

        PostSaveReqDto postDto = createPostOne(userId);

        // when
        Long savedId = postService.save(postDto);
        PostDetailResDto post = postService.findById(savedId);

        // then
        Assertions.assertEquals(post.getTitle(), postDto.getTitle());
        Assertions.assertEquals(post.getPrice(), postDto.getPrice());
        Assertions.assertEquals(post.getImageUrl(), postDto.getImageUrl());
        Assertions.assertEquals(post.getDescription(), postDto.getDescription());
        Assertions.assertEquals(post.getWriterId(), postDto.getUserId());
    }

    @Test
    public void 게시물_수정_테스트() throws Exception {
        // given
        SigninDto userDto = signinOne();
        Long userId = userService.signin(userDto);

        PostSaveReqDto originPost = createPostOne(userId);
        Long postId = postService.save(originPost);

        PostUpdateReqDto postUpdateReqDto = new PostUpdateReqDto();
        postUpdateReqDto.setPostId(postId);
        postUpdateReqDto.setUserId(userId);

        postUpdateReqDto.setTitle("updatedTitle");
        postUpdateReqDto.setPrice(2000);
        postUpdateReqDto.setImageUrl("updatedImageUrl");
        postUpdateReqDto.setDescription("updatedDescription");

        // when
        postService.update(postUpdateReqDto);

        // then
        PostDetailResDto res = postService.findById(postUpdateReqDto.getPostId());
        Assertions.assertEquals(res.getTitle(), postUpdateReqDto.getTitle());
        Assertions.assertEquals(res.getPrice(), postUpdateReqDto.getPrice());
        Assertions.assertEquals(res.getImageUrl(), postUpdateReqDto.getImageUrl());
        Assertions.assertEquals(res.getDescription(), postUpdateReqDto.getDescription());
        Assertions.assertEquals(res.getWriterId(), postUpdateReqDto.getUserId());

    }

    @Test
    public void 게시물_사용자_아이디_조회() throws Exception {
        // given
        SigninDto userDto = signinOne();
        Long userId = userService.signin(userDto);

        int postNum = 10;
        List<PostSaveReqDto> savedPosts = new ArrayList<>(createPostMany(userId, postNum));
        for (PostSaveReqDto post : savedPosts) {
            postService.save(post);
        }

        // when
        List<PostSimpleDto> posts = new ArrayList<>(postService.findByUserId(userId));

        // post의 순서가 달라질 수 있으므로 id로 정렬 후 비교
        // 단순 List의 경우 Immutable 이기 때문에 ArrayList로 변환필요
        savedPosts.sort(Comparator.comparing(PostSaveReqDto::getUserId));
        posts.sort(Comparator.comparing(PostSimpleDto::getId));

        // then
        Assertions.assertEquals(postNum, posts.size());
        for (int i = 0; i < postNum; i++) {
            Assertions.assertEquals(savedPosts.get(i).getTitle(), posts.get(i).getTitle());
            Assertions.assertEquals(savedPosts.get(i).getPrice(), posts.get(i).getPrice());
            Assertions.assertEquals(savedPosts.get(i).getImageUrl(), posts.get(i).getImageUrl());
        }
    }

    private SigninDto signinOne() {
        String email = "b@b.b";
        String password = "bbb!";
        String name = "bbb";

        SigninDto signinDto = new SigninDto();
        signinDto.setEmail(email);
        signinDto.setPassword(password);
        signinDto.setName(name);

        return signinDto;
    }

    private List<SigninDto> signinMany(int count) {
        List<SigninDto> signinDtos = new ArrayList<>();

        if (count == 1) {
            signinDtos.add(signinOne());
            return signinDtos;
        }

        String email = "b@b.b";
        String password = "bbb!";
        String name = "bbb";

        for (int i = 0; i < count; i++) {
            SigninDto signinDto = new SigninDto();
            signinDto.setEmail(email + i);
            signinDto.setPassword(password + i);
            signinDto.setName(name + i);

            signinDtos.add(signinDto);
        }
        return signinDtos;
    }

    private PostSaveReqDto createPostOne(long userId) {
        PostSaveReqDto postSaveReqDto = new PostSaveReqDto();
        postSaveReqDto.setTitle("title");
        postSaveReqDto.setPrice(1000);
        postSaveReqDto.setImageUrl("imageUrl");
        postSaveReqDto.setDescription("description");
        postSaveReqDto.setUserId(userId);
        return postSaveReqDto;
    }

    private List<PostSaveReqDto> createPostMany(long userId, int count) {
        List<PostSaveReqDto> postSaveReqDtos = new ArrayList<>();

        if (count == 1) {
            postSaveReqDtos.add(createPostOne(userId));
            return postSaveReqDtos;
        }

        String title = "title";
        int price = 1000;
        String imageUrl = "imageUrl";
        String description = "description";

        for (int i = 0; i < count; i++) {
            PostSaveReqDto postSaveReqDto = new PostSaveReqDto();
            postSaveReqDto.setTitle(title + i);
            postSaveReqDto.setPrice(price + i);
            postSaveReqDto.setImageUrl(imageUrl + i);
            postSaveReqDto.setDescription(description + i);
            postSaveReqDto.setUserId(userId);

            postSaveReqDtos.add(postSaveReqDto);
        }

        return postSaveReqDtos;
    }

}
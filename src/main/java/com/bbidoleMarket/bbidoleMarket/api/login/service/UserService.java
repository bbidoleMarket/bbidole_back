package com.bbidoleMarket.bbidoleMarket.api.login.service;

import com.bbidoleMarket.bbidoleMarket.api.login.dto.LoginReqDto;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.login.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.api.signup.dto.SignupReqDto;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.exception.BaseException;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(LoginReqDto loginReqDto) {
        User user = userRepository.findByEmail(loginReqDto.getEmail())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Transactional
    public User signup(SignupReqDto signupReqDto) {
        if (userRepository.existsByEmail(signupReqDto.getEmail())) {
            throw new BaseException(CONFLICT, "이미 사용 중인 이메일입니다.");
        }

        User user = User.createUser(
                signupReqDto.getName(),
                signupReqDto.getEmail(),
                signupReqDto.getPassword(),
                signupReqDto.getNickname(),
                passwordEncoder
        );

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}

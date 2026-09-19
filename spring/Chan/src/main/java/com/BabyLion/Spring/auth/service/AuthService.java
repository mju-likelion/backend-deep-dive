package com.BabyLion.Spring.auth.service;

import com.BabyLion.Spring.auth.dto.LoginRequest;
import com.BabyLion.Spring.auth.dto.SignupRequest;
import com.BabyLion.Spring.auth.jwt.JwtProvider;
import com.BabyLion.Spring.global.exception.*;
import com.BabyLion.Spring.member.domain.Member;
import com.BabyLion.Spring.member.domain.RoleType;
import com.BabyLion.Spring.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public Member signup(SignupRequest dto){

        if (memberRepository.existsByLoginId(dto.getLoginId())) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_LOGIN_ID);
        }
        String rawPassword = dto.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);

        Member member = new Member(
                dto.getName(),
                dto.getMajor(),
                dto.getPart(),
                dto.getGeneration(),
                RoleType.LION,
                dto.getStudentId(),
                null, encPassword, dto.getLoginId());

        return memberRepository.save(member);
    }

    public String login(LoginRequest dto) {
        Member member = memberRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.LOGIN_FAILED));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.LOGIN_FAILED);
        }

        return jwtProvider.createToken(member.getId());
    }
}

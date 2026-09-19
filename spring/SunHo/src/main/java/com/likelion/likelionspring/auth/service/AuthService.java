package com.likelion.likelionspring.auth.service;

import com.likelion.likelionspring.auth.dto.LoginRequest;
import com.likelion.likelionspring.auth.dto.SignupRequest;
import com.likelion.likelionspring.auth.dto.TokenResponse;
import com.likelion.likelionspring.member.domain.Member;
import com.likelion.likelionspring.member.domain.RoleType;
import com.likelion.likelionspring.member.dto.MemberResponse;
import com.likelion.likelionspring.global.exception.DuplicateMemberNameException;
import com.likelion.likelionspring.global.exception.InvalidMemberRequestException;
import com.likelion.likelionspring.global.exception.LoginFailedException;
import com.likelion.likelionspring.global.security.JwtProvider;
import com.likelion.likelionspring.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public MemberResponse signup(SignupRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new InvalidMemberRequestException("이름은 빈 문자열일 수 없습니다.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidMemberRequestException("비밀번호는 빈 문자열일 수 없습니다.");
        }
        if (request.getGeneration() <= 0) {
            throw new InvalidMemberRequestException("기수는 1 이상이어야 합니다.");
        }
        if (memberRepository.findByName(request.getName()) != null) {
            throw new DuplicateMemberNameException("이미 존재하는 멤버 이름입니다: " + request.getName());
        }

        RoleType roleType = parseRoleType(request.getRoleType());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = new Member(
                request.getName(), encodedPassword, request.getMajor(), request.getGeneration(),
                request.getPart(), roleType, request.getStudentId(), request.getPosition()
        );
        return MemberResponse.from(memberRepository.save(member));
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByName(request.getName());
        if (member == null || member.getPassword() == null
                || !passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new LoginFailedException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateToken(member);
        return new TokenResponse(accessToken);
    }

    private RoleType parseRoleType(String roleType) {
        try {
            return RoleType.valueOf(roleType);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidMemberRequestException("roleType은 LION 또는 STAFF여야 합니다.");
        }
    }
}
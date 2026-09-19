package com.likelion.pbl.week11.auth.service;

import com.likelion.pbl.week11.auth.dto.LoginRequest;
import com.likelion.pbl.week11.auth.dto.SignupRequest;
import com.likelion.pbl.week11.auth.dto.TokenResponse;
import com.likelion.pbl.week11.domain.Member;
import com.likelion.pbl.week11.domain.RoleType;
import com.likelion.pbl.week11.dto.MemberResponse;
import com.likelion.pbl.week11.global.exception.AuthenticationFailedException;
import com.likelion.pbl.week11.global.exception.DuplicateMemberException;
import com.likelion.pbl.week11.global.security.JwtProvider;
import com.likelion.pbl.week11.repository.MemberRepository;
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
        if (memberRepository.existsByName(request.getName())) {
            throw new DuplicateMemberException("Duplicate member name: " + request.getName());
        }

        RoleType roleType = resolveRoleType(request);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = new Member(
                request.getName(),
                request.getMajor(),
                request.getGeneration(),
                request.getPart(),
                roleType,
                roleType == RoleType.LION ? request.getStudentId() : null,
                roleType == RoleType.STAFF ? request.getPosition() : null,
                encodedPassword
        );

        return MemberResponse.from(memberRepository.save(member));
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByName(request.getName())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid name or password"));

        if (member.getPassword() == null || !passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthenticationFailedException("Invalid name or password");
        }

        return new TokenResponse(jwtProvider.createToken(member.getId(), member.getName()));
    }

    private RoleType resolveRoleType(SignupRequest request) {
        String roleName = request.getRoleName();

        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("roleName은 필수입니다.");
        }

        try {
            return RoleType.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("roleName은 LION 또는 STAFF만 가능합니다.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

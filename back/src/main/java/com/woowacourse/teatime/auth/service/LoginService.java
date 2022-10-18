package com.woowacourse.teatime.auth.service;


import static com.woowacourse.teatime.teatime.domain.Role.COACH;
import static com.woowacourse.teatime.teatime.domain.Role.CREW;

import com.woowacourse.teatime.auth.controller.dto.LoginRequest;
import com.woowacourse.teatime.auth.controller.dto.UserAuthDto;
import com.woowacourse.teatime.auth.domain.UserAuthInfo;
import com.woowacourse.teatime.auth.infrastructure.JwtTokenProvider;
import com.woowacourse.teatime.teatime.controller.dto.request.SheetQuestionUpdateRequest;
import com.woowacourse.teatime.teatime.domain.Coach;
import com.woowacourse.teatime.teatime.domain.Crew;
import com.woowacourse.teatime.teatime.repository.CoachRepository;
import com.woowacourse.teatime.teatime.repository.CrewRepository;
import com.woowacourse.teatime.teatime.service.QuestionService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LoginService {

    private static final String DEFAULT_IMAGE = "https://avatars.slack-edge.com/2022-09-16/4091280804290_59b52a871e8a942a7969_192.png";
    private static final String DEFAULT_SUFFIX_EMAIL = "@email.com";
    private static final String DEFAULT_QUESTION_1 = "이번 면담을 통해 논의하고 싶은 내용";
    private static final String DEFAULT_QUESTION_2 = "최근에 자신이 긍정적으로 보는 시도와 변화";
    private static final String DEFAULT_QUESTION_3 = "이번 면담을 통해 생기기를 원하는 변화";

    private final CrewRepository crewRepository;
    private final CoachRepository coachRepository;
    private final QuestionService questionService;
    private final UserAuthService userAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserAuthDto login(LoginRequest loginRequest) {
        String name = loginRequest.getName();
        String role = loginRequest.getRole();
        if (role.equals("COACH")) {
            return getCoachLoginResponse(name);
        }
        return getCrewLoginResponse(name);
    }

    private UserAuthDto getCoachLoginResponse(String name) {
        Coach coach = coachRepository.findByName(name)
                .orElseGet(() -> saveCoachAndDefaultQuestions(name));

        Map<String, Object> claims = Map.of("id", coach.getId(), "role", COACH);
        String accessToken = jwtTokenProvider.createToken(claims);
        String refreshToken = UUID.randomUUID().toString();
        userAuthService.save(new UserAuthInfo(refreshToken, accessToken, coach.getId(), COACH.name()));
        return new UserAuthDto(accessToken, refreshToken, COACH, coach.getImage(), coach.getName());
    }

    @NotNull
    private Coach saveCoachAndDefaultQuestions(String name) {
        Coach coach = coachRepository.save(new Coach(
                "UXXX01B38BC",
                name,
                name + DEFAULT_SUFFIX_EMAIL,
                DEFAULT_IMAGE));

        List<SheetQuestionUpdateRequest> defaultQuestionDtos = List.of(
                new SheetQuestionUpdateRequest(1, DEFAULT_QUESTION_1, true),
                new SheetQuestionUpdateRequest(2, DEFAULT_QUESTION_2, true),
                new SheetQuestionUpdateRequest(3, DEFAULT_QUESTION_3, true));

        questionService.update(coach.getId(), defaultQuestionDtos);
        return coach;
    }

    private UserAuthDto getCrewLoginResponse(String name) {
        Crew crew = crewRepository.findByName(name)
                .orElseGet(() -> saveCrew(name));

        Map<String, Object> claims = Map.of("id", crew.getId(), "role", CREW);
        String accessToken = jwtTokenProvider.createToken(claims);
        String refreshToken = UUID.randomUUID().toString();
        userAuthService.save(new UserAuthInfo(refreshToken, accessToken, crew.getId(), CREW.name()));
        return new UserAuthDto(accessToken, refreshToken, CREW, crew.getImage(), crew.getName());
    }

    @NotNull
    private Crew saveCrew(String name) {
        Crew crew = new Crew(
                "UXXX01B38BC",
                name,
                name + DEFAULT_SUFFIX_EMAIL,
                DEFAULT_IMAGE);
        return crewRepository.save(crew);
    }
}

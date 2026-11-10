package vcn.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import vcn.model.UserDTO;
import vcn.service.UserInfoService;

@RestController
public class UserInfo {

	private final UserInfoService userInfoService;

    public UserInfo(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/me")
	public Mono<UserDTO> userInfo(@AuthenticationPrincipal OidcUser principal) {
		return userInfoService.getUserInfo(principal);
	}
}

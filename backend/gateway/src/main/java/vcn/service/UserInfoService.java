package vcn.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import reactor.core.publisher.Mono;
import vcn.model.UserDTO;

public interface UserInfoService {

    Mono<UserDTO> getUserInfo(OidcUser principal);
}

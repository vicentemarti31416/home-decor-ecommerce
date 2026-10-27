package vcn.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vcn.model.UserDTO;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Override
    public Mono<UserDTO> getUserInfo(OidcUser principal) {
        if (principal != null) {
            List<String> list = principal.getIdToken().getClaimAsStringList("authorities");
            String authorities = String.join(",", list);
            return Mono.just(
                    new UserDTO(
                            principal.getIdToken().getSubject(),
                            principal.getIdToken().getClaim("details"),
                            authorities));
        }
        return Mono.just(UserDTO.ANONYMOUS);
    }
}

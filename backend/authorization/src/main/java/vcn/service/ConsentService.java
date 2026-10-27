package vcn.service;

import org.springframework.ui.Model;

import java.security.Principal;

public interface ConsentService {

    String consentHandler(String clientId, Principal principal, Model model, String scope, String state);
}

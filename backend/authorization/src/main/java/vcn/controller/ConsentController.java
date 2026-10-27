package vcn.controller;

import java.security.Principal;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vcn.service.ConsentService;

@Controller
public class ConsentController {

	private final ConsentService consentService;

    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping(value = "/oauth2/consent")
	public String consent(
			Principal principal,
			Model model,
			@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
			@RequestParam(OAuth2ParameterNames.STATE) String state) {
	
		return consentService.consentHandler(clientId, principal, model, scope, state);
	}
}

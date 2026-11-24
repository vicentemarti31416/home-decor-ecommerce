package vcn.config;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

	private final ReactiveClientRegistrationRepository repository;
	
	public SecurityConfig(ReactiveClientRegistrationRepository repository) {
		this.repository = repository;
	}

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
				.authorizeExchange(exchange -> exchange
						.pathMatchers(HttpMethod.POST, "/api/payments/webhook").permitAll()
						.pathMatchers(HttpMethod.POST, "/api/users/register").permitAll()
						.pathMatchers("/api/orders/**", "/api/payments/**").authenticated()
						.pathMatchers("/api/users/me", "/api/users/shippmentDetails").authenticated()
						.anyExchange().permitAll())
				.exceptionHandling(spec -> spec
						.authenticationEntryPoint(
								new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
				.csrf(csrf -> csrf
						.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
						.csrfTokenRequestHandler(new ServerCsrfTokenRequestAttributeHandler())
						.requireCsrfProtectionMatcher(new AndServerWebExchangeMatcher(
								CsrfWebFilter.DEFAULT_CSRF_MATCHER,
								new NegatedServerWebExchangeMatcher(
										ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/payments/webhook")))))
				.oauth2Login(withDefaults())
				.oauth2Client(withDefaults())
				.logout(logout -> logout
						.logoutSuccessHandler(logoutSuccessHandler(repository)))
				.build();
	}
	
	@Bean
	ServerLogoutSuccessHandler logoutSuccessHandler(ReactiveClientRegistrationRepository repository) {
	       OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(repository);
		   System.out.println(">>>>>> BEAN logoutSuccessHandler CREADO <<<<<<");
		   oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://127.0.0.1:8090/logged-out");
	       return oidcLogoutSuccessHandler;
	}

	/**
	 * Define cómo WebFlux transporta el ID de sesión entre servidor y navegador:
	 * en qué cookie lo escribe al crear la sesión,
	 * con qué atributos, y de dónde lo lee en cada petición para recuperar la sesión
	 * (y con ella el token OAuth2 que usa TokenRelay).
	 * Falta añadir el atributo secure para la cookie en producción
	 */
	@Bean
	WebSessionIdResolver webSessionIdResolver() {
		var resolver = new CookieWebSessionIdResolver();
		resolver.addCookieInitializer(c -> c
				.path("/") // Para que la cookie se envíe en todas las rutas del gateway
				.httpOnly(true) // Es para que no pueda leerse con javascript y evitar XSS
				/**
				 * Strict rompe el login porque la redirección viene de localhost, que es otro sitio respecto a 127.0.0.1.
				 * Con Strict el navegador no envía SESSION en esa petición,
				 * el Gateway no encuentra la petición de autorización que guardó en la sesión y rechaza el state.
				 * Lax es suficiente siempre que no se modifique el estado desde una petición GET, que es la única
				 * cross-site que viene con la cookie de sesión
				 */
				.sameSite("Lax")); // Evita que pueda usarse desde otra pestaña del navegador y para que la corte CSRF
		return resolver;
	}

	/**
	 * El token CSRF en WebFlux es un Mono: si nadie se suscribe, la cookie XSRF-TOKEN
	 * nunca se escribe y Angular no tiene nada que enviar.
	 */
	@Bean
	WebFilter csrfCookieFilter() {
		return (exchange, chain) -> {
			Mono<CsrfToken> token = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
			return token.then(chain.filter(exchange));
		};
	}
}

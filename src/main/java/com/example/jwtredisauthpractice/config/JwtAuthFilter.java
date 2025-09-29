package com.example.jwtredisauthpractice.config;

public class JwtAuthFilter extends org.springframework.web.filter.OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    public JwtAuthFilter(TokenProvider tp){ this.tokenProvider = tp; }

    @Override protected void doFilterInternal(
            jakarta.servlet.http.HttpServletRequest req,
            jakarta.servlet.http.HttpServletResponse res,
            jakarta.servlet.FilterChain chain) throws jakarta.servlet.ServletException, java.io.IOException {

        String auth = req.getHeader("Authorization");
        if (org.springframework.util.StringUtils.hasText(auth) && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (tokenProvider.validToken(token)) {
                Long uid = tokenProvider.getUserId(token);
                var authn = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        uid, null, java.util.Collections.emptyList());
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authn);
            }
        }
        chain.doFilter(req, res);
    }
}
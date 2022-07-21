package at.ac.ase.config;

import at.ac.ase.interceptor.TokenInterceptor;
import at.ac.ase.util.TokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class  InterceptConfig implements WebMvcConfigurer {

    private TokenUtil tokenUtil;

    public InterceptConfig(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
            new TokenInterceptor(tokenUtil)).excludePathPatterns(
                "/registerUser",
                "/registerHouse",
                "/login",
                "/requestPasswordReset",
                "/sendResetPasswordToken",
                "/resetPassword",
                "/error",
                "/api/auctions/upcoming",
                "/api/auctions/recent",
                "/api/auctions",
                "/api/auctions/getCategories",
                "/api/auctions/countriesWhereAuctionsExist");
    }
}

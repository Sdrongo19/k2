package SecurityHeader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro, se necessaria
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            // Aggiungi Content-Security-Policy
            httpServletResponse.setHeader("Content-Security-Policy", "frame-ancestors 'none'");

            // Aggiungi X-Frame-Options
            httpServletResponse.setHeader("X-Frame-Options", "DENY");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Pulizia del filtro, se necessaria
    }
}

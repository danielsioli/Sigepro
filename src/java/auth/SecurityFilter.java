/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import entities.Usuario;
import java.io.IOException;
import javax.security.auth.Subject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Daniel
 */
public class SecurityFilter implements Filter {

    private final static String FILTER_APPLIED = "_security_filter_applied";

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Subject subject = (Subject) session.getAttribute("javax.security.auth.subject");
        String user = null;
        if (subject == null) {
            subject = new Subject();
        }

        session.setAttribute("javax.security.auth.subject", subject);

        String requestedPage = req.getServletPath();

        if ((!requestedPage.endsWith("login.jsp")) && (!requestedPage.endsWith("login-error.jsp")) && (!requestedPage.endsWith("login.view"))) {
            if (req.getRemoteUser() != null) {
                user = req.getRemoteUser();
            } else if (subject.getPrincipals(Usuario.class) != null && subject.getPrincipals(Usuario.class).size() == 1) {
                Usuario usuario = (Usuario) subject.getPrincipals(Usuario.class).iterator().next();
                if (!usuario.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
                    if (!requestedPage.contains("incluir/usuario.jsp") && !requestedPage.contains("excluir/usuario.jsp")) {
                        user = usuario.getLogin();
                        session.setAttribute("username", user);
                    }
                } else {
                    user = usuario.getLogin();
                    session.setAttribute("username", user);
                }
            }

            if ((user == null) || (user.equals(""))) {
                res.sendRedirect("https://" + req.getServerName() + ":8181" + req.getContextPath() + "/login.jsp");
                return;
            }
        }
        //deliver request to next filter
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
    }
}

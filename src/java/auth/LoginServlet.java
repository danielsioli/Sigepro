/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.io.*;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Daniel
 */
public class LoginServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Subject subject = (Subject) session.getAttribute("javax.security.auth.subject");
        if (subject == null) {
            subject = new Subject();
        }
        session.setAttribute("javax.security.auth.subject", subject);

        String method = request.getParameter("method");
        if (method != null && method.equals("login")) {
            login(request, response, subject);
        } else {
            logout(request, response, subject);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response, Subject subject) {
        LoginContext loginContext = null;
        try {
            try {
                loginContext = new LoginContext("MyJdbcLoginModule", subject, new HttpAuthCallbackHandler(request));
            } catch (Exception le) {
                le.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/login-error.jsp");
                return;
            }

            try {
                loginContext.login();
                request.getSession().setAttribute("username", request.getParameter("username"));
                response.sendRedirect("http://" + request.getServerName() + ":8080" + request.getContextPath());
            // if we return with no exception, authentication succeeded
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/login-error.jsp");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response, Subject subject) {
        try {
            try {
                request.getSession().setAttribute("username", null);
                subject.getPrincipals().clear();
                response.sendRedirect(request.getContextPath());
            // if we return with no exception, authentication succeeded
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/login-error.jsp");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Servlet para login e logout";
    }
    // </editor-fold>
}

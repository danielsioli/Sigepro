/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Daniel
 */
public class HttpAuthCallbackHandler implements CallbackHandler {

    String username = null;
    String password = null;

    public HttpAuthCallbackHandler(HttpServletRequest request) {
        username = request.getParameter("j_username");
        password = request.getParameter("j_password");
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                ((NameCallback) callbacks[i]).setName(username);
            } else if (callbacks[i] instanceof PasswordCallback) {
                ((PasswordCallback) callbacks[i]).setPassword(password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "HttpAuthCallbackHandler:Unrecognized Callback");
            }
        }
    }
}

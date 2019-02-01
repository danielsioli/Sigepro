<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page info="Sistema de Gerenciamento de Processos" %>

<%
            String usernameCookieName = "sigepro1";
            String passwordCookieName = "sigepro2";
            Cookie cookies[] = request.getCookies();
            Cookie usernameCookie = null;
            Cookie passwordCookie = null;
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals(usernameCookieName)) {
                        usernameCookie = cookies[i];
                    } else if (cookies[i].getName().equals(passwordCookieName)) {
                        passwordCookie = cookies[i];
                    }
                }
            }
            if ((usernameCookie != null && usernameCookie.getValue().length() > 0) && (passwordCookie != null && passwordCookie.getValue().length() > 0)) {
                response.sendRedirect("https://" + request.getServerName() + ":8181" + request.getContextPath() + "/login.view?method=login&j_username=" + usernameCookie.getValue() + "&j_password=" + passwordCookie.getValue());
            }
%>

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=getServletInfo()%></title>
        <style type="text/css">@import "<%=request.getContextPath()%>/css/all.css";</style>
        <%
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
        %>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ie6.css" media="screen"/>
        <%
            }
        %>
        <style type="text/css">@import "<%=request.getContextPath()%>/css/site.css";</style>
        
        <script type="text/javascript">
            function saveCookies(){
                var cookies = document.getElementById("cookies");
                var expires = "";
                var date = new Date();
                if(cookies.checked){
                    date.setTime(date.getTime()+(14*24*60*60*1000));//válido por 2 semanas
                    expires = "; expires=" + date.toGMTString();
                }else{
                    date.setTime(date.getTime()+(-1*24*60*60*1000));//venceu ontem - isso é para apagar o cookie
                    expires = "; expires=" + date.toGMTString();
                }
                var username = document.getElementById("j_username");
                var password = document.getElementById("j_password");
                document.cookie = 'sigepro1=' + username.value + expires + '; path=/';
                document.cookie = 'sigepro2=' + password.value + expires + '; path=/';
            }
            
            function onLoad(){
                var username = document.getElementById("j_username");
                username.focus();
            }
        </script>
        
    </head>
    <body onload="onLoad()">
        <div id="header"><%=getServletInfo()%></div>
        <div id="subheader">
            <a href="http://sistemasnet/stel/" target="_top" style="background: url(./img/external.png) right center no-repeat; padding-right: 15px;">STEL</a>
            |
            <a href="http://sistemasnet/sigec" target="_top" style="background: url(./img/external.png) right center no-repeat; padding-right: 15px;">SIGEC</a>
            |
            <a href="http://sistemasnet/sicap" target="_top" style="background: url(./img/external.png) right center no-repeat; padding-right: 15px;">SICAP</a>
            |
            <a href="http://sistemasnet/sard" target="_top" style="background: url(./img/external.png) right center no-repeat; padding-right: 15px;">SARD</a>
            |
            <a href="http://sistemasnet/sarh" target="_top" style="background: url(./img/external.png) right center no-repeat; padding-right: 15px;">SARH</a>
            |
            <a href="http://www.gmail.com" target="_top" style="background: url(./img/external.png) right center no-repeat; padding-right: 15px;">Gmail</a>
        </div>
        <div id="buildVersion">Using Project Version 1.0 Beta</div>
        <div class="wrapper">
            <div class="nav-wrapper">
                <div class="nav-left"></div>
                <div class="nav">
                    <ul id="navigation">
                        <li class="last">
                            <a>
                                <span class="menu-left"></span>
                                <span class="menu-mid">Login</span>
                                <span class="menu-right"></span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="nav-right"></div>
            </div>
            <div id="tela" class="content">
                <h1 id="subtitle" name="subtitle">Entre com nome e senha</h1>
                <form method=post action="<%="https://" + request.getServerName() + ":8181" + request.getContextPath()%>/login.view" onsubmit="saveCookies()">
                    <table>
                        <td>
                            <button type="reset" id="limpar" name="limpar">Limpar</button>
                        </td>
                        <td>
                            <button type="submit" id="confirmar" name="confirmar" value="Ok">Ok</button>
                        </td>
                    </table>
                    <input type="text" id=method name=method value="login" style="display:none">
                    <label for="j_username">Nome:</label>
                    <input type="text"  name="j_username">
                    <label for="j_password">Senha:</label>
                    <input type="password"  name="j_password">
                    <br>
                    <input type="checkbox" id="cookies" name="cookies">Salvar as minhas<br />informações neste<br />computador.</input>
                    <br>
                    <table>
                        <td>
                            <button type="reset" id="limpar" name="limpar">Limpar</button>
                        </td>
                        <td>
                            <button type="submit" id="confirmar" name="confirmar" value="Ok">Ok</button>
                        </td>
                    </table>
                </form>
                <noscript>
                    <br><b>Seu browser não suporta JavaScript</b>
                </noscript>
            </div>
            <div class="content-bottom"></div>
        </div>
    </body>
</html>

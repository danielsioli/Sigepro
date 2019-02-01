<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page info="Sistema de Gerenciamento de Processos" %>

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
            function eraseCookie(){
                var expires = "";
                var date = new Date();
                date.setTime(date.getTime()+(-1*24*60*60*1000));//venceu ontem - isso é para apagar o cookie
                expires = "; expires=" + date.toGMTString();
                document.cookie = 'sigepro1=foo' + expires + '; path=/';
                document.cookie = 'sigepro2=bar' + expires + '; path=/';
                window.location='index.jsp';
            }
        </script>
        
    </head>
    <body>
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
                                <span class="menu-mid">Erro no Login</span>
                                <span class="menu-right"></span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="nav-right"></div>
            </div>
            <div id="tela" class="content">
                <h1 id="subtitle" name="subtitle">Nome ou senha inválido.</h1>
                <p><a href="javascript://nop/" onclick="eraseCookie()">Tente novamente</a></p>
                <noscript>
                    <br><b>Seu browser não suporta JavaScript</b>
                </noscript>
            </div>
            <div class="content-bottom"></div>
        </div>
    </body>
</html>

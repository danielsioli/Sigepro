<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page info="Sistema de Gerenciamento de Processos" %>
<%@page isErrorPage="true" %>

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
        <div id="buildVersion">Using Project Revision <%="$Rev: 73 $".split(" ")[1]%></div>
        <div class="wrapper">
            <div class="nav-wrapper">
                <div class="nav-left"></div>
                <div class="nav">
                    <ul id="navigation">
                        <li class="last">
                            <a>
                                <span class="menu-left"></span>
                                <span class="menu-mid">Erro</span>
                                <span class="menu-right"></span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="nav-right"></div>
            </div>
            <div id="tela" class="content">
                <h1 id="subtitle" name="subtitle">Erro no Servidor</h1>
                Favor informar o erro ao Administrador.<br>
                <%
                StackTraceElement[] stackTraceElements = exception.getStackTrace();
                out.write(exception.getLocalizedMessage());
                for(int i = 0; i < stackTraceElements.length; i++){
                    out.write(stackTraceElements[i].toString() + "<br>");
                }
                %>
                <noscript>
                    <br><b>Seu browser não suporta JavaScript</b>
                </noscript>
            </div>
            <div class="content-bottom"></div>
        </div>
    </body>
</html>
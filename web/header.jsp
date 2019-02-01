<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page info="Sistema de Gerenciamento de Processos" language="java" contentType="text/html" pageEncoding="UTF-8" session="true" autoFlush="true"%>

<%@ taglib uri="http://ajaxtags.org/tags/ajax" prefix="ajax" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="dao.DAO" %>
<%@ page import="entities.Usuario" %>


<jsp:useBean id="usuario" scope="request" class="entities.Usuario"/>
<jsp:useBean id="hashtable" scope="request" class="java.util.Hashtable"/>

<%
            usuario = DAO.getInstance().find(Usuario.class, "" + request.getSession().getAttribute("username"));
            if (usuario == null) {
                usuario = new Usuario("" + request.getSession().getAttribute("username"));
            }
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=getServletInfo()%></title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/scriptaculous/scriptaculous.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_crossframe.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_iframe.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_hide.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_shadow.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajax/ajaxtags.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajax/ajaxtags_controls.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajax/ajaxtags_parser.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/transaction.js"></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/painel/documentos/jscripts/tiny_mce/tiny_mce.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/painel/documentos.js"></script>
    
    <script type="text/javascript">
        function initProgress(){
            var inputs = document.frmacao.getElementsByTagName("input");
            var selects = document.frmacao.getElementsByTagName("select");
            for(var i = 0; i < inputs.length; i++){
                if(inputs[i].attributes["key"].value == "PRI"){
                    Element.addClassName(inputs[i].name, 'progressMeterLoading');
                }else if(inputs[i].attributes["nullable"].value != "STATIC"){
                    inputs[i].value = "";
                }
            }
            for(var i = 0; i < selects.length; i++){
                if(selects[i].attributes["key"].value == "PRI"){
                    Element.addClassName(selects[i].name, 'progressMeterLoading');
                }else if(selects[i].attributes["nullable"].value != "STATIC"){
                    selects[i].value = "";
                }
            }
        }
    
        function resetProgress(){
            var ok = true;
            var inputs = document.frmacao.getElementsByTagName("input");
            var selects = document.frmacao.getElementsByTagName("select");
            for(var i = 0; i < inputs.length; i++){
                if(inputs[i].attributes["key"].value == "PRI"){
                    Element.removeClassName(inputs[i].name, 'progressMeterLoading');
                }else if(inputs[i].attributes["nullable"].value == "false" && inputs[i].value == ""){
                    ok = false;
                    break;
                }
            }
            for(var i = 0; i < selects.length; i++){
                if(selects[i].attributes["key"].value == "PRI"){
                    Element.removeClassName(selects[i].name, 'progressMeterLoading');
                }else if(selects[i].attributes["nullable"].value == "false" && selects[i].value == ""){
                    ok = false;
                    break;
                }
            }
            if(ok){
                for(var i = 0; i < inputs.length; i++){
                    if(inputs[i].attributes["key"].value != "PRI"){
                        new Effect.Highlight(inputs[i].name);
                    }
                }
                for(var i = 0; i < selects.length; i++){
                    if(selects[i].attributes["key"].value != "PRI"){
                        new Effect.Highlight(selects[i].name);
                    }
                }
                showMessage("Dados carregados com sucesso!!!",3500,"success");
            }else{
                showMessage("Dados não encontrados!!!",3500,"failure");
            }
        }

        var timeout = null;

        function showWaitMessage(occurrency){
            if(!responseReceived){
                if(occurrency == 1){
                    showMessage("Carregando dados...",0,"alert");
                    timeout = window.setTimeout("showWaitMessage(2)",15000);
                }else if (occurrency == 2){
                    showMessage("Ainda carregando dados...",0,"alert");
                    timeout = window.setTimeout("showWaitMessage(3)",15000);
                }else{
                    showMessage("Tenha paciência...",0,"alert");
                    timeout = window.setTimeout("showWaitMessage(2)",15000);
                }
            }
        }
    
        function showMessage(msg,time,type){
            $('message').innerHTML = msg;
            if(type == "alert"){
                $('message').style.backgroundImage = "url(/sigepro/img/yellow.png)";
            }else if(type == "success"){
                $('message').style.backgroundImage = "url(/sigepro/img/blue.png)";
            }else{
                $('message').style.backgroundImage = "url(/sigepro/img/red.png)";
            }
            if($('message').style.display == "block"){
                if(timeout != null){
                    clearTimeout(timeout);
                }
            }else{
                $('message').style.display = "block";
            }
            if(time != 0){
                timeout = window.setTimeout("$('message').style.display = 'none'",time);
            }else{
                timeout = null;
            }
        }

        function reportError() {
            showMessage("Erro no código!!!");
        }

        function eraseCookie(){
            var expires = "";
            var date = new Date();
            date.setTime(date.getTime()+(-1*24*60*60*1000));//venceu ontem - isso é para apagar o cookie
            expires = "; expires=" + date.toGMTString();
            document.cookie = 'sigepro1=foo' + expires + '; path=/';
            document.cookie = 'sigepro2=bar' + expires + '; path=/';
            window.location='/sigepro/login.view?method=logout';
        }

    </script>
    
    <style type="text/css">
        .message{
            background: 0 100% no-repeat transparent;
            display: none;
            position: fixed;
            right:-1px;
            top:0px;
            padding-left: 10px;
            padding-right: 10px;
            padding-top: 1px;
            padding-bottom: 1px;
            text-align:center;
            color:#fff;
            max-width:400px;
        }
        .searchTable{
            display: none;
            position:absolute;
            top:center;
            right:center;
            padding-left: 10px;
            padding-right: 10px;
            padding-top: 1px;
            padding-bottom: 1px;
            text-align:center;
            color:#000;
            background-color:#86ED58;
            border-width:thick;
        }
        .searchTable table tr td{
            border-width:1px;
        }
        .searchTable a{
            color:#000;
        }
        .searchTable a:hover{
            color:brown;
        }
    </style>
    
    <style type="text/css">@import "<%=request.getContextPath()%>/css/all.css";</style>
    <%
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
    %>
    <link rel="stylesheet" type="text/css" href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/css/ie6.css" media="screen"/>
    <%
            }
    %>
    <style type="text/css">@import "<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/css/site.css";</style>
    <style type="text/css">@import "<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/css/ajaxtags.css";</style>
</head>

<body>
<div id="header"><%=getServletInfo()%></div>
<div id="subheader">
    <a href="http://sistemasnet/stel/" target="_top" style="background: url(/sigepro/img/external.png) right center no-repeat; padding-right: 15px;">STEL</a>
    |
    <a href="http://sistemasnet/sigec" target="_top" style="background: url(/sigepro/img/external.png) right center no-repeat; padding-right: 15px;">SIGEC</a>
    |
    <a href="http://sistemasnet/sicap" target="_top" style="background: url(/sigepro/img/external.png) right center no-repeat; padding-right: 15px;">SICAP</a>
    |
    <a href="http://sistemasnet/sard" target="_top" style="background: url(/sigepro/img/external.png) right center no-repeat; padding-right: 15px;">SARD</a>
    |
    <a href="http://sistemasnet/sarh" target="_top" style="background: url(/sigepro/img/external.png) right center no-repeat; padding-right: 15px;">SARH</a>
    |
    <a href="http://www.gmail.com" target="_top" style="background: url(/sigepro/img/external.png) right center no-repeat; padding-right: 15px;">Gmail</a>
    |
    <a href="javascript://nop/" target="_top" style="background: url(/sigepro/img/external.png) right center no-repeat; padding-right: 15px;" onclick="eraseCookie()">Sair</a>
</div>
<div id="buildVersion">Using Project Version 1.0 Beta</div>
<div class="wrapper">
<div class="nav-wrapper">
    <div class="nav-left"></div>
    <div class="nav">
        <ul id="navigation">
            <!-- Incluir -->
            <li class="<%=request.getServletPath().matches(".*incluir.*") ? "active" : ""%>">
                <a>
                    <span class="menu-left"></span>
                    <span class="menu-mid">Incluir</span>
                    <span class="menu-right"></span>
                </a>
                <div class="sub">
                    <ul>
                        <%
            if (usuario.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
                        %>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/usuario.jsp" class="contentLink" >Usuário</a>
                        </li>    
                        <%
            }
                        %>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/ausencia.jsp" class="contentLink" >Ausência</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/empresa.jsp" class="contentLink" >Empresa</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/servico.jsp" class="contentLink" >Serviço</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/processo.jsp" class="contentLink" >Processo</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/distribuicao.jsp" class="contentLink" >Distribuição</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/procurador.jsp" class="contentLink" >Procurador</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/incluir/procuracao.jsp" class="contentLink" >Procuração</a>
                        </li>
                    </ul>
                    <div class="btm-bg"></div>
                </div>
            </li>
            <!-- Excluir -->
            <li class="<%=request.getServletPath().matches(".*excluir.*") ? "active" : ""%>">
                <a>
                    <span class="menu-left"></span>
                    <span class="menu-mid">Excluir</span>
                    <span class="menu-right"></span>
                </a>
                <div class="sub">
                    <ul>
                        <%
            if (usuario.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
                        %>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/usuario.jsp" class="contentLink" >Usuário</a>
                        </li>    
                        <%
            }
                        %>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/ausencia.jsp" class="contentLink" >Ausência</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/empresa.jsp" class="contentLink" >Empresa</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/servico.jsp" class="contentLink" >Serviço</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/processo.jsp" class="contentLink" >Processo</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/distribuicao.jsp" class="contentLink" >Distribuição</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/procurador.jsp" class="contentLink" >Procurador</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/excluir/procuracao.jsp" class="contentLink" >Procuração</a>
                        </li>
                    </ul>
                    <div class="btm-bg"></div>
                </div>
            </li>
            <!-- Alterar -->
            <li class="<%=request.getServletPath().matches(".*alterar.*") ? "active" : ""%>">
                <a>
                    <span class="menu-left"></span>
                    <span class="menu-mid">Alterar</span>
                    <span class="menu-right"></span>
                </a>
                <div class="sub">
                    <ul>
                        <li>
                            <a href="<%="https://" + request.getServerName() + ":8181" + request.getContextPath()%>/alterar/usuario.jsp" class="contentLink" >Usuário</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/alterar/ausencia.jsp" class="contentLink" >Ausência</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/alterar/empresa.jsp" class="contentLink" >Empresa</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/alterar/servico.jsp" class="contentLink" >Serviço</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/alterar/processo.jsp" class="contentLink" >Processo</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/alterar/procurador.jsp" class="contentLink" >Procurador</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/alterar/procuracao.jsp" class="contentLink" >Procuração</a>
                        </li>
                    </ul>
                    <div class="btm-bg"></div>
                </div>
            </li>
            <!-- Consultar -->
            <li class="<%=request.getServletPath().matches(".*consultar.*") ? "active" : ""%>">
                <a>
                    <span class="menu-left"></span>
                    <span class="menu-mid">Consultar</span>
                    <span class="menu-right"></span>
                </a>
                <div class="sub">
                    <ul>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/usuario.jsp" class="contentLink" >Usuário</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/ausencia.jsp" class="contentLink" >Ausência</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/empresa.jsp" class="contentLink" >Empresa</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/servico.jsp" class="contentLink" >Serviço</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/processo.jsp" class="contentLink" >Processo</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/distribuicao.jsp" class="contentLink" >Distribuição</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/procurador.jsp" class="contentLink" >Procurador</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/procuracao.jsp" class="contentLink" >Procuração</a>
                        </li>
                        <li>
                            <a href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/consultar/historico.jsp" class="contentLink" >Histórico</a>
                        </li>
                    </ul>
                    <div class="btm-bg"></div>
                </div>
            </li>
            <!-- Paniel de Controle -->
            <li class="<%=request.getServletPath().matches(".*index\\.jsp.*") ? "active" : "last"%>">
                <a id="painel" style="cursor:pointer" name="painel" href="<%="http://" + request.getServerName() + ":8080" + request.getContextPath()%>/index.jsp" class="contentLink" >
                    <span class="menu-left"></span>
                    <span class="menu-mid">Painel de Controle</span>
                    <span class="menu-right"></span>
                </a>
            </li>
        </ul>
    </div>
    <div class="nav-right"></div>
</div>
<div id="tela" class="content">
<div id = "message" class="message"></div>
<div id="consultaTabela" class="searchTable"></div>
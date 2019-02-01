<%-- 
    Document   : ar
    Created on : 07/07/2008, 11:12:05
    Author     : danieloliveira
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://ajaxtags.org/tags/ajax" prefix="ajax" %>
<%@ page import="entities.Oficio" %>
<%@ page import="entities.Paragrafo" %>
<%@ page import="dao.DAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="entities.Usuario" %>
<%@ page import="entities.Procuracao" %>
<%@ page import="entities.Procurador" %>
<%@ page import="entities.Processo" %>
<%@ page import="javax.security.auth.Subject" %>
<jsp:useBean id="hashtable" scope="request" class="java.util.Hashtable"/>
<jsp:useBean id="processo" class="entities.Processo" scope="session"/>
<%
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);

            Usuario usuario = ((Usuario) ((Subject) session.getAttribute("javax.security.auth.subject")).getPrincipals().iterator().next());
%>
<br>
<form>
    <table>
        <td>
            <button type="button" id="gerarAr" name="gerarAr" onclick="addEstatistica('AR',''); return showDocument('/docs/ar/ar.jsp','<%=usuario.getLogin()%>','ar','gerar')">Gerar AR</button>
        </td>
        <td>
            <button type="button" id="gerarEtiqueta" name="gerarEtiqueta" onclick="addEstatistica('Etiqueta',''); return showDocument('/docs/etiqueta/etiqueta.jsp','<%=usuario.getLogin()%>','etiqueta','gerar')">Gerar Etiqueta</button>
        </td>
    </table>
    <label for="newSicap">Número do Processo:</label>
    <input type="text" id="newSicap" name="newSicap" size="22" maxlength="17" onblur="return isNumbersOnly(this);" nullable="false" key="PRI">
    <button type="button" id="adicionar" name="adicionar" onclick="return addOption('processos',document.getElementById('newSicap').value)">Adicionar</button>
    <span id="indicator_sicap" name="indicator_sicap" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    <label for="processos">Processos Selecionados:</label>
    <select id=processos name=processos size=5 width=22 multiple>
        <option value=''>Processos aqui!!!!</option>
    </select>
    <button type="button" id="excluir" name="excluir" onclick="return removeOption('processos')">Retirar Selecionados</button>
    <table>
        <td>
            <button type="button" id="gerarAr" name="gerarAr" onclick="addEstatistica('AR',''); return showDocument('/docs/ar/ar.jsp','<%=usuario.getLogin()%>','ar','gerar')">Gerar AR</button>
        </td>
        <td>
            <button type="button" id="gerarEtiqueta" name="gerarEtiqueta" onclick="addEstatistica('Etiqueta',''); return showDocument('/docs/etiqueta/etiqueta.jsp','<%=usuario.getLogin()%>','etiqueta','gerar')">Gerar Etiqueta</button>
        </td>
    </table>
</form>

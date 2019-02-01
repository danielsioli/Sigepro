<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<h1>Incluir Serviço</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="incluir" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Servico" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Incluir');">Incluir Serviço</button>
        </td>
    </table>
    <label for="servicoNum">Número do Serviço:</label>
    <input type="text" id="servicoNum" name="servicoNum" size="8" maxlength="3" onblur="return isNumbersOnly(this);" nullable="false" key="PRI">
    <button type="button" id="action" name="action" style="display:none;">Buscar Dados</button>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    <span id="indicator" name="indicator" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="nome">Nome do Serviço:</label>
    <input type="text" id="nome" name="nome" size="75" maxlength="80" nullable="false" key="">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="login">Responsável:</label>
    <%
            hashtable.clear();
            List<Usuario> usuarios = DAO.getInstance().findByNamedQuery(Usuario.class, "Usuario.findAll", hashtable);

    %>
    <select id="login" name="login" nullable="true" key="">
        <option value='' <%=request.getParameter("login") == null ? "selected" : ""%>></option>
        <%
            for (int j = 0; j < usuarios.size(); j++) {
        %>
        <option value="<%=usuarios.get(j).getLogin()%>" <%=request.getParameter("login") != null && request.getParameter("login").equals(usuarios.get(j).getLogin()) ? "selected" : ""%>><%=usuarios.get(j).getLogin() + " - " + usuarios.get(j).getNome()%></option>
        <%
            }
        %>
    </select>
    
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Incluir');">Incluir Serviço</button>
        </td>
    </table>
</form>
<%@ include file="../footer.jsp" %>

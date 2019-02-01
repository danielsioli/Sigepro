<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import='entities.Servico' %>
<%@ include file="../header.jsp" %>
<h1>Excluir Serviço</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="excluir" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Servico" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Excluir');">Excluir Serviço</button>
        </td>
    </table>
    <label for="servicoNum">Serviço:</label>
    <%
            hashtable.clear();
            List<Servico> servicos = DAO.getInstance().findByNamedQuery(Usuario.class, "Servico.findAll", hashtable);

    %>
    <select id="servicoNum" name="servicoNum" nullable="false" key="PRI"  value="<%=(request.getParameter("servicoNum") != null ? request.getParameter("servicoNum") : "")%>">
        <option value='' <%=request.getParameter("servicoNum") == null ? "selected" : ""%>></option>
        <%
            for (int j = 0; j < servicos.size(); j++) {
        %>
        <option value="<%=servicos.get(j).getServicoNum()%>" <%=request.getParameter("servicoNum") != null && request.getParameter("servicoNum").equals(servicos.get(j).getServicoNum()) ? "selected" : ""%>><%=servicos.get(j).getServicoNum() + " - " + servicos.get(j).getNome()%></option>
        <%
            }
        %>
    </select>
    <button type="button" id="actionNum" name="actionNum">Buscar Dados</button>
    <span id="indicator_servicoNum" name="indicator_servicoNum" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="nome">Nome do Serviço:</label>
    <input type="text" id="nome" name="nome" size="75" maxlength="80" nullable="false" key="" disabled=disabled>
    
    <label for="login">Responsável:</label>
    <%
            hashtable.clear();
            List<Usuario> usuarios = DAO.getInstance().findByNamedQuery(Usuario.class, "Usuario.findAll", hashtable);

    %>
    <select id="login" name="login" nullable="true" key="" disabled=disabled>
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
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Excluir');">Excluir Serviço</button>
        </td>
    </table>
</form>

<ajax:updateField
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="servicoNum"
    doPost="true"
    valueUpdateByName="true"
    target="nome,login"
    action="actionNum"
    parameters="method=updateField,tabela={tabela},servicoNum={servicoNum}"
    parser="new ResponseXmlParser()"
    preFunction="initProgress"
    postFunction="resetProgress"
    errorFunction="reportError"
    />
<%@ include file="../footer.jsp" %>

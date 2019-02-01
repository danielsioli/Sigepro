<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<h1>Alterar Usuario</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="alterar" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Usuario" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Alterar');">Alterar Usuário</button>
        </td>
    </table>
    <label for="login">Login:</label>
    <%
            hashtable.clear();
            List<Usuario> usuarios = DAO.getInstance().findByNamedQuery(Usuario.class, "Usuario.findAll", hashtable);

    %>
    <select id="login" name="login" nullable="false" key="PRI">
        <option value='' <%=request.getParameter("login") == null ? "selected" : ""%>></option>
        <%
            if (usuario.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
                for (int j = 0; j < usuarios.size(); j++) {
        %>
        <option value="<%=usuarios.get(j).getLogin()%>" <%=request.getParameter("login") != null && request.getParameter("login").equals(usuarios.get(j).getLogin()) ? "selected" : ""%>><%=usuarios.get(j).getLogin() + " - " + usuarios.get(j).getNome()%></option>
        <%
            }
        } else {
        %>
        <option value="<%=usuario.getLogin()%>" selected><%=usuario.getLogin() + " - " + usuario.getNome()%></option>
        <%
            }
        %>
    </select>
    <button type="button" id="actionLogin" name="actionLogin">Buscar Dados</button>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    <span id="indicator" name="indicator" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" size="50" maxlength="45" nullable="true" key="">
    
    <label for="cargo">Cargo:</label>
    <input type="text" id="cargo" name="cargo" size="65" maxlength="60" nullable="true" key="">
    
    <label for="tipoCargo">Tipo de Cargo:</label>
    <select id="tipoCargo" name="tipoCargo" nullable="true" key="">
        <%
            Usuario.TipoCargo[] tipoCargo = Usuario.TipoCargo.values();
            for (int j = 0; j < tipoCargo.length; j++) {
        %>
        <option value='<%=tipoCargo[j]%>'><%=Usuario.getTipoCargoName(tipoCargo[j])%></option>
        <%
            }
        %>
    </select>
    
    <label for="area">Área:</label>
    <select id="area" name="area" nullable="false" key="">
        <option value='' selected></option>
        <%
            Usuario.Area[] areas = Usuario.Area.values();
            for (int j = 0; j < areas.length; j++) {
        %>
        <option value='<%=areas[j]%>'><%=areas[j].toString()%></option>
        <%
            }
        %>
    </select>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="loginSubstituto">Substituto:</label>
    <select id="loginSubstituto" name="loginSubstituto" nullable="true" key="">
        <option value='' <%=request.getParameter("login") == null ? "selected" : ""%>></option>
        <%
            for (int j = 0; j < usuarios.size(); j++) {
        %>
        <option value="<%=usuarios.get(j).getLogin()%>" <%=request.getParameter("login") != null && request.getParameter("login").equals(usuarios.get(j).getLogin()) ? "selected" : ""%>><%=usuarios.get(j).getLogin() + " - " + usuarios.get(j).getNome()%></option>
        <%
            }
        %>
    </select>
    
    <label for="sexo">Sexo:</label>
    <select id="sexo" name="sexo" nullable="false" key="">
        <option value='' selected></option>
        <%
            Usuario.Sexo[] sexo = Usuario.Sexo.values();
            for (int j = 0; j < sexo.length; j++) {
        %>
        <option value='<%=sexo[j]%>'><%=sexo[j].toString()%></option>
        <%
            }
        %>
    </select>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="perfil">Perfil:</label>
    <select id="perfil" name="perfil" nullable="false" key="">
        <option value='' selected></option>
        <%
            Usuario.Perfil[] perfis = Usuario.Perfil.values();
            if (usuario.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
                for (int j = 0; j < perfis.length; j++) {
        %>
        <option value='<%=perfis[j]%>'><%=Usuario.getPerfilName(perfis[j])%></option>
        <%
            }
        } else {
        %>
        <option value='<%=usuario.getPerfil()%>'><%=Usuario.getPerfilName(usuario.getPerfil())%></option>
        <%
            }
        %>
    </select>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="telefone">Telefone:</label>
    <input type="input" id="telefone" name="telefone" size="23" maxlength="18" nullable="false" key="">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="credito"  style="display:none;">Crédito em Distribuições:</label>
    <input type=text id=credito name=credito nullable=true key="" style="display:none;">
    
    <label for="senha">Senha:</label>
    <input type="password" id="senha" name="senha" size="50" maxlength="45" nullable="false" key="" onblur="return isValidUsernameOrPassword(this);">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">    
    
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Alterar');">Alterar Usuário</button>
        </td>
    </table>
</form>

<ajax:updateField
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="login"
    doPost="true"
    valueUpdateByName="true"
    target="nome,cargo,tipoCargo,area,loginSubstituto,sexo,perfil,telefone,credito,senha"
    action="actionLogin"
    parameters="method=updateField,tabela={tabela},login={login}"
    parser="new ResponseXmlParser()"
    preFunction="initProgress"
    postFunction="resetProgress"
    errorFunction="reportError"
    />
<%@ include file="../footer.jsp" %>

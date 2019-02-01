<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="entities.Procurador" %>
<%@ include file="../header.jsp" %>
<h1>Alterar Procurador</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="alterar" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Procurador" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Alterar');">Alterar Procurador</button>
        </td>
    </table>
    <label for="procuradorCpf">CPF:</label>
    <input type="text" id="procuradorCpf" name="procuradorCpf" size="21" maxlength="14" onblur="return isValidCnpjOrCpf(this);" nullable="false" key="PRI" value="<%=(request.getParameter("procuradorCpf") != null ? request.getParameter("procuradorCpf"): "")%>">
    <button type="button" id="actionCpf" name="actionCpf">Buscar Dados</button>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    <span id="indicator_procuradorCpf" name="indicator_procuradorCpf" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="pronome">Pronome de Tratamento:</label>
    <input type="text" id="pronome" name="pronome" size="50" maxlength="45" nullable="false" key="">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" size="50" maxlength="45" nullable="false" key="">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="cargo">Cargo:</label>
    <input type="text" id="cargo" name="cargo" size="50" maxlength="45" nullable="false" key="">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="sexo">Sexo:</label>
    <select id="sexo" name="sexo" nullable="false" key="">
        <option value='' selected></option>
        <%
            Procurador.Sexo[] sexo = Procurador.Sexo.values();
            for (int j = 0; j < sexo.length; j++) {
        %>
        <option value='<%=sexo[j]%>'><%=sexo[j].toString()%></option>
        <%
            }
        %>
    </select>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Alterar');">Alterar Procurador</button>
        </td>
    </table>
</form>

<ajax:autocomplete
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="procuradorCpf"
    target="procuradorCpf"
    className="autocomplete"
    indicator="indicator_procuradorCpf"
    minimumCharacters="3"
    parameters="method=autoComplete,tabela={tabela},primaryKeyName=procuradorCpf,primaryKeyValue={procuradorCpf}"
    />
<ajax:updateField
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="procuradorCpf"
    doPost="true"
    valueUpdateByName="true"
    target="pronome,nome,cargo,sexo"
    action="actionCpf"
    parameters="method=updateField,tabela={tabela},procuradorCpf={procuradorCpf}"
    parser="new ResponseXmlParser()"
    preFunction="initProgress"
    postFunction="resetProgress"
    errorFunction="reportError"
    />
    <%@ include file="../footer.jsp" %>
    
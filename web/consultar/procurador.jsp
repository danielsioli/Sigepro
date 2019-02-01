<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="entities.Procurador" %>
<%@ include file="../header.jsp" %>
<h1>Consultar Procurador</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="consultar" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Procurador" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Procurador</button>
        </td>
    </table>
    <label for="procuradorCpf">CPF:</label>
    <input type="text" id="procuradorCpf" name="procuradorCpf" size="21" maxlength="14" onblur="return isValidCnpjOrCpf(this);" nullable="false" key="PRI">
    <span id="indicator_procuradorCpf" name="indicator_procuradorCpf" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="pronome">Pronome de Tratamento:</label>
    <input type="text" id="pronome" name="pronome" size="50" maxlength="45" nullable="false" key="">
    
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" size="50" maxlength="45" nullable="false" key="">
    
    <label for="cargo">Cargo:</label>
    <input type="text" id="cargo" name="cargo" size="50" maxlength="45" nullable="false" key="">
    
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
    
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Procurador</button>
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
<%@ include file="../footer.jsp" %>

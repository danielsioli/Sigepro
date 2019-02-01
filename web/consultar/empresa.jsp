<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import='entities.Empresa' %>
<%@ include file="../header.jsp" %>
<h1>Consultar Empresa</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="consultar" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Empresa" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Empresa</button>
        </td>
    </table>
    <label for="empresaCnpj">CNPJ / CPF:</label>
    <input type="text" id="empresaCnpj" name="empresaCnpj" size="23" maxlength="18" onblur="return isValidCnpjOrCpf(this);" nullable="false" key="PRI">
    <span id="indicator_empresaCnpj" name="indicator_empresaCnpj" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="razaoSocial">Razão Social:</label>
    <input type="text" id="razaoSocial" name="razaoSocial" size="50" maxlength="255" nullable="false" key="">
    
    <label for="cep">CEP:</label>
    <input type="text" id="cep" name="cep" size="15" maxlength="10" onblur="return isNumbersOnly(this);" nullable="false" key="">
    
    <label for="estado">Estado:</label>
    <select id="estado" name="estado" nullable="false" key="">
        <option value='' selected></option>
        <%
            Empresa.Estado[] estados = Empresa.Estado.values();
            for (int j = 0; j < estados.length; j++) {
        %>
        <option value='<%=estados[j]%>'><%=estados[j].toString()%></option>
        <%
            }
        %>
    </select>
    
    <label for="cidade">Cidade:</label>
    <input type="text" id="cidade" name="cidade" size="50" maxlength="45" nullable="false" key="">
    
    <label for="bairro">Bairro:</label>
    <input type="text" id="bairro" name="bairro" size="50" maxlength="100" nullable="false" key="">
    
    <label for="logradouro">Logradouro:</label>
    <input type="text" id="logradouro" name="logradouro" size="50" maxlength="100" nullable="false" key="">
    
    <label for="numeroLogradouro">Número do Logradouro:</label>
    <input type="text" id="numeroLogradouro" name="numeroLogradouro" size="15" maxlength="10" nullable="false" key="">
    
    <label for="complementoLogradouro">Complemento Logradouro:</label>
    <input type="text" id="complementoLogradouro" name="complementoLogradouro" size="50" maxlength="100" nullable="true" key="">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Empresa</button>
        </td>
    </table>
</form>

<ajax:autocomplete
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="empresaCnpj"
    target="empresaCnpj"
    className="autocomplete"
    indicator="indicator_empresaCnpj"
    minimumCharacters="3"
    parameters="method=autoComplete,tabela={tabela},primaryKeyName=empresaCnpj,primaryKeyValue={empresaCnpj}"
    />
    <%@ include file="../footer.jsp" %>
    
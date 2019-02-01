<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<h1>Consultar Procuração</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="consultar" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Procuracao" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Procuração</button>
        </td>
    </table>
    
    <label for="empresaCnpj">CNPJ / CPF da Empresa:</label>
    <input type="text" id="empresaCnpj" name="empresaCnpj" size="23" maxlength="18" onblur="return isValidCnpjOrCpf(this);" nullable="false" key="PRI">
    <span id="indicator_empresaCnpj" name="indicator_empresaCnpj" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="procuradorCpf">CPF do Procurador:</label>
    <input type="text" id="procuradorCpf" name="procuradorCpf" size="23" maxlength="18" onblur="return isValidCnpjOrCpf(this);" nullable="false" key="PRI">
    <span id="indicator_procuradorCpf" name="indicator_procuradorCpf" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="validade">Validade (dd/mm/aaaa):</label>
    <input type="text" id="validade" name="validade" size="10" maxlength="15" onblur="return isValidDate(this);" nullable="true" key="">
    
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Procuração</button>
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
    parameters="method=autoComplete,tabela=Empresa,primaryKeyName=empresaCnpj,primaryKeyValue={empresaCnpj},procurador={procuradorCpf}"
    />
<ajax:autocomplete
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="procuradorCpf"
    target="procuradorCpf"
    className="autocomplete"
    indicator="indicator_procuradorCpf"
    minimumCharacters="3"
    parameters="method=autoComplete,tabela=Procurador,primaryKeyName=procuradorCpf,primaryKeyValue={procuradorCpf},empresa={empresaCnpj}"
    />
<%@ include file="../footer.jsp" %>

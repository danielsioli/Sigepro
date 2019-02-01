<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import='entities.Servico' %>
<%@ include file="../header.jsp" %>
<h1>Consultar Processo</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="consultar" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Processo" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Processo</button>
        </td>
    </table>
    <label for="sicap">Número do Processo:</label>
    <input type="text" id="sicap" name="sicap" size="22" maxlength="17" onblur="return isNumbersOnly(this);" nullable="false" key="PRI">
    <span id="indicator_sicap" name="indicator_sicap" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="empresaCnpj">CNPJ / CPF:</label>
    <input type="text" id="empresaCnpj" name="empresaCnpj" size="23" maxlength="18" onblur="return isValidCnpjOrCpf(this);" nullable="false" key="">
    <span id="indicator_empresaCnpj" name="indicator_empresaCnpj" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="servicoNum">Serviço:</label>
    <%
            hashtable.clear();
            List<Servico> servicos = DAO.getInstance().findByNamedQuery(Usuario.class, "Servico.findAll", hashtable);

    %>
    <select id="servicoNum" name="servicoNum" nullable="false" key="MUL">
        <option value='' selected></option>
        <%
            for (int j = 0; j < servicos.size(); j++) {
        %>
        <option value="<%=servicos.get(j).getServicoNum()%>"><%=servicos.get(j).getServicoNum() + " - " + servicos.get(j).getNome()%></option>
        <%
            }
        %>
    </select>
    
    <label for="peso">Peso (Máximo de Distribuições Seguidas para o mesmo Servidor):</label>
    <input type=text id=peso name=peso nullable=false key="" onblur="if (isNumbersOnly(this) && this.value <= 0){alert('Peso deve ser maior que 0');this.focus()}">
    
    <input type=text id=solicitacoesSeguidas name=solicitacoesSeguidas nullable=true key="" value=0 style="display:none;">
    
    <label for="fistel">Número de Fistel:</label>
    <input type="text" id="fistel" name="fistel" size="16" maxlength="11" onblur="return isValidFistelOrSitar(this);" nullable="true" key="">
    
    <label for="sitar">Número da Entidade:</label>
    <input type="text" id="sitar" name="sitar" size="13" maxlength="8" onblur="return isValidFistelOrSitar(this);" nullable="true" key="">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Consultar');">Consultar Processo</button>
        </td>
    </table>
</form>

<ajax:autocomplete
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="sicap"
    target="sicap"
    className="autocomplete"
    indicator="indicator_sicap"
    minimumCharacters="6"
    parameters="method=autoComplete,tabela={tabela},primaryKeyName=sicap,primaryKeyValue={sicap}"
    />
<ajax:autocomplete
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="empresaCnpj"
    target="empresaCnpj"
    className="autocomplete"
    indicator="indicator_empresaCnpj"
    minimumCharacters="3"
    parameters="method=autoComplete,tabela=Empresa,primaryKeyName=empresaCnpj,primaryKeyValue={empresaCnpj}"
    />
    <%@ include file="../footer.jsp" %>
    
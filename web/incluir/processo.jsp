<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import='entities.Servico' %>
<%@ include file="../header.jsp" %>
<h1>Incluir Processo</h1>
<%
            Cookie cookies[] = request.getCookies();
            Cookie empresaCnpjCookie = null;
            Cookie servicoNumCookie = null;
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("last_empresaCnpj")) {
                        empresaCnpjCookie = cookies[i];
                    } else if (cookies[i].getName().equals("last_servicoNum")) {
                        servicoNumCookie = cookies[i];
                    }
                }
            }
%>
<form name="frmacao">
    <input type="text" id="method" name="method" value="incluir" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Processo" nullable="STATIC" key="" style="display:none;">
        <input type="text" id="idtContrato" name="idtContrato" value="" nullable="true" key="" style="display:none;">
            <input type="text" id="id" name="id" value="" nullable="true" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Incluir');">Incluir Processo</button>
        </td>
    </table>
    <label for="sicap">Número do Processo:</label>
    <input type="text" id="sicap" name="sicap" size="22" maxlength="17" onblur="return isNumbersOnly(this);" nullable="false" key="PRI">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    <button type="button" id="actionSicap" name="actionSicap" style="display:none;">Buscar Dados</button>
    <span id="indicator_sicap" name="indicator_sicap" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="empresaCnpj">CNPJ / CPF:</label>
    <input type="text" id="empresaCnpj" name="empresaCnpj" size="23" maxlength="18" onblur="return isValidCnpjOrCpf(this);" nullable="false" key="" value="<%=empresaCnpjCookie != null ? empresaCnpjCookie.getValue() : ""%>">
    <button type="button" id="actionEmpresaCnpj" name="actionEmpresaCnpj" style="display:none;">Buscar Dados</button>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    <span id="indicator_empresaCnpj" name="indicator_empresaCnpj" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
    
    <label for="servicoNum">Serviço:</label>
    <%
            hashtable.clear();
            List<Servico> servicos = DAO.getInstance().findByNamedQuery(Usuario.class, "Servico.findAll", hashtable);

    %>
    <select id="servicoNum" name="servicoNum" nullable="false" key="MUL">
        <option value=''  <%=servicoNumCookie == null ? "selected" : ""%>></option>
        <%
            for (int j = 0; j < servicos.size(); j++) {
        %>
        <option value="<%=servicos.get(j).getServicoNum()%>" <%=servicoNumCookie != null && servicoNumCookie.getValue().equals(servicos.get(j).getServicoNum()) ? "selected" : ""%>><%=servicos.get(j).getServicoNum() + " - " + servicos.get(j).getNome()%></option>
        <%
            }
        %>
    </select>
    <img id="obrigatorio_servicoId" src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="peso">Peso (Máximo de Distribuições Seguidas para o mesmo Servidor):</label>
    <input type=text id=peso name=peso nullable=false key="" value=1 onblur="if (isNumbersOnly(this) && this.value <= 0){alert('Peso deve ser maior que 0');this.focus()}">
    <img id="obrigatorio_servicoId" src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
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
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Incluir');">Incluir Processo</button>
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
    parameters="method=autoComplete,tabela=Empresa,primaryKeyName=empresaCnpj,primaryKeyValue={empresaCnpj}"
    />
<%@ include file="../footer.jsp" %>

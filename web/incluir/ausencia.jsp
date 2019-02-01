<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import='entities.Empresa' %>
<%@ page import='java.util.Calendar' %>
<%@ include file="../header.jsp" %>
<h1>Incluir Ausência</h1>
<form name="frmacao">
    <input type="text" id="method" name="method" value="incluir" nullable="STATIC" key="" style="display:none;">
    <input type="text" id="tabela" name="tabela" value="Ausencia" nullable="STATIC" key="" style="display:none;">
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'Incluir');">Incluir Ausência</button>
        </td>
    </table>
    <%
            hashtable.clear();
            List<Usuario> usuarios = DAO.getInstance().findByNamedQuery(Usuario.class, "Usuario.findAll", hashtable);

    %>
    <!--TODO: recuperar ausencias do usuário escolhido para mostrar na tela -->
    <select id="login" name="login" nullable="false" key="PRI" onchange="return getAusencias('frmacao',this.value)">
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
    <img id="obrigatorio_servicoId" src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="delay">Delay (dias para parar distribuição antes da ausência):</label>
    <input type=text id=delay name=delay nullable=false key="" value=0 onblur="if (isNumbersOnly(this) && this.value < 0){alert('Peso deve ser maior ou igual a 0');this.focus()}">
    <img id="obrigatorio_servicoId" src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="inicioAusencia">Início da Ausência (dd/mm/aaaa):</label>
    <input type="text" id="inicioAusencia" name="inicioAusencia" size="10" maxlength="15" onblur="return isValidDate(this);" nullable="false" key="PRI">
    <img id="obrigatorio_servicoId" src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="tempoAusente">Tempo Ausente (Dias):</label>
    <input type="text" id="tempoAusente" name="tempoAusente" size="10" maxlength="15" onblur="return isNumbersOnly(this);" nullable="false" key="">
    <img id="obrigatorio_servicoId" src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <table>
        <td>
            <button type="reset" id="limpar" name="limpar">Limpar</button>
        </td>
        <td>
            <button type="button" id="confirmar" name="confirmar" onclick="if(document.getElementById('delay').value > document.getElementById('tempoAusente').value){alert('Delay não pode ser maior que o tempo de ausencia')}else{return getResponse(frmacao,'Incluir');}">Incluir Ausência</button>
        </td>
    </table>
    
    <label for="ausencias">Ausências entre 01/01/<%=Calendar.getInstance().get(Calendar.YEAR)%> e 31/01/<%=Calendar.getInstance().get(Calendar.YEAR)+1%>:</label>
    <!-- TODO: mostrar ausencias do usuário escolhido aqui -->
    <div id=ausencias style="display:none"></div>
</form>
<%@ include file="../footer.jsp" %>
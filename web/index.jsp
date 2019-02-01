<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<%@ page import="entities.Processo" %>
<script type="text/javascript">
    function onReset(){
        document.getElementById('messageProcesso').innerHTML = 'Processo ainda não selecionado';
        document.getElementById('messageProcesso').style.color = 'red';
        var processo = document.getElementById('processo');
        var messageProcesso = document.getElementById('messageProcesso');
        if(processo != null){
            var date = new Date();
            date.setTime(date.getTime() + -1*30*60*1000);//já expirou
            var expires = "; expires=" + date.toGMTString();
            document.cookie = 'current_sicap=' + processo.value + expires + '; path=/';
            document.cookie = 'last_sicap=' + processo.value + expires + '; path=/';
            document.cookie = 'messageProcesso=' + messageProcesso.innerHTML + expires + '; path=/';
        }
    }
</script>
<%@ taglib uri="http://ajaxtags.org/tags/ajax" prefix="ajax" %>
<jsp:useBean id="processo" class="entities.Processo" scope="session"/>
<%
            Cookie cookies[] = request.getCookies();
            Cookie lastSicapCookie = null;
            Cookie currentSicapCookie = null;
            Cookie messageProcesso = null;
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("last_sicap")) {
                        lastSicapCookie = cookies[i];
                    } else if (cookies[i].getName().equals("current_sicap")) {
                        currentSicapCookie = cookies[i];
                    } else if (cookies[i].getName().equals("messageProcesso")) {
                        messageProcesso = cookies[i];
                    }
                }
            }
            String[] razaoSocial = new String[]{""};
            if (currentSicapCookie != null) {
                if (processo == null) {
                    processo = DAO.getInstance().find(Processo.class, currentSicapCookie.getValue());
                } else if (processo.getSicap() == null) {
                    processo = DAO.getInstance().find(Processo.class, currentSicapCookie.getValue());
                } else if (!processo.getSicap().equals(currentSicapCookie.getValue())) {
                    processo = DAO.getInstance().find(Processo.class, currentSicapCookie.getValue());
                }
                razaoSocial = processo.getEmpresaCnpj().getRazaoSocial().split(" ");
            } else if (lastSicapCookie != null) {
                if (processo == null) {
                    processo = DAO.getInstance().find(Processo.class, lastSicapCookie.getValue());
                } else if (processo.getSicap() == null) {
                    processo = DAO.getInstance().find(Processo.class, lastSicapCookie.getValue());
                } else if (!processo.getSicap().equals(lastSicapCookie.getValue())) {
                    processo = DAO.getInstance().find(Processo.class, lastSicapCookie.getValue());
                }
                razaoSocial = processo.getEmpresaCnpj().getRazaoSocial().split(" ");
            }
%>
<input id=processo name=processo style="display:none" value="<%=(currentSicapCookie != null ? currentSicapCookie.getValue() : (lastSicapCookie != null ? lastSicapCookie.getValue() : ""))%>">
<div id="messageProcesso" name="messageProcesso" <%=(currentSicapCookie != null || lastSicapCookie != null) ? "style=\"color:black;\">Processo atual: " + processo.getSicap() + " - " + razaoSocial[0] + (razaoSocial.length > 1 ? " " + razaoSocial[1] : "") + " - " + processo.getServicoNum().getServicoNum() : "style=\"color:red;\">Processo ainda não selecionado"%></div>
     <br>
<form name="frmacao" onreset="onReset()">
    <input type="text" id="method" name="method" value="getProcesso" nullable="STATIC" key="" style="display:none;">
    <label for="sicap">Número do Processo:</label>
    <input type="text" id="sicap" name="sicap" size="22" maxlength="17" onblur="return isNumbersOnly(this);" nullable="false" key="PRI">
    <button type="button" id="confirmar" name="confirmar" onclick="return getResponse(frmacao,'GetProcesso');">Pegar Processo</button>
    <button type="reset" id="limpar" name="limpar" onclick="document.getElementById('processo').value = '';document.getElementById('sicap').value = ''">Limpar</button>
    <span id="indicator_sicap" name="indicator_sicap" style="display:none;"><img src="${pageContext.request.contextPath}/img/indicator.gif" /></span>
</form>
<br>
<ajax:tabPanel
    panelStyleId="tabPanel"
    contentStyleId="tabContent"
    panelStyleClass="tabPanel"
    contentStyleClass="tabContent"
    currentStyleClass="ajaxCurrentTab">
<ajax:tab caption="Sistemas"
          baseUrl="${pageContext.request.contextPath}/painel/sistemas.jsp"
          defaultTab="false"
          />
<ajax:tab caption="Aplicativos"
          baseUrl="${pageContext.request.contextPath}/painel/aplicativos.jsp"
          defaultTab="false"
          />
<ajax:tab caption="Documentos"
          baseUrl="${pageContext.request.contextPath}/painel/documentos.jsp"
          defaultTab="true"
          />
</ajax:tabPanel>

<ajax:autocomplete
    baseUrl="${pageContext.request.contextPath}/control.view"
    source="sicap"
    target="sicap"
    className="autocomplete"
    indicator="indicator_sicap"
    minimumCharacters="6"
    parameters="method=autoComplete,tabela=Processo,primaryKeyName=sicap,primaryKeyValue={sicap}"
    />
<%@ include file="footer.jsp" %>
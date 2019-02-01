<%-- 
    Document   : documentos
    Created on : 06/05/2008, 14:28:53
    Author     : danieloliveira
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List" %>
<%@ page import="dao.DAO" %>
<%@ page import="entities.Oficio" %>
<%@ page import="entities.Usuario" %>
<%@ page import="entities.Estatistica" %>
<%@ page import="javax.security.auth.Subject" %>

<jsp:useBean id="hashtable" scope="request" class="java.util.Hashtable"/>

<%
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            Usuario usuario = ((Usuario) ((Subject) session.getAttribute("javax.security.auth.subject")).getPrincipals().iterator().next());
            Estatistica maiorEstatistica = usuario.getMaiorEstatistica();
%>

<script type="text/javascript">
    if (document.getElementsByTagName) {
        var tipoDocumento = document.getElementById("tipoDocumento");
        window.select_current = new Array();
        tipoDocumento.onchange = function(){restore(tipoDocumento);}
        emulate(tipoDocumento);
    }

    function restore(e) {
        if (e.options[e.selectedIndex].disabled) {
            e.selectedIndex = window.select_current[e.id];
        }
        onTipoDocumentoChanged();
    }

    function emulate(e) {
        for (var i=0, option; option = e.options[i]; i++) {
            if (option.disabled) {
                option.style.color = "graytext";
            }
            else {
                option.style.color = "menutext";
            }
        }
    }
    
    function onTipoDocumentoChanged(){
        var tipoDocumento = document.getElementById('tipoDocumento');
        for (var i=0, option; option = tipoDocumento.options[i]; i++) {
            if(option.selected && option.value.length > 0){
                if(option.value != "ar"){
                    document.getElementById('tipo_' + option.value).style.display = "block";
                }else{
                    getARScreen();
                }
            }else if(option.value.length > 0){
                if(document.getElementById('tipo_' + option.value)){
                    document.getElementById('tipo_' + option.value).style.display = "none";
                }
                if(document.getElementById('base_' + option.value)){
                    document.getElementById('base_' + option.value).style.display = "none";
                }
                document.getElementById('telaDocumento').style.display = "none";
            }
        }
        document.getElementById('select_analiseJuridica').selectedIndex = 0;
        document.getElementById('select_analiseTecnica').selectedIndex = 0;
        document.getElementById('select_ato').selectedIndex = 0;
        document.getElementById('select_despacho').selectedIndex = 0;
        document.getElementById('select_extrato').selectedIndex = 0;
        document.getElementById('select_informe').selectedIndex = 0;
        document.getElementById('select_materia').selectedIndex = 0;
        document.getElementById('select_oficio').selectedIndex = 0;
    }
</script>
<!-- Tipos de Documentos -->
<label for="tipoDocumento">Tipo de Documento:</label>
<select id="tipoDocumento" name="tipoDocumento">
    <option value='' <%=maiorEstatistica.getTipoDocumento().equals("") ? "selected" : ""%>></option>
    <option value='analiseJuridica' <%=maiorEstatistica.getTipoDocumento().equals("AnaliseJuridica") ? "selected" : "disabled"%>>Análise Jurídica</option>
    <option value='analiseTecnica' <%=maiorEstatistica.getTipoDocumento().equals("AnaliseTecnica") ? "selected" : "disabled"%>>Análise Técnica</option>
    <option value='ato' <%=maiorEstatistica.getTipoDocumento().equals("Ato") ? "selected" : "disabled"%>>Ato</option>
    <option value='despacho' <%=maiorEstatistica.getTipoDocumento().equals("Despacho") ? "selected" : "disabled"%>>Despacho</option>
    <option value='ar' <%=maiorEstatistica.getTipoDocumento().equals("AR") || maiorEstatistica.getTipoDocumento().equals("Etiqueta") ? "selected" : ""%>>Etiqueta / AR</option>
    <option value='extrato' <%=maiorEstatistica.getTipoDocumento().equals("Extrato") ? "selected" : "disabled"%>>Extrato de Ato</option>
    <option value='informe' <%=maiorEstatistica.getTipoDocumento().equals("Informe") ? "selected" : "disabled"%>>Informe</option>
    <option value='materia' <%=maiorEstatistica.getTipoDocumento().equals("Materia") ? "selected" : "disabled"%>>Matéria</option>
    <option value='oficio' <%=maiorEstatistica.getTipoDocumento().equals("Oficio") ? "selected" : ""%>>Ofício</option>
</select>

<script type="text/javascript">
    var tipoDocumento = document.getElementById('tipoDocumento');
    if(tipoDocumento.selectedIndex == 5){
        getARScreen();
    }
</script>

<!-- Fim dos Tipos de Documentos -->

<!-- Tipos de Análise Técnica -->
<div id="tipo_analiseTecnica" style="display:<%=maiorEstatistica.getTipoDocumento().equals("AnaliseTecnica") ? "block" : "none"%>">
    <label for="select_analiseTecnica">Modelo de Análise Técnica:</label>
    <select id="select_analiseTecnica" name="select_analiseTecnica">
        <option value="" selected></option>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>
<div id="base_analiseTecnica" style="display:none">
    <label for="select_base_analiseTecnica">Baseado em:</label>
    <select id="select_base_analiseTecnica" name="select_base_analiseTecnica" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0">Nenhum</option>
    </select>
</div>
<!-- Fim dos Tipos de Análise -->

<!-- Tipos de Análise Jurídica -->
<div id="tipo_analiseJuridica" style="display:<%=maiorEstatistica.getTipoDocumento().equals("AnaliseJuridica") ? "block" : "none"%>">
    <label for="select_analiseJuridica">Modelo de Análise Jurídica:</label>
    <select id="select_analiseJuridica" name="select_analiseJuridica">
        <option value="" selected></option>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>
<div id="base_analiseJuridica" style="display:none">
    <label for="select_base_analiseJuridica">Baseado em:</label>
    <select id="select_base_analiseJuridica" name="select_base_analiseJuridica" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0">Nenhum</option>
    </select>
</div>
<!-- Fim dos Tipos de Análise -->

<!-- Tipos de Informe -->
<div id="tipo_informe" style="display:<%=maiorEstatistica.getTipoDocumento().equals("Informe") ? "block" : "none"%>">
    <label for="select_informe">Modelo de Informe:</label>
    <select id="select_informe" name="select_informe">
        <option value="" selected></option>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>
<div id="base_informe" style="display:none">
    <label for="select_base_informe">Baseado em:</label>
    <select id="select_base_informe" name="select_base_informe" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0">Nenhum</option>
    </select>
</div>
<!-- Fim dos Tipos de Informe -->

<!-- Tipos de Matéria -->
<div id="tipo_materia" style="display:<%=maiorEstatistica.getTipoDocumento().equals("Materia") ? "block" : "none"%>">
    <label for="select_materia">Modelo de Matéria:</label>
    <select id="select_materia" name="select_materia">
        <option value="" selected></option>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>
<div id="base_materia" style="display:none">
    <label for="select_base_materia">Baseado em:</label>
    <select id="select_base_materia" name="select_base_materia" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0">Nenhum</option>
    </select>
</div>
<!-- Fim dos Tipos de Matéria -->

<!-- Tipos de Ato -->
<div id="tipo_ato" style="display:<%=maiorEstatistica.getTipoDocumento().equals("Ato") ? "block" : "none"%>">
    <label for="select_ato">Modelo de Ato:</label>
    <select id="select_ato" name="select_ato">
        <option value="" selected></option>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>
<div id="base_ato" style="display:none">
    <label for="select_base_ato">Baseado em:</label>
    <select id="select_base_ato" name="select_base_ato" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0">Nenhum</option>
    </select>
</div>
<!-- Fim dos Tipos de Ato -->

<!-- Tipos de Extrato de Ato -->
<div id="tipo_extrato" style="display:<%=maiorEstatistica.getTipoDocumento().equals("Extrato") ? "block" : "none"%>">
    <label for="select_extrato">Modelo de Extrato:</label>
    <select id="select_extrato" name="select_extrato">
        <option value="" selected></option>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>
<div id="base_extrato" style="display:none">
    <label for="select_base_extrato">Baseado em:</label>
    <select id="select_base_extrato" name="select_base_extrato" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0">Nenhum</option>
    </select>
</div>
<!-- Fim dos Tipos de Extrato de Ato -->

<!-- Ofício -->
<div id="tipo_oficio" style="display:<%=maiorEstatistica.getTipoDocumento().equals("Oficio") ? "block" : "none"%>">
    <label for="select_oficio">Modelo de Ofício:</label>
    <select id="select_oficio" name="select_oficio" onchange="return getDocumentScreen(this.value,0);">
        <option value="" selected></option>
        <%
            hashtable.clear();
            List<Oficio> oficios = DAO.getInstance().findByNamedQuery(Oficio.class, "Oficio.findAllOrderByNome", hashtable);
            for (int i = 0; i < oficios.size(); i++) {
                if (oficios.get(i).getPublico() == 1) {
        %>
        <option value="<%=oficios.get(i).getOficioId()%>" <%=maiorEstatistica.getModelo().equals(oficios.get(i).getNome()) ? "selected" : ""%>><%=oficios.get(i).getNome()%></option>
        <%
            } else {
                if (oficios.get(i).getDono().equals(usuario)) {
        %>
        <option value="<%=oficios.get(i).getOficioId()%>" <%=maiorEstatistica.getModelo().equals(oficios.get(i).getNome()) ? "selected" : ""%>><%=oficios.get(i).getNome()%></option>
        <%
                    }
                }
            }
        %>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>

<script type="text/javascript">
    var tipoOficio = document.getElementById('select_oficio');
    if(document.getElementById('tipo_oficio').style.display == "block"){
        if (tipoOficio.selectedIndex != 0){
            getDocumentScreen(tipoOficio.value,0)
        }
    }
</script>

<div id="base_oficio" style="display:none">
    <label for="select_base_oficio">Baseado em:</label>
    <select id="select_base_oficio" name="select_base_oficio" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0" selected>Nenhum</option>
        <%
            for (int i = 0; i < oficios.size(); i++) {
        %>
        <option value="<%=oficios.get(i).getOficioId()%>"><%=oficios.get(i).getNome()%></option>
        <%
            }
        %>
    </select>
</div>
<!-- Fim de Ofício -->

<!-- Tipos de Despacho -->
<div id="tipo_despacho" style="display:<%=maiorEstatistica.getTipoDocumento().equals("Despacho") ? "block" : "none"%>">
    <label for="select_despacho">Modelo de Despacho:</label>
    <select id="select_despacho" name="select_despacho">
        <option value="" selected></option>
        <option value="-1" style="background-color:yellow">Criar novo Modelo</option>
    </select>
</div>
<div id="base_despacho" style="display:none">
    <label for="select_base_despacho">Baseado em:</label>
    <select id="select_base_despacho" name="select_base_despacho" onchange="return getDocumentScreen(-1,this.value);">
        <option value="0">Nenhum</option>
    </select>
</div>
<!-- Fim dos Tipos de Despacho -->

<!-- Tela para manipular documento -->
<div id="telaDocumento"></div>
<!-- Fim da Tela para manipular documento -->
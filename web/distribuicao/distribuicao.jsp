<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import='entities.Empresa' %>
<%@ include file="../header.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/distribuicao/distribuicao.js"></script>

<h1>Distribuição</h1>

<table>
    <td>
        <button type="button" id="distribuir" name="distribuir" onclick="distribute()">Distribuir</button>
    </td>
    <td>
        <button type="button" id="limpar" name="limpar" onclick="clean()">Limpar</button>
    </td>
</table>

<div id=processos>
    <table id=processosTable border=2>
        <tr>
            <td id=processo_1 bgcolor=#FFFFFF>
                <label for="processo_1_sicap">Número do Processo:</label>
                <input type="text" id="processo_1_sicap" name="processo_1_sicap" size="22" maxlength="17" onblur="return isNumbersOnly(this);" nullable="false" key="PRI">
                
                <label for=processo_1_resposta>Resposta a Exigência</label>
                <select id=processo_1_resposta name=processo_1_resposta nullable=false key=PRI>
                    <option value=0 selected>Não</option>
                    <option value=1>Sim</option>
                </select>
                
                <label for=processo_1_fila>Fila:</label>
                <select id=processo_1_fila name=processo_1_fila nullable=false key=PRI>
                    <option value=1>Administrativa</option>
                    <option value=2>Jurídica</option>
                    <option value=3 selected>Técnica</option>
                </select>
                
                <label for=processo_1_destinatarios>Destinatário:</label>
                <%
            hashtable.clear();
            List<Usuario> usuarios = DAO.getInstance().findByNamedQuery(Usuario.class, "Usuario.findAll", hashtable);

                %>
                <select id=processo_1_destinatarios name=processo_1_destinatarios nullable=false key=PRI>
                    <option value=auto selected>Escolher Automaticamente</option>
                    <%
            if (usuario.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
                for (int j = 0; j < usuarios.size(); j++) {
                    %>
                    <option value="<%=usuarios.get(j).getLogin()%>" <%=request.getParameter("login") != null && request.getParameter("login").equals(usuarios.get(j).getLogin()) ? "selected" : ""%>><%=usuarios.get(j).getLogin() + " - " + usuarios.get(j).getNome()%></option>
                    <%
                        }
                    } else {
                    %>
                    <option value="<%=usuario.getLogin()%>"><%=usuario.getLogin() + " - " + usuario.getNome()%></option>
                    <%
            }
                    %>
                </select>
                
            </td>
            <td valign=top bgcolor=#FFFFFF>
                <select id=processo_1_MoreActions name=processo_1_MoreActions onchange="moreActions('processo_1',this.value)">
                    <option value='' style="color:silver">Mais Ações</option>
                    <option value='add'>Adicionar Processo</option>
                </select>
            </td>
        </tr>
    </table>
</div>

<table>
    <td>
        <button type="button" id="distribuir" name="distribuir" onclick="distribute()">Distribuir</button>
    </td>
    <td>
        <button type="button" id="limpar" name="limpar" onclick="clean()">Limpar</button>
    </td>
</table>

<%@ include file="../footer.jsp" %>

/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function initRequest() {
    var httpRequest = null;
    try {  // Firefox, Opera 8.0+, Safari
        httpRequest = new XMLHttpRequest();
    } catch (e) { // Internet Explorer
        try {
            httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                showAlert("Your browser does not support AJAX!");
            }
        }
    }
    return httpRequest;
}

function moreActions(processo,action){
    document.getElementById(processo + '_MoreActions').selectedIndex = 0;
    var processos = document.getElementById("processos");
    var processosTable = document.getElementById("processosTable");
    var processosTD = processosTable.getElementsByTagName("td");
    var html = "<table id=processosTable border=2>\n";
    var white = "#FFFFFF";
    var yellow = "#FFFF00";
    var currentColor = 0;
    if(action =='add'){
        var qtd = prompt("Adicionar quantos Processos?","1");
    }
    for (var i = 0, currentProcesso; currentProcesso = processosTD[i]; i+=2) {
        if(currentProcesso.id == processo){
            switch(action){
                case '':
                    break;
                case 'add':
                    if(currentColor == 0){
                        html+=writeProcesso(i + 1,currentProcesso.id.split("_")[1],white);
                    }else{
                        html+=writeProcesso(i + 1,currentProcesso.id.split("_")[1],yellow);
                    }
                    currentColor = (currentColor + 1) % 2;
                    for(var t = 0; t < qtd; t++){
                        if(currentColor == 0){
                            html+=writeProcesso(i + 2 + t,0,white);
                        }else{
                            html+=writeProcesso(i + 2 + t,0,yellow);
                        }
                        currentColor = (currentColor + 1) % 2;
                    }
                    break;
                case 'remove':
                    break;
            }
        }else{
            if(currentColor == 0){
                html+=writeProcesso(i + 1,currentProcesso.id.split("_")[1],white);
            }else{
                html+=writeProcesso(i + 1,currentProcesso.id.split("_")[1],yellow);
            }
            currentColor = (currentColor + 1) % 2;
        }
    }
    html+="<table>\n";
    processos.innerHTML = html;
}

function writeProcesso(i,iOriginal,color){
    var html = "<tr>\n";
    html+="<td id=processo_" + i + " bgcolor=" + color + ">\n";
    html+="   <label for='id=processo_" + i + "_sicap'>Número do Processo:</label>\n";
    if(document.getElementById("processo_" + iOriginal + "_sicap")){
        html+="   <input type='text' id='processo_" + i + "_sicap' name='processo_" + i + "_sicap' size='22' maxlength='17' onblur='return isNumbersOnly(this);' nullable='false' key='PRI' value='" + document.getElementById("processo_" + iOriginal + "_sicap").value + "'>\n";
    }else{
        html+="   <input type='text' id='processo_" + i + "_sicap' name='processo_" + i + "_sicap' size='22' maxlength='17' onblur='return isNumbersOnly(this);' nullable='false' key='PRI'>\n";
    }
    html+="   \n";
    html+="   <label for=processo_" + i + "_resposta>Resposta a Exigência</label>\n";
    html+="   <select id=processo_" + i + "_resposta name=processo_" + i + "_resposta nullable=false key=PRI>\n";
    var resposta = document.getElementById("processo_" + iOriginal + "_resposta");
    if(resposta){
        if(resposta.selectedIndex == 0){
            html+="       <option value=0 selected>Não</option>\n";
            html+="       <option value=1>Sim</option>\n";
        }else{
            html+="       <option value=0>Não</option>\n";
            html+="       <option value=1 selected>Sim</option>\n";
        }
    }else{
        html+="       <option value=0 selected>Não</option>\n";
        html+="       <option value=1>Sim</option>\n";
    }
    html+="   </select>\n";
    html+="   <label for=processo_" + i + "_fila>Fila:</label>\n";
    html+="   <select id=processo_" + i + "_fila name=processo_" + i + "_fila nullable=false key=PRI>\n";
    if(document.getElementById("processo_" + iOriginal + "_fila")){
        var selectedIndex = document.getElementById("processo_" + iOriginal + "_fila").selectedIndex;
        switch(selectedIndex){
            case 0:
                html+="       <option value=1 selected>Administrativa</option>\n";
                html+="       <option value=2>Jurídica</option>\n";
                html+="       <option value=3>Técnica</option>\n";
                break;
            case 1:
                html+="       <option value=1>Administrativa</option>\n";
                html+="       <option value=2 selected>Jurídica</option>\n";
                html+="       <option value=3>Técnica</option>\n";
                break;
            default:
                html+="       <option value=1>Administrativa</option>\n";
                html+="       <option value=2>Jurídica</option>\n";
                html+="       <option value=3 selected>Técnica</option>\n";
                            
        }
    }else{
        html+="       <option value=1>Administrativa</option>\n";
        html+="       <option value=2>Jurídica</option>\n";
        html+="       <option value=3 selected>Técnica</option>\n";
    }
    html+="   </select>\n";
    html+="   \n";
    html+="   <label for=processo_" + i + "_destinatarios>Destinatário:</label>\n";
    html+="   <select id=processo_" + i + "_destinatarios name=processo_" + i + "_destinatarios nullable=false key=PRI>\n";
    var usuarios = document.getElementById("processo_1_destinatarios").options;
    var destinatarios = document.getElementById("processo_" + iOriginal + "_destinatarios");
    if(usuarios){
        var selectedIndex = 0;
        if(destinatarios){
            selectedIndex = destinatarios.selectedIndex;
        }
        for(var j = 0, usuario; usuario = usuarios[j]; j++){
            if(j == selectedIndex){
                html+="       <option value=" + usuario.value + " selected>" + usuario.innerHTML + "</option>\n";
            }else{
                html+="       <option value=" + usuario.value + ">" + usuario.innerHTML + "</option>\n";
            }
        }
    }
    html+="   </select>\n";
    html+="</td>\n";
    html+="<td valign=top bgcolor=" + color + ">\n";
    html+="   <select id=processo_" + i + "_MoreActions name=processo_" + i + "_MoreActions onchange=\"moreActions('processo_" + i + "',this.value)\">\n";
    html+="       <option value='' style='color:silver'>Mais Ações</option>\n";
    html+="       <option value='add'>Adicionar Processo</option>\n";
    if(i != 1){
        html+="       <option value='remove'>Remover Processo</option>\n";
    }
    html+="   </select>\n";
    html+="</td>\n";
    html+="</tr>\n"
    return html;
}

function clean(){
    var processos = document.getElementById("processos");
    var html = "<table id=processosTable border=2>\n";
    html+=writeProcesso(1,0,"#FFFFFF");
    html+="<table>\n";
    processos.innerHTML = html;
}

function distribute(){
    var url = "/sigepro/control.view"
    var params = "method=autoDistribute";
    var processosTable = document.getElementById("processosTable");
    var processosTD = processosTable.getElementsByTagName("td");
    var sicaps = "&";
    var respostas = "";
    var filas = "";
    var destinatarios = "";
    var go = true;
    for (var i = 0, currentProcesso; currentProcesso = processosTD[i]; i+=2) {
        if(document.getElementById(currentProcesso.id + "_sicap").value != ''){
            sicaps += "sicap=" + document.getElementById(currentProcesso.id + "_sicap").value + "&";
            respostas += "resposta=" + document.getElementById(currentProcesso.id + "_resposta").value + "&";
            filas += "fila=" + document.getElementById(currentProcesso.id + "_fila").value + "&";
            if(i == processosTD.length - 2){
                destinatarios += "destinatario=" + document.getElementById(currentProcesso.id + "_destinatarios").value;
            }else{
                destinatarios += "destinatario=" + document.getElementById(currentProcesso.id + "_destinatarios").value + "&";
            }
        }else{
            go = false;
            break;
        }
    }
    if(!go){
        alert("Preencha todos os números de Processos");
    }else{
        params += sicaps + respostas + filas + destinatarios;
        var httpRequest = initRequest();
        if(httpRequest != null){
            httpRequest.onreadystatechange = function() {
                if (httpRequest.readyState == 4) {
                    if (httpRequest.status == 200) {
                        //TODO: mostrar resultado da distribuição
                    }
                }
            };
            httpRequest.open("POST", url, true);
            httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            httpRequest.setRequestHeader("Content-length", params.length);
            httpRequest.setRequestHeader("Connection", "close");
            httpRequest.send(params);
        }
    }
}


function isValidFields(form,tipo){
    var msg = "";
    var inputs = form.getElementsByTagName("input");
    var selects = form.getElementsByTagName("select");
    if(tipo == "Excluir"){
        for(var i = 0; i < inputs.length; i++){
            if(inputs[i].attributes["nullable"].value != "STATIC"){
                msg+=validField(inputs[i]);
                if(inputs[i].attributes["key"].value == "PRI"){
                    msg += fieldEmpty(inputs[i]);
                }
            }
        }
        for(var i = 0; i < selects.length; i++){
            if(selects[i].attributes["key"].value == "PRI"){
                msg += fieldEmpty(selects[i]);
            }
        }
    }else if(tipo == "Consultar"){
    var atLeastOne = false;
    for(var i = 0; i < inputs.length; i++){
        if(inputs[i].attributes["nullable"].value != "STATIC"){
            msg+=validField(inputs[i]);
            if(fieldEmpty(inputs[i]).length == 0){
                atLeastOne = true;
                break;
            }
        }
    }
    if(!atLeastOne){
        for(var i = 0; i < selects.length; i++){
            if(fieldEmpty(selects[i]).length == 0){
                atLeastOne = true;
                break;
            }
        }
    }
    if(!atLeastOne){
        msg="Pelo menos um campo deve ser preenchido!";
    }
}else if(tipo != "GetProcesso"){
for(var i = 0; i < inputs.length; i++){
    if(inputs[i].attributes["nullable"].value != "STATIC"){
        msg+=validField(inputs[i]);
        if(inputs[i].attributes["nullable"].value == "false"){
            msg += fieldEmpty(inputs[i]);
        }
    }
}

for(var i = 0; i < selects.length; i++){
    if(selects[i].attributes["nullable"].value == "false"){
        msg += fieldEmpty(selects[i]);
    }
}
}
return msg;
}

function validDate(field){
    var goodDates = /(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d/;
    if(field.value.length > 0 & !goodDates.test(field.value)){
        return "Entre com uma data válida em " + field.name + ".\n";
    }else{
    return "";
}
}

function isValidDate(field){
    var result = "";
    result = validDate(field);
    if(result.length == 0){
        return true;
    }else{
    alert(result);
    field.focus();
    return false;
}
}

function validField(field){
    if(field.name == "cnpj" || field.name == "empresaCnpj" || field.name == "cpf" || field.name == "procuradorCpf"){
        return validCnpjOrCpf(field);
    }
    if(field.name == "sitar"){
        return validSitar(field);
    }
    if(field.name == "fistel"){
        return validFistel(field);
    }
    if(field.name == "cep" || field.name == "id"){
        return numbersOnly(field);
    }
    if(field.name == "senha"){
        return validUsernameOrPassword(field);
    }
    if(field.name == "login"){
        return validUsernameOrPassword(field);
    }
    if(field.name == "date"){
        return validDate(field);
    }
    return "";
}

function isValidUsernameOrPassword(field){
    var result = "";
    result = validUsernameOrPassword(field);
    if(result.length == 0){
        return true;
    }else{
    alert(result);
    field.focus();
    return false;
}
}

function validUsernameOrPassword(field){
    var space = /[ ]/;
    var badCharacters = /[^A-Za-z0-9*]/;
    if(field.value.length > 0 & badCharacters.test(field.value)){
        return "Apenas números e letras são permitidas no campo " + field.name + ".\n";
    }else{
    return "";
}
}

function isValidFistelOrSitar(field){
    var result = "";
    if(field.name == "sitar"){
        result = validSitar(field);
    }else if(field.name == "fistel"){
    result = validFistel(field);
}
if(result.length == 0){
    return true;
}else{
alert(result);
field.focus();
return false;
}
}

function validSitar(field){
    if (field.value.length != 0 && !modulo11(field.value)) {	
        return "Número de " + field.name + " inválido.\n";
    }
    return "";
}

function validFistel(field){
    var str = field.value;
    
    if (str.length != 11 && str.length != 0){
        return "Número de " + field.name + " inválido!\n";
    }
    
    if (str != ""){
        var MaskNumber = "";
        var Lpad = "00000000000";
        for (var i = 0; i < str.length; i++){
            var chr = str.substring(i, i + 1);
            if (chr >= "0" && chr <= "9"){
                MaskNumber += chr;
            }
        }
        MaskNumber = Lpad.substring(0, 11 - MaskNumber.length) + MaskNumber;
        MaskNumber = MaskNumber.substring((MaskNumber.length - 11), MaskNumber.length);
        var SumBlock1 = 0;
        var RemBlock1 = 0;
        var SumBlock2 = 0;
        var RemBlock2 = 0;
        for (var i = 0; i < (MaskNumber.length - 2); i++){
            var chr = MaskNumber.substring(i, i + 1);
            SumBlock1 += eval(chr * (10 - i));
        }
        RemBlock1 = eval(SumBlock1 - (parseInt(SumBlock1 / 11) * 11));
        if (RemBlock1 == 0 || RemBlock1 == 1){
            RemBlock1 = 0;
        }else{
        RemBlock1 = eval(11 - RemBlock1);
    }
    for (var i = 0; i < (MaskNumber.length - 1); i++){
        var chr = MaskNumber.substring(i, i + 1);
        SumBlock2 += eval(chr * (11 - i));
    }
    RemBlock2 = eval(SumBlock2 - (parseInt(SumBlock2 / 11) * 11));
    if (RemBlock2 == 0 || RemBlock2 == 1){
        RemBlock2 = 0;
    }else{
    RemBlock2 = eval(11 - RemBlock2);
}

if (RemBlock1 == parseInt(MaskNumber.substring(9,10)) && RemBlock2 == parseInt(MaskNumber.substring(10,11))){
    if(MaskNumber != "00000000000" && MaskNumber != "11111111111" && MaskNumber != "22222222222" && MaskNumber != "33333333333" && MaskNumber != "44444444444" && MaskNumber != "55555555555" && MaskNumber != "66666666666" && MaskNumber != "77777777777" && MaskNumber != "88888888888" && MaskNumber != "99999999999") {
        return "";
    }else{
    return "Número de " + field.name + " inválido!\n";
}
}else{
return "Número de " + field.name + " inválido!\n";
}
}else{
return "";
}
}

function isValidCnpjOrCpf(field){
    if(field.value.length >= 14 || field.value.length >= 11){//CNPJ
        var result = validCnpjOrCpf(field);
        if(result.length == 0){
            return true;
        }else{
        alert(result);
        field.focus();
        return false;
    }
}else{
return true;
}
}

function validCnpjOrCpf(field){
    var result = "";
    if(field.value.length != 0){
        result += numbersOnly(field);
        if(!isCpfCnpj(field.value)){
            result += "CPF ou CNPJ inválido.\n";
        }
    }
    return result;
}

function isFieldEmpty(field){
    var result = fieldEmpty(field);
    if(result.length == 0){
        return true;
    }else{
    alert(result);
    field.focus();
    return false;
}
}

function fieldEmpty(field){
    if(field.value == ""){
        return "Campo " + field.name + " é obrigatório e não foi preenchido.\n";
    }else{
    return "";
}
}

function isNumbersOnly(field){
    var result = numbersOnly(field);
    if(result.length == 0){
        return true;
    }else{
    alert(result);
    field.focus();
    return false;
}
}

function numbersOnly(field){
    var characters = /\D/;
    field.value = field.value.replace(/[\(\)\.\-\ \/\\\,]/g, '');
    if(field.value.length > 0 & characters.test(field.value)){
        return "Preencha o campo " + field.name + " apenas com números.\n";
    }else{
    return "";
}
}

function modulo11(valor){
    var valorTemp = valor;
    var dv=0, dvComparacao,mult=1,multTemp;
    
    // recupera o dígito verificador passado
    dvComparacao = valor.substr(valor.length-1,1)
    
    // recupera o valor sem o dígito
    valorTemp = valor.substr(0, valor.length-1)
    
    for (var i=valorTemp.length-1; i>=0; i--) {
        mult==9 ? mult=2 : mult++
        dv+=parseInt(valorTemp.substr(i,1)) * mult
    }
    
    // Trata o dv
    dv = dv % 11
    dv = 11 - dv
    if (dv > 9) dv = 0;
    
    if (dv != dvComparacao)
        return false;
    else
        return true;
}

function RetornaDVModulo11(valor){
    var valorTemp = valor;
    var dv=0, mult=1,multTemp;
    
    for (var i=valorTemp.length-1; i>=0; i--) {
        mult==9 ? mult=2 : mult++
        dv+=parseInt(valorTemp.substr(i,1)) * mult
    }
    
    // Trata o dv
    dv = dv % 11
    dv = 11 - dv
    if (dv > 9) dv = 0;
    
    return dv;
}

/**
* @author Márcio d'Ávila
* @version 1.01, 2004
*
* PROTÓTIPOS:
* método String.lpad(int pSize, char pCharPad)
* método String.trim()
*
* String unformatNumber(String pNum)
* String formatCpfCnpj(String pCpfCnpj, boolean pUseSepar, boolean pIsCnpj)
* String dvCpfCnpj(String pEfetivo, boolean pIsCnpj)
* boolean isCpf(String pCpf)
* boolean isCnpj(String pCnpj)
* boolean isCpfCnpj(String pCpfCnpj)
*/


var NUM_DIGITOS_CPF  = 11;
var NUM_DIGITOS_CNPJ = 14;
var NUM_DGT_CNPJ_BASE = 8;


/**
* Adiciona método lpad() à classe String.
* Preenche a String à esquerda com o caractere fornecido,
* até que ela atinja o tamanho especificado.
*/
String.prototype.lpad = function(pSize, pCharPad)
{
    var str = this;
    var dif = pSize - str.length;
    var ch = String(pCharPad).charAt(0);
    for (; dif>0; dif--) str = ch + str;
    return (str);
} //String.lpad


/**
* Adiciona método trim() à classe String.
* Elimina brancos no início e fim da String.
*/
String.prototype.trim = function()
{
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
} //String.trim


/**
* Elimina caracteres de formatação e zeros à esquerda da string
* de número fornecida.
* @param String pNum
*      String de número fornecida para ser desformatada.
* @return String de número desformatada.
*/
function unformatNumber(pNum)
{
    return String(pNum).replace(/\D/g, "").replace(/^0+/, "");
} //unformatNumber


/**
* Formata a string fornecida como CNPJ ou CPF, adicionando zeros
* à esquerda se necessário e caracteres separadores, conforme solicitado.
* @param String pCpfCnpj
*      String fornecida para ser formatada.
* @param boolean pUseSepar
*      Indica se devem ser usados caracteres separadores (. - /).
* @param boolean pIsCnpj
*      Indica se a string fornecida é um CNPJ.
*      Caso contrário, é CPF. Default = false (CPF).
* @return String de CPF ou CNPJ devidamente formatada.
*/
function formatCpfCnpj(pCpfCnpj, pUseSepar, pIsCnpj)
{
    if (pIsCnpj==null) pIsCnpj = false;
    if (pUseSepar==null) pUseSepar = true;
    var maxDigitos = pIsCnpj? NUM_DIGITOS_CNPJ: NUM_DIGITOS_CPF;
    var numero = unformatNumber(pCpfCnpj);
    
    numero = numero.lpad(maxDigitos, '0');
    if (!pUseSepar) return numero;
    
    if (pIsCnpj)
        {
            reCnpj = /(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/;
            numero = numero.replace(reCnpj, "$1.$2.$3/$4-$5");
        }
        else
            {
                reCpf  = /(\d{3})(\d{3})(\d{3})(\d{2})$/;
                numero = numero.replace(reCpf, "$1.$2.$3-$4");
            }
            return numero;
        } //formatCpfCnpj
        
        
        /**
        * Calcula os 2 dígitos verificadores para o número-efetivo pEfetivo de
        * CNPJ (12 dígitos) ou CPF (9 dígitos) fornecido. pIsCnpj é booleano e
        * informa se o número-efetivo fornecido é CNPJ (default = false).
        * @param String pEfetivo
        *      String do número-efetivo (SEM dígitos verificadores) de CNPJ ou CPF.
        * @param boolean pIsCnpj
        *      Indica se a string fornecida é de um CNPJ.
        *      Caso contrário, é CPF. Default = false (CPF).
        * @return String com os dois dígitos verificadores.
        */
        function dvCpfCnpj(pEfetivo, pIsCnpj)
        {
            if (pIsCnpj==null) pIsCnpj = false;
            var i, j, k, soma, dv;
            var cicloPeso = pIsCnpj? NUM_DGT_CNPJ_BASE: NUM_DIGITOS_CPF;
            var maxDigitos = pIsCnpj? NUM_DIGITOS_CNPJ: NUM_DIGITOS_CPF;
            var calculado = formatCpfCnpj(pEfetivo, false, pIsCnpj);
            calculado = calculado.substring(2, maxDigitos);
            var result = "";
            
            for (j = 1; j <= 2; j++)
                {
                    k = 2;
                    soma = 0;
                    for (i = calculado.length-1; i >= 0; i--)
                        {
                            soma += (calculado.charAt(i) - '0') * k;
                            k = (k-1) % cicloPeso + 2;
                        }
                        dv = 11 - soma % 11;
                        if (dv > 9) dv = 0;
                        calculado += dv;
                        result += dv
                    }
                    
                    return result;
                } //dvCpfCnpj
                
                
                /**
                * Testa se a String pCpf fornecida é um CPF válido.
                * Qualquer formatação que não seja algarismos é desconsiderada.
                * @param String pCpf
                *      String fornecida para ser testada.
                * @return <code>true</code> se a String fornecida for um CPF válido.
                */
                function isCpf(pCpf)
                {
                    var numero = formatCpfCnpj(pCpf, false, false);
                    var base = numero.substring(0, numero.length - 2);
                    var digitos = dvCpfCnpj(base, false);
                    var algUnico, i;
                    
                    // Valida dígitos verificadores
                    if (numero != base + digitos) return false;
                    
                    /* Não serão considerados válidos os seguintes CPF:
                    * 000.000.000-00, 111.111.111-11, 222.222.222-22, 333.333.333-33, 444.444.444-44,
                    * 555.555.555-55, 666.666.666-66, 777.777.777-77, 888.888.888-88, 999.999.999-99.
                    */
                    algUnico = true;
                    for (i=1; i<NUM_DIGITOS_CPF; i++)
                        {
                            algUnico = algUnico && (numero.charAt(i-1) == numero.charAt(i));
                        }
                        return (!algUnico);
                    } //isCpf
                    
                    
                    /**
                    * Testa se a String pCnpj fornecida é um CNPJ válido.
                    * Qualquer formatação que não seja algarismos é desconsiderada.
                    * @param String pCnpj
                    *      String fornecida para ser testada.
                    * @return <code>true</code> se a String fornecida for um CNPJ válido.
                    */
                    function isCnpj(pCnpj)
                    {
                        var numero = formatCpfCnpj(pCnpj, false, true);
                        var base = numero.substring(0, NUM_DGT_CNPJ_BASE);
                        var ordem = numero.substring(NUM_DGT_CNPJ_BASE, 12);
                        var digitos = dvCpfCnpj(base + ordem, true);
                        var algUnico;
                        
                        // Valida dígitos verificadores
                        if (numero != base + ordem + digitos) return false;
                        
                        /* Não serão considerados válidos os CNPJ com os seguintes números BÁSICOS:
                        * 11.111.111, 22.222.222, 33.333.333, 44.444.444, 55.555.555,
                        * 66.666.666, 77.777.777, 88.888.888, 99.999.999.
                        */
                        algUnico = numero.charAt(0) != '0';
                        for (i=1; i<NUM_DGT_CNPJ_BASE; i++)
                            {
                                algUnico = algUnico && (numero.charAt(i-1) == numero.charAt(i));
                            }
                            if (algUnico) return false;
                            
                            /* Não será considerado válido CNPJ com número de ORDEM igual a 0000.
                            * Não será considerado válido CNPJ com número de ORDEM maior do que 0300
                            * e com as três primeiras posições do número BÁSICO com 000 (zeros).
                            * Esta crítica não será feita quando o no BÁSICO do CNPJ for igual a 00.000.000.
                            */
                            if (ordem == "0000") return false;
                            return (base == "00000000"
                                || parseInt(ordem, 10) <= 300 || base.substring(0, 3) != "000");
                        } //isCnpj
                        
                        
                        /**
                        * Testa se a String pCpfCnpj fornecida é um CPF ou CNPJ válido.
                        * Se a String tiver uma quantidade de dígitos igual ou inferior
                        * a 11, valida como CPF. Se for maior que 11, valida como CNPJ.
                        * Qualquer formatação que não seja algarismos é desconsiderada.
                        * @param String pCpfCnpj
                        *      String fornecida para ser testada.
                        * @return <code>true</code> se a String fornecida for um CPF ou CNPJ válido.
                        */
                        function isCpfCnpj(pCpfCnpj)
                        {
                            var numero = pCpfCnpj.replace(/\D/g, "");
                            if (numero.length > NUM_DIGITOS_CPF)
                                return isCnpj(pCpfCnpj)
                            else
                                return isCpf(pCpfCnpj);
                        } //isCpfCnpj
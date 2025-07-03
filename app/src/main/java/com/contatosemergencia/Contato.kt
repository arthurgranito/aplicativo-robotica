package com.contatosemergencia

import com.google.gson.annotations.SerializedName

data class Contato(
    @SerializedName("id")
    val id: String? = null,
    
    @SerializedName("nome")
    val nome: String,
    
    @SerializedName("telefone")
    val telefone: String,
    
    @SerializedName("descricao")
    val descricao: String
) 
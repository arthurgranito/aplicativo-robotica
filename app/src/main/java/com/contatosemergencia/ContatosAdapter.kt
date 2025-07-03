package com.contatosemergencia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContatosAdapter(
    private var contatos: List<Contato> = emptyList(),
    private val onExcluirClick: ((Contato) -> Unit)? = null
) : RecyclerView.Adapter<ContatosAdapter.ContatoViewHolder>() {

    class ContatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        val tvTelefone: TextView = itemView.findViewById(R.id.tvTelefone)
        val tvDescricao: TextView = itemView.findViewById(R.id.tvDescricao)
        val btnExcluir: ImageButton = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contato, parent, false)
        return ContatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val contato = contatos[position]
        holder.tvNome.text = contato.nome
        holder.tvTelefone.text = contato.telefone
        holder.tvDescricao.text = contato.descricao
        holder.btnExcluir.setOnClickListener {
            onExcluirClick?.invoke(contato)
        }
    }

    override fun getItemCount(): Int = contatos.size

    fun updateContatos(novosContatos: List<Contato>) {
        contatos = novosContatos
        notifyDataSetChanged()
    }

    fun getContatos(): List<Contato> = contatos
} 
package com.ufcg.psoft.mercadofacil.dto;

import com.ufcg.psoft.mercadofacil.model.Carrinho;

public class UsuarioDTO {
	
	private String cpf;
	private String nome;
	private String endereco;
	private String telefone;
	
	public UsuarioDTO(String cpf, String nome, String telefone, String endereco) {
		this.cpf = cpf; 
		this.nome = nome;
		this.telefone = telefone;
		this.endereco = endereco;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public String getTelefone() {
		return telefone;
	}

	@Override
	public String toString() {
		return "Usuário: " + getCpf() + " - Nome: " + getNome();
	}
}

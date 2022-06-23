package com.ufcg.psoft.mercadofacil.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.dto.UsuarioDTO;
import com.ufcg.psoft.mercadofacil.exception.UsuarioAlreadyExists;
import com.ufcg.psoft.mercadofacil.exception.UsuarioNotFoundException;
import com.ufcg.psoft.mercadofacil.model.Usuario;
import com.ufcg.psoft.mercadofacil.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository userRepo; 
	
	// Queremos apenas o CPF e o nome dos usuários
	public List<String> listUsers() {
		List<String> users = new ArrayList<String>();
		for (Usuario usuario : this.userRepo.getAll()) {
			String userList = "CPF do usuário: " + usuario.getCpf() + " - Nome do usuário: "+ usuario.getNome();
			users.add(userList); 
		}
		return users;
	}
	
	public String createUser(UsuarioDTO userDTO) throws UsuarioAlreadyExists  {
		Usuario usuario = new Usuario(userDTO.getCpf(), userDTO.getNome(), userDTO.getEndereco(), userDTO.getTelefone());
		
		if (userRepo.getUser(userDTO.getCpf())!= null) throw new UsuarioAlreadyExists("Usuário já está cadastrado!");
		
		this.userRepo.addUser(usuario);
		
		return usuario.getCpf();
	}
	
	public Usuario getUserById(String cpf) throws UsuarioNotFoundException { 
		Usuario user = this.userRepo.getUser(cpf);
		if (user == null) throw new UsuarioNotFoundException("Usuário: " + cpf + " não encontrado");
		
		return user;
	}
	
	public void editUser(String enderecoUpdate, String telefoneUpdate, Usuario usuario) throws UsuarioNotFoundException { 
		usuario.setEndereco(!enderecoUpdate.isBlank() ? enderecoUpdate : usuario.getEndereco());
		usuario.setTelefone(!telefoneUpdate.isBlank() ? telefoneUpdate : usuario.getTelefone());
		this.userRepo.editUser(usuario.getCpf(), usuario);
	}
	
	public void deletUser(String cpf) throws UsuarioNotFoundException {
		this.userRepo.delUser(cpf);
	}
	
}
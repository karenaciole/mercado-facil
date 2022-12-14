package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;

import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ufcg.psoft.mercadofacil.dto.UsuarioDTO;
import com.ufcg.psoft.mercadofacil.exception.UsuarioAlreadyExists;
import com.ufcg.psoft.mercadofacil.exception.UsuarioNotFoundException;
import com.ufcg.psoft.mercadofacil.model.Usuario;
import com.ufcg.psoft.mercadofacil.service.UsuarioService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@RequestMapping(value = "/usuario", method = RequestMethod.POST)
	public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO userDTO, UriComponentsBuilder ucBuilder) {
		String usuarioID;

		if (userDTO.getCpf().length() != 11)
			return new ResponseEntity<String>("CPF inválido: deve conter 11 dígitos", HttpStatus.BAD_REQUEST);

		if (!validaEntradaDePerfil(userDTO.getPerfil().toUpperCase().replaceAll(" ", "")))
			if (!userDTO.getPerfil().isBlank())
				return new ResponseEntity<String>("Perfil inválido: deve ser NORMAL, ESPECIAL OU PREMIUM", HttpStatus.BAD_REQUEST);

		try {
			usuarioID = usuarioService.createUser(userDTO);
		} catch (UsuarioAlreadyExists e) {
			return new ResponseEntity<String>("Usuário já existe", HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>("Usuário cadastrado: " + usuarioID, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/usuario/{cpf}", method = RequestMethod.GET)
	public ResponseEntity<?> consultarUsuarioPeloID(@PathVariable("cpf") String cpf) {
		Usuario usuario;
		try {
			usuario = usuarioService.getUserById(cpf);
		} catch (UsuarioNotFoundException e) { 
			return new ResponseEntity<String>("Usuário não encontrado", HttpStatus.NOT_FOUND);
		}


		String dadosDoCliente = "{\n\"cpf\":"+ usuario.getCpf() + ",\n \"endereco\": " + usuario.getEndereco() +
				",\n \"nome\": " + usuario.getNome() + ",\n \"perfil\": " + usuario.getPerfil() +
				",\n \"telefone\": " + usuario.getTelefone() + "\n}";


		return new ResponseEntity<String>(dadosDoCliente, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/usuarios", method = RequestMethod.GET)
	public ResponseEntity<?> listarUsuarios() {
		List<String> usuarios = usuarioService.listUsers();
		
		return new ResponseEntity<List<String>>(usuarios, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/usuario/{cpf}", method = RequestMethod.PUT)
	public ResponseEntity<?> editarUsuario(@PathVariable("cpf") String cpf, @RequestParam (required = false)
	String enderecoDTO, @RequestParam (required = false) String telefoneDTO, @RequestParam (required = false)
	String perfilDTO, UriComponentsBuilder ucBuilder) {

		Usuario usuario;
		try {
			usuario = usuarioService.getUserById(cpf);

			if(perfilDTO != null)
				if (!validaEntradaDePerfil(perfilDTO.toUpperCase().replaceAll(" ", "")))
					return new ResponseEntity<String>("Perfil inválido: deve ser NORMAL, ESPECIAL OU PREMIUM", HttpStatus.BAD_REQUEST);

			UsuarioDTO usuarioDTO = new UsuarioDTO(cpf, usuario.getNome(), telefoneDTO, enderecoDTO, perfilDTO);
			usuarioService.editUser(usuarioDTO, usuario);
		} catch (UsuarioNotFoundException e) {
			return new ResponseEntity<String>("Usuário não encontrado", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Usuário atualizado\n" + usuario, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/usuario/{cpf}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletarUsuario(@PathVariable("cpf") String cpf) {
		try {
			this.usuarioService.deletUser(cpf);
		} catch (UsuarioNotFoundException e) { 
			return new ResponseEntity<String>("Usuário não encontrado", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Usuário deletado", HttpStatus.OK);
	}

	private boolean validaEntradaDePerfil(String perfil) {
		return perfil.equals(("NORMAL")) ||
				perfil.equals(("ESPECIAL")) ||
				perfil.equals(("PREMIUM"));
	}
	
}

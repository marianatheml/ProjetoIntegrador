package com.projetointegrador.cultivar.service;

import java.nio.charset.Charset;

import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.projetointegrador.cultivar.dtos.UserLoginDTO;
import com.projetointegrador.cultivar.model.Usuario;
import com.projetointegrador.cultivar.repository.UsuarioRepository;

/**
 * 
 * @author pedro
 * @author marianatheml
 * @version 1.1
 *
 */

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Usuario CadastrarUsuario(Usuario usuario) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);

		return repository.save(usuario);

	}

	public Optional<UserLoginDTO> Logar(Optional<UserLoginDTO> user) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());

		if (usuario.isPresent()) {
			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
				String auth = user.get().getUsuario() + ":" + user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);

				user.get().setToken(authHeader);
				user.get().setUsuario(usuario.get().getUsuario());

				return user;
			}
		}
		return null;
	}

}

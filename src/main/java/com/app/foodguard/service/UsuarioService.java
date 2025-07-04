package com.app.foodguard.service;

import com.app.foodguard.model.Usuario;
import com.app.foodguard.repository.UsuarioRepository;
import lombok.Getter;

public class UsuarioService {
    private final UsuarioRepository repository;
    @Getter
    private Usuario usuario;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
        this.usuario = repository.load();
    }

    public void saveUsuario(Usuario usuario) {
        // Define ID fixo já que só teremos um usuário
        usuario.setId(1);
        this.usuario = usuario;
        repository.save(usuario);
    }
}
package com.app.foodguard.repository;

import com.app.foodguard.model.Usuario;
import com.app.foodguard.utils.CsvUtil;

import java.util.Collections;
import java.util.List;

public class UsuarioRepository {

    private static final String FILE_PATH = "src/main/resources/csv/usuario.csv";

    public void save(Usuario usuario) {
        // Como só temos um usuário, salvamos uma lista com um único elemento
        CsvUtil.save(Collections.singletonList(usuario), FILE_PATH);
    }

    public Usuario load() {
        List<Usuario> usuarios = CsvUtil.load(FILE_PATH, Usuario.class);
        // Retorna o primeiro usuário ou null se a lista estiver vazia
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
}
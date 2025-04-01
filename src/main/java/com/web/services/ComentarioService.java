package com.web.services;

import com.web.models.Comentario;

import java.util.List;
import java.util.UUID;

public interface ComentarioService {
    Comentario criarComentario(Comentario comentario);

    Comentario buscarComentarioPorId(UUID id);

    List<Comentario> listarTodosComentarios();

    List<Comentario> listarComentariosPorPost(UUID postId); // Adicione esta linha

    Comentario atualizarComentario(UUID id, Comentario comentario);

    void deletarComentario(UUID id);
}

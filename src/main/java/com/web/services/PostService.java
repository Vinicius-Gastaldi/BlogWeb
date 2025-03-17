package com.web.services;

import com.web.models.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post criarPost(Post post);

    Post buscarPostPorId(UUID id);

    List<Post> listarTodosPosts();

    Post atualizarPost(UUID id, Post post);

    void deletarPost(UUID id);
}

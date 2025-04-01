package com.web.converters.impls;

import com.web.converters.Converter;
import com.web.dtos.ComentarioDTO;
import com.web.models.Comentario;
import com.web.models.Post;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ComentarioConverter implements Converter<Comentario, ComentarioDTO> {

    @Override
    public ComentarioDTO toDTO(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getId(),
                comentario.getData(),
                comentario.getComentario(),
                getPostId(comentario.getPost())
        );
    }

    @Override
    public Comentario toEntity(ComentarioDTO comentarioDTO) {
        Comentario comentario = new Comentario();
        comentario.setId(comentarioDTO.getId()); // Usando getter
        comentario.setComentario(comentarioDTO.getComentario()); // Usando getter
        comentario.setPost(createPostReference(comentarioDTO.getPostId())); // Usando getter
        return comentario;
    }


    // ========== Métodos Auxiliares ==========
    private UUID getPostId(Post post) {
        return (post != null) ? post.getId() : null;
    }

    private Post createPostReference(UUID postId) {
        if (postId == null) return null;
        Post post = new Post();
        post.setId(postId);
        return post;
    }
}
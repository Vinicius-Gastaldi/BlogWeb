package com.web.converters.impls;

import com.web.converters.Converter;
import com.web.dtos.ComentarioDTO;
import com.web.models.Comentario;
import com.web.models.Post;
import com.web.repositories.PostRepository;
import org.springframework.stereotype.Component;

@Component
public class ComentarioConverter implements Converter<Comentario, ComentarioDTO> {

    private final PostRepository postRepository;

    public ComentarioConverter(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public ComentarioDTO toDTO(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getId(),
                comentario.getData(),
                comentario.getComentario(),
                comentario.getPost() != null ? comentario.getPost().getId() : null
        );
    }

    @Override
    public Comentario toEntity(ComentarioDTO comentarioDTO) {
        Comentario comentario = new Comentario();
        comentario.setId(comentarioDTO.id());
        comentario.setComentario(comentarioDTO.comentario());

        if (comentarioDTO.postId() != null) {
            Post post = postRepository.findById(comentarioDTO.postId())
                    .orElseThrow(() -> new RuntimeException("Post não encontrado"));
            comentario.setPost(post);
        }

        return comentario;
    }
}
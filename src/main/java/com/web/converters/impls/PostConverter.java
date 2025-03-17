package com.web.converters.impls;

import com.web.converters.Converter;
import com.web.dtos.PostDTO;
import com.web.models.Post;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PostConverter implements Converter<Post, PostDTO> {

    private final ComentarioConverter comentarioConverter;

    public PostConverter(ComentarioConverter comentarioConverter) {
        this.comentarioConverter = comentarioConverter;
    }

    @Override
    public PostDTO toDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getAutor(),
                post.getData(),
                post.getTitulo(),
                post.getTexto(),
                post.getComentarios().stream()
                        .map(comentarioConverter::toDTO)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Post toEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.id());
        post.setAutor(postDTO.autor());
        post.setTitulo(postDTO.titulo());
        post.setTexto(postDTO.texto());

        return post;
    }
}
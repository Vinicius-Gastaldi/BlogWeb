package com.web.controllers;

import com.web.dtos.PostDTO;
import com.web.models.Comentario;
import com.web.models.Post;
import com.web.services.PostService;
import com.web.converters.impls.PostConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.web.services.ComentarioService; // Importe a interface

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final ComentarioService comentarioService; // Declare o serviço
    private final PostConverter postConverter;

    public PostController(PostService postService,ComentarioService comentarioService, PostConverter postConverter) {
        this.postService = postService;
        this.comentarioService = comentarioService; // Injeção do ComentarioService
        this.postConverter = postConverter;
    }

    // ========== FORMULÁRIOS ==========
    @GetMapping("/novo")
    public ModelAndView exibirFormularioCriacao() {
        ModelAndView model = new ModelAndView("posts/formulario");
        model.addObject("postDTO", new PostDTO());
        return model;
    }

    @GetMapping("/{id}/editar")
    public ModelAndView exibirFormularioEdicao(@PathVariable UUID id) {
        Post post = postService.buscarPostPorId(id);
        ModelAndView model = new ModelAndView("posts/formulario");
        model.addObject("postDTO", postConverter.toDTO(post));
        return model;
    }

    // ========== CRUD ==========
    @PostMapping
    public ModelAndView criarPost(@ModelAttribute PostDTO postDTO) {
        Post post = postConverter.toEntity(postDTO);
        Post postSalvo = postService.criarPost(post);
        return new ModelAndView("redirect:/posts/" + postSalvo.getId());
    }

    @GetMapping("/{id}")
    public ModelAndView mostrarPostDetalhes(@PathVariable UUID id) {
        Post post = postService.buscarPostPorId(id);
        List<Comentario> comentarios = comentarioService.listarComentariosPorPost(id); // Busca comentários do post
        ModelAndView model = new ModelAndView("posts/detalhes");
        model.addObject("post", post);
        model.addObject("comentarios", comentarios); // Passa a lista para a view
        return model;
    }

    @GetMapping
    public ModelAndView listarTodosPosts() {
        List<PostDTO> posts = postService.listarTodosPosts()
                .stream()
                .map(postConverter::toDTO)
                .collect(Collectors.toList());

        ModelAndView model = new ModelAndView("posts/lista");
        model.addObject("posts", posts);
        return model;
    }

    @PostMapping("/{id}/atualizar")
    public ModelAndView atualizarPost(
            @PathVariable UUID id,
            @ModelAttribute PostDTO postDTO
    ) {
        Post post = postConverter.toEntity(postDTO);
        postService.atualizarPost(id, post);
        return new ModelAndView("redirect:/posts/" + id);
    }

    @PostMapping("/{id}/excluir")
    public ModelAndView deletarPost(@PathVariable UUID id) {
        postService.deletarPost(id);
        return new ModelAndView("redirect:/posts");
    }
}
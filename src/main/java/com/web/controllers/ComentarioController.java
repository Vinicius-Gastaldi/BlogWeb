package com.web.controllers;

import com.web.dtos.ComentarioDTO;
import com.web.models.Comentario;
import com.web.services.ComentarioService;
import com.web.converters.impls.ComentarioConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final ComentarioConverter comentarioConverter;

    public ComentarioController(ComentarioService comentarioService,
                                ComentarioConverter comentarioConverter) {
        this.comentarioService = comentarioService;
        this.comentarioConverter = comentarioConverter;
    }

    // ========== FORMULÁRIOS ==========

    // FORMULÁRIO PARA NOVO COMENTÁRIO
    @GetMapping("/novo")
    public ModelAndView exibirFormularioNovo(@RequestParam UUID postId) {
        ModelAndView model = new ModelAndView("comentarios/formulario");
        ComentarioDTO dto = new ComentarioDTO();
        dto.setPostId(postId); // Set only postId for new comment
        model.addObject("comentarioDTO", dto);
        model.addObject("editMode", false);
        return model;
    }

    // FORMULÁRIO PARA EDITAR COMENTÁRIO (NOVO ENDPOINT)
    @GetMapping("/{id}/editar")
    public ModelAndView exibirFormularioEdicao(@PathVariable UUID id) {
        try {
            Comentario comentario = comentarioService.buscarComentarioPorId(id);
            ComentarioDTO dto = comentarioConverter.toDTO(comentario);

            ModelAndView model = new ModelAndView("comentarios/formulario");
            model.addObject("comentarioDTO", dto);
            model.addObject("editMode", true);
            return model;
        } catch (EntityNotFoundException e) {
            return new ModelAndView("redirect:/posts");
        }
    }


    // ========== CRUD ==========
    @PostMapping // Handles BOTH Create and Update now based on hidden field or logic
    public ModelAndView salvarComentario(@ModelAttribute ComentarioDTO comentarioDTO) {
        Comentario comentario = comentarioConverter.toEntity(comentarioDTO);
        if (comentarioDTO.getId() != null) {
            // Faz os updates dos posts existentes
            comentarioService.atualizarComentario(comentarioDTO.getId(), comentario);
        } else {
            comentario = comentarioService.criarComentario(comentario);
        }
        return new ModelAndView("redirect:/posts/" + comentarioDTO.getPostId());
    }
    @GetMapping("/{id}")
    public ModelAndView buscarComentarioPorId(@PathVariable UUID id) {
        try {
            Comentario comentario = comentarioService.buscarComentarioPorId(id);
            ModelAndView model = new ModelAndView("comentarios/detalhes");
            model.addObject("comentario", comentarioConverter.toDTO(comentario));
            // Add postId if needed by the view
            model.addObject("postId", comentario.getPost().getId());
            return model;
        } catch (EntityNotFoundException e) {
            return new ModelAndView("redirect:/posts"); // Or error view
        }
    }
    @GetMapping("/todos")
    public ModelAndView listarTodosComentarios() {
        List<ComentarioDTO> comentarios = comentarioService.listarTodosComentarios()
                .stream()
                .map(comentarioConverter::toDTO)
                .collect(Collectors.toList());

        ModelAndView model = new ModelAndView("comentarios/lista_todos"); // Ainda não implementado, mas é pra poder ver todos os comentarios sem ligar pra post
        model.addObject("comentarios", comentarios);
        return model;
    }
    @GetMapping("/post/{postId}")
    public ModelAndView listarComentariosPorPost(@PathVariable UUID postId) {
        List<ComentarioDTO> comentarios = comentarioService.listarComentariosPorPost(postId)
                .stream()
                .map(comentarioConverter::toDTO)
                .collect(Collectors.toList());

        ModelAndView model = new ModelAndView("comentarios/lista");
        model.addObject("comentarios", comentarios);
        model.addObject("postId", postId);
        return model;
    }

    // DELETE Comment
    @PostMapping("/{id}/excluir")
    public ModelAndView deletarComentario(@PathVariable UUID id) {
        UUID postId;
        try {
            Comentario comentario = comentarioService.buscarComentarioPorId(id);
            postId = comentario.getPost().getId(); // Recupera o ID do post associado
            comentarioService.deletarComentario(id);
        } catch (EntityNotFoundException e) {
            return new ModelAndView("redirect:/posts");
        }
        return new ModelAndView("redirect:/posts/" + postId);
    }
}
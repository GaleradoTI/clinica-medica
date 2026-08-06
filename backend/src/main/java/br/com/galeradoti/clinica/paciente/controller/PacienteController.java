package br.com.galeradoti.clinica.paciente.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.galeradoti.clinica.paciente.dto.PacienteCreateRequest;
import br.com.galeradoti.clinica.paciente.dto.PacienteResponse;
import br.com.galeradoti.clinica.paciente.dto.PacienteSummaryResponse;
import br.com.galeradoti.clinica.paciente.dto.PacienteUpdateRequest;
import br.com.galeradoti.clinica.paciente.service.PacienteService;
import br.com.galeradoti.clinica.shared.response.PageResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private static final int MAX_PAGE_SIZE = 100;

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'RECEPCIONISTA', 'MEDICO')"
    )
    public PageResponse<PacienteSummaryResponse> listar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) String telefone,
        @RequestParam(required = false) Boolean ativo,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "nomeCompleto") String sort,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        int pageSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int pageNumber = Math.max(page, 0);

        Sort.Direction sortDirection =
            "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sorting = Sort.by(sortDirection, validarOrdenacao(sort));

        Pageable pageable = PageRequest.of(
            pageNumber,
            pageSize,
            sorting
        );

        return pacienteService.listar(
            nome,
            cpf,
            telefone,
            ativo,
            pageable
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'RECEPCIONISTA', 'MEDICO')"
    )
    public PacienteResponse buscarPorId(
        @PathVariable Long id
    ) {
        return pacienteService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'RECEPCIONISTA')"
    )
    public ResponseEntity<PacienteResponse> cadastrar(
        @Valid @RequestBody PacienteCreateRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        PacienteResponse response = pacienteService.cadastrar(
            request,
            obterUsuarioId(jwt)
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'RECEPCIONISTA')"
    )
    public PacienteResponse atualizar(
        @PathVariable Long id,
        @Valid @RequestBody PacienteUpdateRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        return pacienteService.atualizar(
            id,
            request,
            obterUsuarioId(jwt)
        );
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'RECEPCIONISTA')"
    )
    public ResponseEntity<Void> ativar(
        @PathVariable Long id,
        @AuthenticationPrincipal Jwt jwt
    ) {
        pacienteService.ativar(id, obterUsuarioId(jwt));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'RECEPCIONISTA')"
    )
    public ResponseEntity<Void> desativar(
        @PathVariable Long id,
        @AuthenticationPrincipal Jwt jwt
    ) {
        pacienteService.desativar(id, obterUsuarioId(jwt));

        return ResponseEntity.noContent().build();
    }

    private Long obterUsuarioId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

    private String validarOrdenacao(String sort) {
        return switch (sort) {
            case "id" -> "id";
            case "cpf" -> "cpf";
            case "dataNascimento" -> "dataNascimento";
            case "criadoEm" -> "criadoEm";
            default -> "nomeCompleto";
        };
    }
}
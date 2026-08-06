package br.com.galeradoti.clinica.paciente.service;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.galeradoti.clinica.paciente.dto.EnderecoRequest;
import br.com.galeradoti.clinica.paciente.dto.PacienteCreateRequest;
import br.com.galeradoti.clinica.paciente.dto.PacienteResponse;
import br.com.galeradoti.clinica.paciente.dto.PacienteSummaryResponse;
import br.com.galeradoti.clinica.paciente.dto.PacienteUpdateRequest;
import br.com.galeradoti.clinica.paciente.entity.Paciente;
import br.com.galeradoti.clinica.paciente.repository.PacienteRepository;
import br.com.galeradoti.clinica.paciente.specification.PacienteSpecification;
import br.com.galeradoti.clinica.shared.exception.BusinessException;
import br.com.galeradoti.clinica.shared.exception.ResourceConflictException;
import br.com.galeradoti.clinica.shared.exception.ResourceNotFoundException;
import br.com.galeradoti.clinica.shared.response.PageResponse;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<PacienteSummaryResponse> listar(
        String nome,
        String cpf,
        String telefone,
        Boolean ativo,
        Pageable pageable
    ) {
        Page<Paciente> pacientes = pacienteRepository.findAll(
            PacienteSpecification.comFiltros(
                nome,
                cpf,
                telefone,
                ativo
            ),
            pageable
        );

        return PageResponse.from(
            pacientes,
            PacienteSummaryResponse::from
        );
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return PacienteResponse.from(buscarEntidade(id));
    }

    @Transactional
    public PacienteResponse cadastrar(
        PacienteCreateRequest request,
        Long usuarioId
    ) {
        String cpf = normalizarNumeros(request.cpf());

        if (pacienteRepository.existsByCpf(cpf)) {
            throw new ResourceConflictException(
                "Já existe um paciente cadastrado com esse CPF."
            );
        }

        EnderecoRequest endereco = request.endereco();

        Paciente paciente = new Paciente(
            normalizarTexto(request.nomeCompleto()),
            cpf,
            request.dataNascimento(),
            request.sexo(),
            normalizarNumeros(request.telefone()),
            normalizarNumeros(request.telefoneSecundario()),
            normalizarEmail(request.email()),
            normalizarTexto(request.nomeMae()),
            endereco == null
                ? null
                : normalizarNumeros(endereco.cep()),
            endereco == null
                ? null
                : normalizarTexto(endereco.logradouro()),
            endereco == null
                ? null
                : normalizarTexto(endereco.numero()),
            endereco == null
                ? null
                : normalizarTexto(endereco.complemento()),
            endereco == null
                ? null
                : normalizarTexto(endereco.bairro()),
            endereco == null
                ? null
                : normalizarTexto(endereco.cidade()),
            endereco == null
                ? null
                : normalizarEstado(endereco.estado()),
            normalizarTexto(request.observacoes()),
            usuarioId
        );

        return PacienteResponse.from(
            pacienteRepository.save(paciente)
        );
    }

    @Transactional
    public PacienteResponse atualizar(
        Long id,
        PacienteUpdateRequest request,
        Long usuarioId
    ) {
        Paciente paciente = buscarEntidade(id);
        String cpf = normalizarNumeros(request.cpf());

        if (pacienteRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new ResourceConflictException(
                "Já existe outro paciente cadastrado com esse CPF."
            );
        }

        EnderecoRequest endereco = request.endereco();

        paciente.atualizar(
            normalizarTexto(request.nomeCompleto()),
            cpf,
            request.dataNascimento(),
            request.sexo(),
            normalizarNumeros(request.telefone()),
            normalizarNumeros(request.telefoneSecundario()),
            normalizarEmail(request.email()),
            normalizarTexto(request.nomeMae()),
            endereco == null
                ? null
                : normalizarNumeros(endereco.cep()),
            endereco == null
                ? null
                : normalizarTexto(endereco.logradouro()),
            endereco == null
                ? null
                : normalizarTexto(endereco.numero()),
            endereco == null
                ? null
                : normalizarTexto(endereco.complemento()),
            endereco == null
                ? null
                : normalizarTexto(endereco.bairro()),
            endereco == null
                ? null
                : normalizarTexto(endereco.cidade()),
            endereco == null
                ? null
                : normalizarEstado(endereco.estado()),
            normalizarTexto(request.observacoes()),
            usuarioId
        );

        return PacienteResponse.from(paciente);
    }

    @Transactional
    public void ativar(Long id, Long usuarioId) {
        Paciente paciente = buscarEntidade(id);

        if (paciente.estaAtivo()) {
            throw new BusinessException(
                "O paciente já está ativo."
            );
        }

        paciente.ativar(usuarioId);
    }

    @Transactional
    public void desativar(Long id, Long usuarioId) {
        Paciente paciente = buscarEntidade(id);

        if (!paciente.estaAtivo()) {
            throw new BusinessException(
                "O paciente já está inativo."
            );
        }

        /*
         * A validação de agendamentos futuros será adicionada
         * quando o módulo de agendamentos possuir Repository.
         */
        paciente.desativar(usuarioId);
    }

    private Paciente buscarEntidade(Long id) {
        return pacienteRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Paciente não encontrado."
                )
            );
    }

    private String normalizarNumeros(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.replaceAll("\\D", "");
    }

    private String normalizarEmail(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value
            .trim()
            .toLowerCase(Locale.ROOT);
    }

    private String normalizarEstado(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value
            .trim()
            .toUpperCase(Locale.ROOT);
    }

    private String normalizarTexto(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
package br.com.galeradoti.clinica.paciente.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.galeradoti.clinica.paciente.entity.Paciente;

public interface PacienteRepository
    extends
        JpaRepository<Paciente, Long>,
        JpaSpecificationExecutor<Paciente> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    Optional<Paciente> findByCpf(String cpf);
}
package br.com.galeradoti.clinica.paciente.specification;

import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import br.com.galeradoti.clinica.paciente.entity.Paciente;

public final class PacienteSpecification {

    private PacienteSpecification() {
    }

    public static Specification<Paciente> comFiltros(
        String nome,
        String cpf,
        String telefone,
        Boolean ativo
    ) {
        Specification<Paciente> specification =
            (root, query, builder) -> builder.conjunction();

        if (nome != null && !nome.isBlank()) {
            String termo = "%" + nome
                .trim()
                .toLowerCase(Locale.ROOT) + "%";

            specification = specification.and(
                (root, query, builder) ->
                    builder.like(
                        builder.lower(root.get("nomeCompleto")),
                        termo
                    )
            );
        }

        if (cpf != null && !cpf.isBlank()) {
            String cpfNormalizado = cpf.replaceAll("\\D", "");

            specification = specification.and(
                (root, query, builder) ->
                    builder.equal(root.get("cpf"), cpfNormalizado)
            );
        }

        if (telefone != null && !telefone.isBlank()) {
            String telefoneNormalizado =
                telefone.replaceAll("\\D", "");

            specification = specification.and(
                (root, query, builder) ->
                    builder.like(
                        root.get("telefone"),
                        "%" + telefoneNormalizado + "%"
                    )
            );
        }

        if (ativo != null) {
            specification = specification.and(
                (root, query, builder) ->
                    builder.equal(root.get("ativo"), ativo)
            );
        }

        return specification;
    }
}
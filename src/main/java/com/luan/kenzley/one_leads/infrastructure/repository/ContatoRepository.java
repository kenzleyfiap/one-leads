package com.luan.kenzley.one_leads.infrastructure.repository;

import com.luan.kenzley.one_leads.domain.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
    Optional<Contato> findByEmail(String email);

    List<Contato> findByNomeContainingIgnoreCase(String nome);

    List<Contato> findByEmailContainingIgnoreCase(String email);

    List<Contato> findByEmpresasIsEmpty();
}

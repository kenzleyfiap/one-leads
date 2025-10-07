package com.luan.kenzley.one_leads.repository;

import com.luan.kenzley.one_leads.domain.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
    Optional<Contato> findByEmail(String email);
}

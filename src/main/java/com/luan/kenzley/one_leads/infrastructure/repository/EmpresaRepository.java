package com.luan.kenzley.one_leads.infrastructure.repository;

import com.luan.kenzley.one_leads.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCnpj(String cnpj);

    List<Empresa> findByNomeContainingIgnoreCase(String nome);
    List<Empresa> findByCnpjContaining(String cnpj);
}
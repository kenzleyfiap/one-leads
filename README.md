# one-leads

🧱 Arquitetura Geral
- Backend: Java 17 + Spring Boot
- Persistência: Spring Data JPA + PostgreSQL
- Documentação: Swagger (Springdoc OpenAPI)
- Containerização: Docker + Docker Compose


🧩 Modelagem de Domínio
🔗 Relacionamento: N:N entre Empresa e Contat

```
@Entity
public class Empresa {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String nome;

    @ManyToMany
    @JoinTable(
        name = "empresa_contato",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "contato_id")
    )
    private Set<Contato> contatos = new HashSet<>();
}

```

```
@Entity
public class Contato {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefone;
    private String observacoes;

    @ManyToMany(mappedBy = "contatos")
    private Set<Empresa> empresas = new HashSet<>();
}
```
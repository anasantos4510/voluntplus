# Arquitetura do backend Voluntá+

Monólito modular com entrada REST/Spring Security e três módulos de negócio. As URLs e tabelas existentes foram mantidas.

| Módulo | Responsabilidade | Interface pública para os demais |
| --- | --- | --- |
| `users` | Conta, tipo de pessoa, papel ativo e perfil | `UserAccess` |
| `volunteerservices` | Catálogo e ciclo de vida dos serviços, autorização pelo dono | `ServiceCatalog` |
| `reviews` | Comentários e notas, autorização de beneficiário | `ReviewCatalog` |

`adapters.in.web.ServiceController` monta a resposta dos serviços com avaliações sem acoplar o módulo de serviços ao de avaliações. O módulo de avaliações consulta serviços por `ServiceCatalog`, e ambos verificam o papel por `UserAccess`. `users.adapter.in.web.UserController` delega para `UserAccounts`.

Os casos de uso dependem de portas de saída (`UserAccountStore`, `ServiceListingStore`, `ReviewStore`, `ImageStorage`, `AuthenticatedIdentityProvider`). Implementações de infraestrutura usam Spring Data/JPA, JWT do Clerk e disco local. O tipo de pessoa e o papel ativo permanecem no PostgreSQL; a troca de papel da pessoa física não apaga serviços nem avaliações. Organização fica restrita a ofertante.

## Imagens locais

O serviço recebe `providerImage` como URL externa ou data URL PNG, JPEG ou WebP de até 5 MB. Data URLs são gravadas em `APP_IMAGE_DIR` (padrão `./data/images`) e passam a ser servidas em `/api/service-images/{filename}`. Configure `APP_PUBLIC_BASE_URL` com a URL pública do backend quando não for `http://localhost:8080`. Preserve a pasta de imagens entre reinícios e inclua-a no backup junto do PostgreSQL. Imagens antigas armazenadas no banco continuam legíveis.

## Dependência de framework ainda presente

As entidades de domínio atuais usam anotações JPA para manter o esquema e os dados existentes sem migração. As regras de negócio já não dependem dos repositórios JPA concretos. Uma separação estrita entre entidades de domínio e modelos de persistência exigirá mapeadores e uma migração própria, antes de remover essas anotações.

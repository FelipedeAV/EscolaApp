# Arquitetura do Projeto EscolaApp

## Estrutura Híbrida por Features

O projeto foi refatorado para seguir uma arquitetura híbrida que combina organização por features com camadas compartilhadas.

```
com.escolaapp/
├── core/                          # Infraestrutura e código compartilhado entre features
│   ├── data/
│   │   ├── local/                 # Persistência local (futuro)
│   │   ├── mapper/                # Mapeadores DTO → Domain
│   │   ├── models/                # DTOs da API
│   │   ├── remote/
│   │   │   └── gateway/          # ApiClient e ApiGateway
│   │   └── repository/           # Repositórios compartilhados (Student, User)
│   ├── domain/
│   │   └── model/                # Modelos de domínio compartilhados
│   ├── navigation/               # Sistema de navegação global
│   └── utils/                    # Utilitários compartilhados
│
├── features/                     # Features organizadas por domínio de negócio
│   ├── auth/                    # Feature de autenticação
│   │   ├── data/
│   │   │   └── repository/      # AuthRepository
│   │   ├── domain/
│   │   │   └── model/          # Modelos específicos de auth
│   │   └── presentation/
│   │       └── login/          # LoginScreen, LoginViewModel
│   │
│   ├── teacher/                # Feature do professor
│   │   ├── data/
│   │   │   └── repository/    # ClassRepository, GradeBookRepository, etc
│   │   ├── domain/
│   │   │   └── model/        # Modelos específicos de teacher
│   │   └── presentation/
│   │       ├── attendance/   # AttendanceCallScreen
│   │       ├── classlist/    # ClassListScreen, ClassListMode
│   │       ├── dashboard/    # TeacherDashboardScreen
│   │       └── gradebook/    # GradeBookScreen
│   │
│   └── guardian/              # Feature do responsável (guardian)
│       ├── data/
│       │   └── repository/   # AttendanceRepository, GradeRepository, etc
│       ├── domain/
│       │   └── model/       # Modelos específicos de guardian
│       └── presentation/
│           ├── attendance/  # AttendanceScreen
│           ├── dashboard/   # DashboardScreen
│           ├── grades/      # GradesScreen
│           └── notices/     # NoticesScreen
│
└── shared/                   # Componentes e telas compartilhadas entre features
    ├── components/          # Componentes UI reutilizáveis
    │                       # AppHeader, AppActionButton, TeacherActionCard, etc
    └── presentation/
        └── profile/        # ProfileScreen (usado por teacher e guardian)

```

## Princípios da Arquitetura

### 1. **Core**
- Contém infraestrutura técnica e código compartilhado
- Navigation global
- Data layer comum (gateway, mappers, DTOs)
- Domain models que são usados por múltiplas features
- Utilitários gerais

### 2. **Features**
Cada feature representa um domínio de negócio específico:
- **auth**: Login e autenticação
- **teacher**: Funcionalidades do professor (chamada, notas, turmas)
- **guardian**: Funcionalidades do responsável (visualizar notas, frequência, avisos)

Cada feature pode ter suas próprias:
- `data/repository`: Repositórios específicos
- `domain/model`: Modelos de negócio específicos
- `presentation`: Screens e ViewModels

### 3. **Shared**
- Componentes UI reutilizáveis entre features
- Telas compartilhadas (ex: ProfileScreen usado por teacher e guardian)

## Fluxo de Dependências

```
presentation (UI) 
    ↓
domain (Business Logic)
    ↓
data (Data Sources)
    ↓
core (Infrastructure)
```

**Regra**: Features NÃO podem depender de outras features, apenas de `core` e `shared`.

## Benefícios

1. **Modularidade**: Cada feature pode ser desenvolvida independentemente
2. **Escalabilidade**: Fácil adicionar novas features (ex: coordinator)
3. **Manutenibilidade**: Mudanças em uma feature não afetam outras
4. **Testabilidade**: Features isoladas facilitam testes
5. **Navegabilidade**: Estrutura clara e organizada por domínio

## Próximos Passos

- [ ] Adicionar feature `coordinator` quando necessário
- [ ] Mover mais modelos compartilhados para core conforme necessário
- [ ] Criar testes unitários por feature
- [ ] Documentar cada feature individualmente


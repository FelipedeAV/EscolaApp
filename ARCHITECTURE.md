# Arquitetura do Projeto EscolaApp

## Estrutura Híbrida por Features

O projeto segue uma arquitetura híbrida que combina organização por features (domínios de negócio) com camadas compartilhadas, seguindo os princípios de Clean Architecture.

```
com.escolaapp/
├── core/                          # Infraestrutura e código compartilhado entre features
│   ├── data/
│   │   ├── local/                 # Persistência local (futuro)
│   │   ├── mapper/                # Mapeadores DTO → Domain
│   │   ├── models/                # DTOs da API
│   │   ├── remote/
│   │   │   └── gateway/          # ApiClient e ApiGateway (HTTP client)
│   │   └── repository/           # Repositórios compartilhados (Notice, Student, User)
│   ├── domain/
│   │   └── model/                # Modelos de domínio compartilhados
│   ├── navigation/               # Sistema de navegação global
│   │   ├── NavigationEvent.kt   # Eventos de navegação
│   │   ├── NavigationHandler.kt # Orquestrador de rotas
│   │   └── NavigationViewModel.kt
│   └── utils/                    # Utilitários compartilhados
│       ├── DateUtils.kt         # Utilitários de data (expect/actual)
│       └── NumberUtils.kt       # Utilitários numéricos
│
├── features/                     # Features organizadas por domínio de negócio
│   ├── auth/                    # Feature de autenticação
│   │   ├── data/
│   │   │   ├── mapper/          # Mapeadores específicos
│   │   │   └── repository/      # AuthRepository
│   │   ├── domain/
│   │   │   └── model/          # LoginRequest, LoginResponse
│   │   └── presentation/
│   │       └── login/          # LoginScreen, LoginViewModel
│   │
│   ├── coordinator/            # Feature do coordenador pedagógico
│   │   ├── data/
│   │   │   ├── mapper/        # Mapeadores de DTOs
│   │   │   ├── model/         # DTOs específicos
│   │   │   └── repository/   # CoordinatorRepository
│   │   ├── domain/
│   │   │   └── model/        # CoordinatorDashboard, QuickAction, etc
│   │   └── presentation/
│   │       └── dashboard/    # CoordinatorDashboardScreen, ViewModel
│   │
│   ├── teacher/                # Feature do professor
│   │   ├── data/
│   │   │   ├── mapper/        # Mapeadores específicos
│   │   │   ├── model/         # DTOs específicos
│   │   │   └── repository/   # ClassRepository, GradeBookRepository,
│   │   │                     # AttendanceSummaryRepository
│   │   ├── domain/
│   │   │   └── model/        # ClassInfo, ClassListMode, TeacherClass,
│   │   │                     # AttendanceSummary, GradeSummary, etc
│   │   └── presentation/
│   │       ├── addattendance/ # AddAttendanceScreen + ViewModel
│   │       ├── attendance/    # AttendanceCallScreen + ViewModel
│   │       ├── classlist/     # ClassListScreen + ViewModel
│   │       ├── components/    # TeacherNavigationBar, TeacherActionCard
│   │       ├── dashboard/     # TeacherDashboardScreen + ViewModel
│   │       ├── grade/         # AddGradeScreen + ViewModel
│   │       ├── gradebook/     # GradeBookScreen + ViewModel
│   │       └── notice/        # AddNoticeScreen + ViewModel
│   │
│   └── guardian/              # Feature do responsável (guardian)
│       ├── data/
│       │   ├── mapper/       # Mapeadores específicos
│       │   └── repository/  # AttendanceRepository, GradeRepository
│       ├── domain/
│       │   └── model/       # Attendance, Grade, Notice, Student
│       └── presentation/
│           ├── attendance/  # AttendanceScreen + ViewModel
│           ├── dashboard/   # DashboardScreen + ViewModel
│           ├── grades/      # GradesScreen + ViewModel
│           └── notices/     # NoticesScreen + ViewModel
│
└── shared/                   # Componentes e telas compartilhadas entre features
    ├── components/          # Componentes UI reutilizáveis
    │   ├── AppActionButton.kt    # Botão de ação padrão
    │   ├── AppHeader.kt          # Cabeçalho padrão
    │   └── AppTopBar.kt          # Top bar padrão
    └── presentation/
        └── profile/        # ProfileScreen + ViewModel (multi-role)
                           # ProfileSettingsScreen + ViewModel

```

## Princípios da Arquitetura

### 1. **Core** (Infraestrutura Compartilhada)
Contém código técnico e infraestrutura que todas as features podem utilizar:

- **data/**: Camada de dados compartilhada
  - `gateway/`: Cliente HTTP (ApiClient) e configurações de rede
  - `models/`: DTOs (Data Transfer Objects) usados em múltiplas features
  - `mapper/`: Conversores de DTO para modelos de domínio
  - `repository/`: Repositórios para entidades compartilhadas (Notice, Student, User)

- **domain/**: Lógica de negócio compartilhada
  - `model/`: Modelos de domínio usados por múltiplas features

- **navigation/**: Sistema de navegação centralizado
  - Gerencia rotas entre todas as features
  - Baseado em eventos (NavigationEvent)
  - Desacoplado das features

- **utils/**: Utilitários multiplataforma
  - Funções auxiliares (formatação de datas, números, etc)
  - Implementações expect/actual para KMP

### 2. **Features** (Domínios de Negócio)
Cada feature representa um contexto delimitado (Bounded Context do DDD):

#### **auth** - Autenticação
Responsável pelo fluxo de login e controle de acesso:
- Login de usuários (Teacher, Guardian, Coordinator)
- Gerenciamento de tokens JWT
- Validação de credenciais

#### **coordinator** - Coordenador Pedagógico
Gestão administrativa e acadêmica:
- Dashboard com visão geral do semestre
- Gestão de turmas, disciplinas, professores e alunos
- Ações rápidas (cadastros, aprovações)
- Estatísticas e atividades recentes

#### **teacher** - Professor
Funcionalidades do corpo docente:
- Dashboard com turmas atribuídas
- Fazer chamada (AttendanceCall)
- Lançar notas (GradeBook)
- Visualizar turmas (ClassList)
- Gerenciar avisos
- Componentes específicos (TeacherNavigationBar)

#### **guardian** - Responsável/Tutor
Acompanhamento do aluno:
- Dashboard com informações do estudante
- Visualizar notas e médias
- Visualizar frequência
- Receber avisos da escola

**Estrutura padrão de cada feature:**
```
feature/
├── data/
│   ├── mapper/       # Conversão DTO ↔ Domain
│   ├── model/        # DTOs específicos (opcional)
│   └── repository/   # Acesso a dados
├── domain/
│   └── model/        # Entidades de negócio
└── presentation/
    ├── screen1/      # Screen + ViewModel agrupados
    ├── screen2/
    └── components/   # Componentes específicos da feature
```

### 3. **Shared** (Código Compartilhado de UI)
Componentes e telas reutilizáveis entre features:

- **components/**: UI components genéricos
  - `AppActionButton`: Botão de ação padronizado
  - `AppHeader`: Cabeçalho com navegação
  - `AppTopBar`: Barra superior
  
- **presentation/profile/**: Telas de perfil
  - Usado por Teacher, Guardian e Coordinator
  - Configurações do usuário
  - Alteração de senha

## Fluxo de Dependências

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│    (Screens, ViewModels, UI)            │
│  features/*/presentation/               │
│  shared/presentation/                   │
└─────────────────┬───────────────────────┘
                  │ depende de
                  ↓
┌─────────────────────────────────────────┐
│         Domain Layer                    │
│    (Business Logic, Models)             │
│  features/*/domain/                     │
│  core/domain/                           │
└─────────────────┬───────────────────────┘
                  │ depende de
                  ↓
┌─────────────────────────────────────────┐
│         Data Layer                      │
│    (Repositories, Data Sources)         │
│  features/*/data/                       │
│  core/data/                             │
└─────────────────┬───────────────────────┘
                  │ depende de
                  ↓
┌─────────────────────────────────────────┐
│         Infrastructure                   │
│    (HTTP, Storage, Utils)               │
│  core/data/remote/gateway/              │
│  core/utils/                            │
└─────────────────────────────────────────┘
```

### Regras de Dependência:

1. **Features são independentes**: Uma feature NÃO pode depender de outra feature
2. **Core é compartilhado**: Todas as features podem depender de `core`
3. **Shared é reutilizável**: Features podem usar componentes de `shared`
4. **Unidirecional**: Dependências sempre fluem de fora para dentro (presentation → domain → data)

## Tecnologias e Padrões

### Arquitetura
- **Clean Architecture**: Separação clara de responsabilidades
- **MVVM**: ViewModels gerenciam estado da UI
- **Repository Pattern**: Abstração de acesso a dados
- **Dependency Injection**: Koin para injeção de dependências

### Kotlin Multiplatform
- **Compose Multiplatform**: UI declarativa para Android e iOS
- **expect/actual**: Implementações específicas de plataforma (DateUtils)
- **Voyager**: Navegação multiplataforma

### Bibliotecas Principais
- **Ktor Client**: Cliente HTTP multiplataforma
- **Kotlinx Serialization**: Serialização JSON
- **Koin**: Injeção de dependências
- **Compose Material3**: Design System
- **Coroutines & Flow**: Programação assíncrona e reativa

## Benefícios da Arquitetura

1. **Modularidade**
   - Cada feature pode ser desenvolvida, testada e implantada independentemente
   - Fácil onboarding de novos desenvolvedores (podem focar em uma feature)

2. **Escalabilidade**
   - Adicionar novas features é simples (ex: adicionar feature `student` no futuro)
   - Features podem crescer sem impactar outras

3. **Manutenibilidade**
   - Mudanças em uma feature não quebram outras
   - Código organizado por domínio de negócio (fácil de localizar)
   - Componentes compartilhados evitam duplicação

4. **Testabilidade**
   - Features isoladas facilitam testes unitários
   - Repositories mockáveis para testes
   - ViewModels testáveis sem dependência de UI

5. **Reusabilidade**
   - Componentes em `shared/` reutilizáveis
   - `core/` fornece infraestrutura comum
   - Reduz duplicação de código

## Estado Atual e Próximos Passos

### ✅ Implementado
- [x] Feature `auth` completa (login multi-role)
- [x] Feature `teacher` completa (dashboard, chamada, notas, turmas)
- [x] Feature `guardian` completa (dashboard, notas, frequência, avisos)
- [x] Feature `coordinator` base (dashboard com visão geral)
- [x] Sistema de navegação centralizado
- [x] Componentes compartilhados (AppHeader, AppActionButton)
- [x] Perfil de usuário multi-role
- [x] Utilitários multiplataforma (DateUtils, NumberUtils)

### 🔧 Melhorias Sugeridas

#### Organização de Código
- [x] Reorganizar `Add*Screen` da feature teacher em subpastas apropriadas ✅
  - Movido para `presentation/notice/`, `presentation/grade/`, `presentation/addattendance/`
- [ ] Criar testes unitários para cada feature
- [ ] Documentar cada feature individualmente (README por feature)

#### Novas Funcionalidades - Coordinator
- [ ] Implementar telas de gestão (classes, subjects, teachers, students)
- [ ] Implementar cadastros (novo aluno, novo professor)
- [ ] Implementar tela de notificações
- [ ] Implementar tela de configurações do coordenador

#### Melhorias de UX
- [ ] Adicionar loading states e error handling consistentes
- [ ] Implementar offline-first com persistência local
- [ ] Adicionar animações de transição entre telas

#### Infraestrutura
- [ ] Adicionar logging centralizado
- [ ] Implementar refresh token automático
- [ ] Adicionar analytics e crash reporting
- [ ] Configurar CI/CD

### 📚 Convenções de Código

#### Nomenclatura
- **Screens**: `[Feature][Purpose]Screen.kt` (ex: `TeacherDashboardScreen.kt`)
- **ViewModels**: `[Feature][Purpose]ViewModel.kt` (ex: `ClassListViewModel.kt`)
- **Repositories**: `[Entity]Repository.kt` (ex: `ClassRepository.kt`)
- **Models (Domain)**: PascalCase sem sufixo (ex: `Student.kt`, `Grade.kt`)
- **DTOs**: `[Entity]Dto.kt` ou `[Entity]Response.kt` (ex: `StudentDto.kt`)

#### Estrutura de Arquivos
- Agrupar Screen + ViewModel na mesma pasta quando possível
- Componentes específicos de feature vão em `presentation/components/`
- Componentes genéricos vão em `shared/components/`

#### Injeção de Dependências
```kotlin
// No AppModule.kt
factory { MyViewModel(get(), get()) }  // Para ViewModels
single { MyRepository(get()) }         // Para Repositories
```

#### Navegação
```kotlin
// Emitir evento de navegação no ViewModel
navigationEvents.tryEmit(NavigationEvent.ToSomeScreen(...))

// Nunca navegar diretamente da UI
// ❌ navigator.push(...)
// ✅ viewModel.navigateToSomewhere()
```

## Diagrama de Features

```
┌─────────────────────────────────────────────────────────────────┐
│                          EscolaApp                              │
└───────────────────────────┬─────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
   ┌────▼────┐        ┌─────▼─────┐      ┌─────▼──────┐
   │  Auth   │        │  Teacher   │      │  Guardian  │
   │Feature  │        │  Feature   │      │  Feature   │
   └─────────┘        └────────────┘      └────────────┘
        │                   │                   │
        │              ┌────▼─────┐            │
        │              │Coordinator│            │
        │              │ Feature   │            │
        │              └───────────┘            │
        │                                       │
        └───────────────────┬───────────────────┘
                            │
                ┌───────────▼──────────┐
                │   Shared & Core      │
                │ (Components, Utils,  │
                │  Navigation, Data)   │
                └──────────────────────┘
```

## Referências

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Voyager Navigator](https://voyager.adriel.cafe/)
- [Koin Documentation](https://insert-koin.io/)

---

**Última atualização**: Abril 2026  
**Versão**: 2.0  
**Mantenedor**: Equipe de Desenvolvimento EscolaApp


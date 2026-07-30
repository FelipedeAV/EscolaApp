# Melhorias Aplicadas ao EscolaApp

## Data: 19 de Abril de 2026

Este documento detalha todas as melhorias aplicadas ao projeto EscolaApp seguindo as melhores práticas de Kotlin Multiplatform, MVVM e Clean Architecture.

---

## 1. ✅ Serialização Global com snake_case

### Implementado em: `core/data/remote/gateway/ApiClient.kt`

**Mudanças:**
- Configurado `JsonNamingStrategy.SnakeCase` globalmente no cliente Ktor
- Adicionado `@OptIn(ExperimentalSerializationApi::class)` para usar a feature experimental
- Documentação adicionada no topo do arquivo explicando a convenção

**Código:**
```kotlin
@OptIn(ExperimentalSerializationApi::class)
private val jsonConfig = Json {
    namingStrategy = JsonNamingStrategy.SnakeCase
    ignoreUnknownKeys = true
    isLenient = true
}
```

**Benefício:**
- Não é mais necessário adicionar `@SerialName` para cada campo que segue o padrão snake_case
- DTOs agora usam camelCase em Kotlin, convertido automaticamente para snake_case na API
- Código mais limpo e menos propenso a erros

---

## 2. ✅ Enums com Serialização Explícita

### Criados em: `core/domain/model/`

**Novos arquivos:**
- `Role.kt` - Enum para papéis de usuário
- `ClassStatus.kt` - Enum para status de turmas

**Código:**
```kotlin
@Serializable
enum class Role {
    @SerialName("guardian")
    GUARDIAN,
    
    @SerialName("teacher")
    TEACHER,
    
    @SerialName("coordinator")
    COORDINATOR
}
```

**Benefício:**
- Garantia de que valores do enum são serializados exatamente como a API espera
- Type-safety em todo o código
- Fácil de estender no futuro

---

## 3. ✅ Feature Coordinator Completa

### Estrutura Implementada:

```
features/coordinator/
├── CoordinatorModule.kt          # Koin DI module
├── data/
│   ├── mapper/
│   │   └── CoordinatorMapper.kt
│   ├── model/
│   │   ├── CoordinatorDashboardResponse.kt
│   │   └── CoordinatorClassSummaryResponse.kt
│   └── repository/
│       └── CoordinatorRepository.kt
├── domain/
│   └── model/
│       ├── ActivityType.kt
│       ├── ClassStatus.kt
│       ├── CoordinatorDashboard.kt
│       ├── CoordinatorClassSummary.kt
│       ├── QuickAction.kt
│       ├── RecentActivity.kt
│       └── SemesterStats.kt
└── presentation/
    └── dashboard/
        ├── CoordinatorDashboardScreen.kt
        └── CoordinatorDashboardViewModel.kt
```

**Benefício:**
- Feature completa seguindo a arquitetura do projeto
- Pronta para ser expandida com novas funcionalidades
- Isolada das outras features

---

## 4. ✅ Módulos Koin por Feature

### Arquivos Criados:

1. **`core/CoreModule.kt`**
   - ApiClient (singleton)
   - Repositórios compartilhados (Student, User, Notice)
   - NavigationViewModel
   - ViewModels compartilhados (Profile, ProfileSettings)

2. **`features/auth/AuthModule.kt`**
   - AuthRepository
   - LoginViewModel

3. **`features/teacher/TeacherModule.kt`**
   - Repositórios: ClassRepository, AttendanceSummaryRepository, GradeBookRepository
   - ViewModels: TeacherDashboard, AttendanceCall, GradeBook, ClassList, etc.

4. **`features/guardian/GuardianModule.kt`**
   - Repositórios: AttendanceRepository, GradeRepository
   - ViewModels: Dashboard, Grades, Attendance, Notices

5. **`features/coordinator/CoordinatorModule.kt`**
   - CoordinatorRepository
   - CoordinatorDashboardViewModel

### AppModule.kt Refatorado:

```kotlin
val appModule = listOf(
    coreModule,
    authModule,
    teacherModule,
    guardianModule,
    coordinatorModule
)
```

**Benefícios:**
- Código mais organizado e modular
- Cada feature gerencia suas próprias dependências
- Fácil de encontrar e modificar configurações de DI
- Possibilita modularização futura (Gradle modules)

---

## 5. ✅ ViewModels com Injeção de Parâmetros

### ViewModels Refatorados:

#### ProfileViewModel
**Antes:**
```kotlin
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {
    fun loadProfile(token: String, userId: Int) { ... }
}
```

**Depois:**
```kotlin
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val navigationViewModel: NavigationViewModel,
    private val token: String,
    private val userId: Int,
) : ScreenModel {
    init {
        loadProfile()
    }
    private fun loadProfile() { ... }
}
```

**Screen atualizada:**
```kotlin
@Composable
override fun Content() {
    val viewModel: ProfileViewModel = koinInject { parametersOf(token, userId) }
    // ...
}
```

#### Mesmas mudanças aplicadas para:
- `ProfileSettingsViewModel`
- `TeacherDashboardViewModel`

**Benefícios:**
- ViewModels recebem dados necessários via construtor
- Menos passagem de parâmetros em funções
- Init automático dos dados
- Melhor testabilidade
- Código mais limpo

---

## 6. ✅ Navegação Centralizada

### Estrutura Mantida:

Toda navegação continua passando pelo `NavigationHandler` via `NavigationEvent`:

```kotlin
// No ViewModel
fun navigateToProfile() {
    screenModelScope.launch {
        navigationViewModel.emit(
            NavigationEvent.ToProfile(
                token = token,
                userId = userId,
                // ...
            )
        )
    }
}

// NUNCA diretamente na UI
// ❌ navigator.push(ProfileScreen(...))
```

**Benefício:**
- Single source of truth para navegação
- Fácil de adicionar analytics, logging
- Testável

---

## Resumo das Mudanças por Arquivo

### Arquivos Criados (9):
1. `core/CoreModule.kt`
2. `core/domain/model/Role.kt`
3. `core/domain/model/ClassStatus.kt`
4. `features/auth/AuthModule.kt`
5. `features/teacher/TeacherModule.kt`
6. `features/guardian/GuardianModule.kt`
7. `features/coordinator/CoordinatorModule.kt`
8. `IMPROVEMENTS_APPLIED.md` (este arquivo)

### Arquivos Modificados (7):
1. `AppModule.kt` - Refatorado para usar módulos por feature
2. `core/data/remote/gateway/ApiClient.kt` - JsonNamingStrategy + documentação
3. `shared/presentation/profile/ProfileViewModel.kt` - Parâmetros no construtor
4. `shared/presentation/profile/ProfileSettingsViewModel.kt` - Parâmetros no construtor
5. `shared/presentation/profile/ProfileScreen.kt` - Uso de parametersOf
6. `shared/presentation/profile/ProfileSettingsScreen.kt` - Uso de parametersOf + atualização de chamadas
7. `features/teacher/presentation/dashboard/TeacherDashboardViewModel.kt` - Parâmetros no construtor
8. `features/teacher/presentation/dashboard/TeacherDashboardScreen.kt` - Uso de parametersOf + atualização de chamadas

---

## Próximos Passos Recomendados

### Curto Prazo:
- [ ] Aplicar o mesmo padrão de parametersOf para outros ViewModels (AttendanceCall, GradeBook, ClassList)
- [ ] Adicionar testes unitários para ViewModels
- [ ] Verificar e corrigir todos os warnings de compilação

### Médio Prazo:
- [ ] Implementar as telas de gestão do Coordinator (classes, subjects, teachers, students)
- [ ] Adicionar suporte a refresh token
- [ ] Implementar persistência local (cache)

### Longo Prazo:
- [ ] Separar features em módulos Gradle independentes
- [ ] Adicionar CI/CD
- [ ] Implementar logging centralizado
- [ ] Adicionar analytics

---

## Padrões Estabelecidos

### 1. ViewModels
```kotlin
class MyViewModel(
    private val repository: MyRepository,
    private val navigationViewModel: NavigationViewModel,
    private val param1: String,  // Parâmetros injetados
    private val param2: Int,
) : ScreenModel {
    init {
        loadData()  // Init automático
    }
}
```

### 2. Screens
```kotlin
data class MyScreen(
    val param1: String,
    val param2: Int,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: MyViewModel = koinInject { 
            parametersOf(param1, param2) 
        }
        // ...
    }
}
```

### 3. Módulos Koin
```kotlin
val myFeatureModule = module {
    // Repositories - single
    single { MyRepository(get()) }
    
    // ViewModels sem parâmetros - factory
    factory { SimpleViewModel(get()) }
    
    // ViewModels com parâmetros - factory com lambda
    factory { (param: String) ->
        ComplexViewModel(get(), param)
    }
}
```

### 4. DTOs (com JsonNamingStrategy)
```kotlin
@Serializable
data class MyDto(
    val userId: Int,        // Automaticamente convertido para user_id
    val createdAt: String,  // Automaticamente convertido para created_at
    
    @SerialName("special_field")  // Apenas quando não segue o padrão
    val specialField: String
)
```

---

## Conclusão

Todas as melhorias foram aplicadas com sucesso seguindo as melhores práticas de:
- ✅ Kotlin Multiplatform
- ✅ MVVM Architecture
- ✅ Clean Architecture
- ✅ Dependency Injection (Koin)
- ✅ Type Safety
- ✅ Modularidade

O projeto está agora mais organizado, testável e pronto para escalar.


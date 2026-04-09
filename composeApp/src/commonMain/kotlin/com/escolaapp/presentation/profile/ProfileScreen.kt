package com.escolaapp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.utils.TeacherNavigationBar
import com.escolaapp.utils.TeacherNavigationTab
import org.koin.compose.koinInject

data class ProfileScreen(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String,
    val role: String
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = koinInject()
        var selectedTab by remember { mutableStateOf(TeacherNavigationTab.SETTINGS) }

        Scaffold(
            bottomBar = {
                TeacherNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        selectedTab = tab
                        viewModel.onTabSelected(tab, token, userId, name, email, role)
                    },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier            = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier         = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🎓", fontSize = 18.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text       = "EscolaApp",
                                style      = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(text = "🔔", fontSize = 20.sp)
                    }
                }

                // Card de perfil
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(16.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier            = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar com botão editar
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Box(
                                    modifier         = Modifier
                                        .size(96.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text      = name.first().uppercaseChar().toString(),
                                        fontSize  = 40.sp,
                                        fontWeight = FontWeight.Bold,
                                        color     = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Box(
                                    modifier         = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "✏", fontSize = 12.sp, color = Color.White)
                                }
                            }

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text       = name,
                                style      = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text  = email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text      = if (role == "Teacher") "PROFESSOR" else "RESPONSÁVEL",
                                    style     = MaterialTheme.typography.labelMedium,
                                    color     = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Card Alterar Senha
                item {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier         = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFE8F0FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🔒", fontSize = 20.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text       = "Alterar Senha",
                                        style      = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text  = "Proteja seu acesso à academia",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text  = "Garanta que sua conta permaneça privada atualizando suas credenciais de segurança regularmente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(Modifier.height(12.dp))

                            Row(
                                modifier  = Modifier.clickable { },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text      = "Configurar segurança →",
                                    style     = MaterialTheme.typography.bodyMedium,
                                    color     = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // Card Controle de Sessão
                item {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier         = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFE8F0FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "📱", fontSize = 20.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text       = "Controle de Sessão",
                                        style      = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text  = "Gerencie seus dispositivos ativos",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(Modifier.height(16.dp))

                            // Dispositivo atual
                            DeviceRow(
                                icon          = "📱",
                                deviceName    = "Dispositivo atual",
                                location      = "Brasil",
                                status        = "Ativo agora",
                                isCurrentDevice = true,
                                onLogout      = {}
                            )
                        }
                    }
                }

                // Botão sair de todas as sessões
                item {
                    Box(
                        modifier  = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFAF0F0))
                            .clickable { }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "Sair de todas as sessões",
                            color      = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold,
                            style      = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Footer
                item {
                    Column(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text  = "VERSÃO 1.0.0-ALPHA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text  = "SISTEMA ACADÊMICO ONLINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }

    @Composable
    private fun DeviceRow(
        icon: String,
        deviceName: String,
        location: String,
        status: String,
        isCurrentDevice: Boolean,
        onLogout: () -> Unit
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = deviceName,
                        style      = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isCurrentDevice) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFE8F4FD))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text  = "Este dispositivo",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF1565C0)
                            )
                        }
                    }
                }
                Text(
                    text  = location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text  = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCurrentDevice)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text     = "↪",
                fontSize = 20.sp,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable { onLogout() }
            )
        }
    }
}
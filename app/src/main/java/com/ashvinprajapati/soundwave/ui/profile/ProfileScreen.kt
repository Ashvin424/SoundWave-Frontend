package com.ashvinprajapati.soundwave.ui.profile

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ashvinprajapati.soundwave.data.local.ProfileColorManager
import com.ashvinprajapati.soundwave.data.local.TokenManager
import com.ashvinprajapati.soundwave.data.repository.UserRepository
import com.ashvinprajapati.soundwave.ui.theme.SoundWaveTheme

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val viewModel : ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(tokenManager)
    )

    val user by UserRepository.userProfile.collectAsState()
    val firstLetter = user?.fullName?.firstOrNull()?.uppercaseChar() ?: "?"

    val colorManager = remember { ProfileColorManager(context) }
    val profileColor by colorManager.profileColor.collectAsState(
        initial = ProfileColorManager.DEFAULT_COLOR
    )

    Log.d("ProfileScreen", "User recomposed: ${user?.fullName}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(color = Color(profileColor))
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = firstLetter.toString(),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = user?.fullName ?: "Loading...",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = user?.email ?: "xyz@gmail.com",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = user?.role ?: "Default",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,          // solid accent
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), // fade
                                MaterialTheme.colorScheme.secondaryContainer  // darker end
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterEnd), // sits on right side
                    tint = Color.White.copy(alpha = 0.1f) // very faint
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatItem(
                        value = viewModel.playedCount.toString(),
                        label = "Songs Played",
                        valueColor = Color.White,
                        labelColor = Color.White.copy(alpha = 0.7f)
                    )
                }

            }
        }

        Spacer(Modifier.height(24.dp))

        ProfileActionButton(
            icon = Icons.Default.Edit,
            label = "Edit Profile",
            iconTint = Color(0xFF0D59F2),
            accentColor = Color(0xFF0D59F2),
            onClick = { navController.navigate("editProfile") }
        )

        ProfileActionButton(
            icon = Icons.Default.Lock,
            label = "Change Password",
            iconTint = Color(0xFF9C27B0),    // purple
            accentColor = Color(0xFF9C27B0),
            onClick = { viewModel.showChangePasswordDialog = true }
        )

        ProfileActionButton(
            icon = Icons.Default.ExitToApp,
            label = "Logout",
            iconTint = Color(0xFFDE3737),
            accentColor = Color(0xFFDE3737), // red
            textColor = Color(0xFFDE3737),
            onClick = { viewModel.logout() }
        )

        if (viewModel.showChangePasswordDialog) {
            ChangePasswordDialog(
                onDismiss = {
                    viewModel.showChangePasswordDialog = false
                    viewModel.changePasswordError = ""
                    viewModel.changePasswordSuccess = false
                },
                onConfirm = { currentPassword, newPassword, confirmPassword ->
                    viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                },
                errorMessage = viewModel.changePasswordError,
                isSuccess = viewModel.changePasswordSuccess
            )
        }

    }

}

@Composable
fun StatItem(
    value: String,
    label: String,
    valueColor: Color = MaterialTheme.colorScheme.secondary, // default for other uses
    labelColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Text(
            text = label,
            fontSize = 13.sp,
            color = labelColor
        )
    }
}

@Composable
fun ProfileActionButton(
    icon: ImageVector,
    label: String,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    accentColor: Color = MaterialTheme.colorScheme.primary, // left bar color
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
            .padding(vertical =4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.1f)
        )
    ) {
        // Parent Row — accent bar + content side by side
        Row(modifier = Modifier.fillMaxWidth()) {

            // ✅ The left accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(56.dp)  // matches the row height
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            bottomStart = 12.dp
                        )
                    )
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    fontSize = 16.sp,
                    color = textColor.copy(alpha = 0.75f),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = iconTint.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    errorMessage: String,
    isSuccess: Boolean
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isSuccess) {
                    // Show success message instead of form
                    Text(
                        text = "Password changed successfully!",
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    // Current password field
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    // New password field
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    // Confirm password field
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Error message
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (isSuccess) {
                TextButton(onClick = onDismiss) { Text("Done") }
            } else {
                TextButton(onClick = { onConfirm(currentPassword, newPassword, confirmPassword) }) {
                    Text("Change")
                }
            }
        },
        dismissButton = {
            if (!isSuccess) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenPrev() {

    SoundWaveTheme {
        ProfileScreen(navController = NavController(LocalContext.current))
    }
}
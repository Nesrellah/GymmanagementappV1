import kotlin.math.round

fun updateUserProfile(
    email: String,
    name: String,
    role: String
) {
    viewModelScope.launch {
        repository.getUserProfileByEmail(email)?.let { existingProfile ->
            val updatedProfile = existingProfile.copy(
                name = name,
                role = role
            )
            repository.updateUserProfile(updatedProfile)
            _userProfile.value = updatedProfile
        }
    }
} 
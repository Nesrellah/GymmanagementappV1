fun updateUserProfile(
    email: String,
    name: String,
    role: String = "member"
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

fun updateUserProfileWithBMI(
    email: String,
    name: String,
    age: Int?,
    height: Float?,
    weight: Float?,
    role: String = "member"
) {
    viewModelScope.launch {
        repository.getUserProfileByEmail(email)?.let { existingProfile ->
            val bmi = calculateBMI(height, weight)
            val updatedProfile = existingProfile.copy(
                name = name,
                age = age,
                height = height,
                weight = weight,
                bmi = bmi,
                role = role,
                joinDate = existingProfile.joinDate
            )
            repository.updateUserProfile(updatedProfile)
            _userProfile.value = updatedProfile
        }
    }
}

fun addMember(
    email: String,
    name: String,
    role: String = "member"
) {
    viewModelScope.launch {
        val profile = UserProfile(
            id = 0,
            email = email,
            name = name,
            role = role
        )
        repository.insertUserProfile(profile)
    }
} 
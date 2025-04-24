# Clean the project and invalidate caches
Write-Host "Cleaning project and invalidating caches..."

# Stop any running Gradle daemons
Write-Host "Stopping Gradle daemons..."
./gradlew --stop

# Clean the project
Write-Host "Cleaning project..."
./gradlew clean

# Delete build directories
Write-Host "Deleting build directories..."
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue .gradle
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue build
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue app/build

# Delete .idea directory (optional, uncomment if needed)
# Remove-Item -Recurse -Force -ErrorAction SilentlyContinue .idea

Write-Host "Done! Please restart Android Studio and sync the project." 
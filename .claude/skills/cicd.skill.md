---
name: cicd
description: Continuous Integration and Continuous Deployment setup for Android apps. Triggers when user mentions CI/CD, GitHub Actions, GitLab CI, deployment, or automated builds.
---

# CI/CD Skill

Automated build, test, and deployment pipelines.

## GitHub Actions

### Build and Test

```yaml
# .github/workflows/android.yml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Build with Gradle
      run: ./gradlew build
      
    - name: Run tests
      run: ./gradlew test
      
    - name: Run lint
      run: ./gradlew lint
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

### Release to Play Store

```yaml
name: Release

on:
  push:
    tags:
      - 'v*'

jobs:
  release:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Build Release APK
      run: ./gradlew assembleRelease
      
    - name: Sign APK
      uses: r0adkll/sign-android-release@v1
      with:
        releaseDirectory: app/build/outputs/apk/release
        signingKeyBase64: ${{ secrets.SIGNING_KEY }}
        alias: ${{ secrets.ALIAS }}
        keyStorePassword: ${{ secrets.KEY_STORE_PASSWORD }}
        
    - name: Upload to Play Store
      uses: r0adkll/upload-google-play@v1
      with:
        serviceAccountJsonPlainText: ${{ secrets.SERVICE_ACCOUNT_JSON }}
        packageName: com.example.app
        releaseFiles: app/build/outputs/apk/release/*.apk
        track: production
```

## GitLab CI

```yaml
# .gitlab-ci.yml
image: openjdk:17-jdk

stages:
  - build
  - test
  - deploy

build:
  stage: build
  script:
    - chmod +x ./gradlew
    - ./gradlew assembleDebug
  artifacts:
    paths:
      - app/build/outputs/

test:
  stage: test
  script:
    - ./gradlew test
    - ./gradlew lint

deploy:
  stage: deploy
  script:
    - ./gradlew assembleRelease
  only:
    - main
```

## Fastlane

```ruby
# fastlane/Fastfile
default_platform(:android)

platform :android do
  
  desc "Run tests"
  lane :test do
    gradle(task: "test")
  end
  
  desc "Build debug APK"
  lane :build_debug do
    gradle(task: "assembleDebug")
  end
  
  desc "Deploy to Play Store"
  lane :deploy do
    gradle(task: "assembleRelease")
    upload_to_play_store
  end
  
end
```

---

**Remember:** Automate everything!

# Setup Guide - Android Dev Agent v2.0

Complete guide to setting up and using the Android Development Agent.

---

## 🎯 Choose Your Setup Method

### Quick Comparison

| Method | Setup Time | Where It Works | Best For |
|--------|------------|----------------|----------|
| **Claude.ai Chat** | 2 min | Browser, mobile | Quick questions, learning |
| **Claude Code** | 5 min | Terminal, IDEs | Real development work |
| **Android Studio** | 10 min | Android Studio | Reference while coding |

---

## 📱 Method 1: Claude.ai Chat (Easiest)

**Best for:** Quick questions, code generation, learning

### Setup (2 minutes)

1. **Download the agent folder**
   - You should have `android-dev-agent-v2/` folder

2. **Open Claude.ai**
   - Go to https://claude.ai
   - Start a new chat

3. **Upload the folder**
   - Click the 📎 attachment icon
   - Upload the entire `android-dev-agent-v2` folder
   - Wait for upload to complete

4. **Activate the agent**
   ```
   Use this Android dev agent for all development tasks
   ```

5. **Test it!**
   ```
   Create a simple product list screen with Material 3
   ```

### Usage

```
# Every new chat
"Load android-dev-agent-v2 system"

# Then ask normally
"Create a login feature"
"Add navigation to settings"
"Write tests for this class"
```

### Pros & Cons

✅ **Pros:**
- No installation
- Works everywhere
- Safe (you control what goes in project)

❌ **Cons:**
- Manual file copying
- Can't read your existing codebase
- Can't run tests automatically

---

## 💻 Method 2: Claude Code (Most Powerful)

**Best for:** Real development, automated file creation, code refactoring

### Prerequisites

- macOS, Linux, or Windows
- Node.js 18+ (for npm installation)
- Git (recommended)
- Your Android project

### Step 1: Install Claude Code (5 minutes)

**Option A: Using installer script**
```bash
# macOS/Linux
curl -fsSL https://claude.ai/install.sh | sh

# Verify installation
claude --version
```

**Option B: Using npm**
```bash
npm install -g @anthropic-ai/claude-code

# Verify installation
claude --version
```

**Option C: Download binary**
- Visit https://claude.ai/download
- Download for your OS
- Extract and add to PATH

### Step 2: Copy Agent to Project (2 minutes)

```bash
# Navigate to your Android project
cd ~/Projects/YourAndroidApp

# Create .claude directory
mkdir -p .claude

# Copy dev-agent
cp -r ~/Downloads/dev-agent/* .claude/

# Verify
ls .claude/
# Should see: skills/ memory/ tools/ SYSTEM-PROMPT.md README.md
```

### Step 3: Create CLAUDE.md (2 minutes)

Create `CLAUDE.md` in your project root:

```bash
cat > CLAUDE.md << 'EOF'
# Android Development Agent

## Role
You are an expert Android developer assistant following the patterns and conventions defined in .claude/

## System Prompt
Use .claude/SYSTEM-PROMPT.md for core behavior and safety rules.

## Skills
Auto-load skills from .claude/skills/ based on task context.

## Memory
- Project Context: .claude/memory/project-context.json
- User Preferences: .claude/memory/user-preferences.json  
- Domain Knowledge: .claude/memory/domain-knowledge.md

## Project Structure
```
app/
├── src/
│   ├── main/
│   │   └── kotlin/com/example/app/
│   │       ├── domain/
│   │       ├── data/
│   │       └── presentation/
│   └── test/
└── build.gradle.kts
```

## Current Focus
[Update as needed with current sprint/features]

---

**Always follow safety rules and use appropriate skills for each task!**
EOF
```

### Step 4: Customize for Your Project (5 minutes)

**Edit `.claude/memory/project-context.json`:**

```json
{
  "projectName": "YourApp",  // ← Change this
  "techStack": {
    "frameworks": {
      "compose": "1.6.10",    // ← Your version
      "decompose": "3.0.0",   // ← Your version
      "ktor": "2.3.11"        // ← Your version
    }
  },
  "moduleStructure": {
    "type": "multi-module",   // or "single-module"
    "modules": [
      "app",
      "feature-auth",         // ← Your modules
      "feature-home"
    ]
  }
}
```

**Edit `.claude/memory/user-preferences.json`:**

```json
{
  "codingPreferences": {
    "verbosity": {
      "comments": "moderate",     // ← Your preference
      "explanations": "detailed"  // ← Your preference
    }
  }
}
```

### Step 5: Start Using Claude Code (1 minute)

```bash
# In your project directory
claude

# Claude Code will:
# 1. Read CLAUDE.md
# 2. Load .claude/SYSTEM-PROMPT.md
# 3. Be ready to help!

# Example usage
> Create a user authentication feature with:
> - Email/password login
> - Token storage
> - Auto-refresh

# Claude Code will:
# - Create all necessary files in your project
# - Follow your architecture patterns
# - Use your tech stack versions
# - Run tests
# - Show you a diff
```

### Usage Examples

```bash
# Create feature
> Build a product catalog with API and caching

# Refactor code
> Refactor this ViewModel to use MVI pattern
> [Claude reads your file and updates it]

# Fix bugs
> Fix the crash in ProductListScreen.kt

# Add tests
> Write tests for GetProductsUseCase

# Build tasks
> Update Gradle dependencies to latest stable
```

### Pros & Cons

✅ **Pros:**
- Creates files directly in project
- Reads your actual code
- Runs tests automatically
- Git integration
- Most powerful option

❌ **Cons:**
- Requires installation
- Terminal-based
- More complex setup

---

## 🔧 Method 3: Android Studio Integration

**Best for:** Reference while coding, quick documentation lookup

### Step 1: Copy to Project (2 minutes)

```bash
cd ~/Projects/YourAndroidApp

# Create docs directory
mkdir -p docs/dev-agent

# Copy agent
cp -r ~/Downloads/dev-agent/* docs/dev-agent/

# Verify
tree docs/dev-agent -L 1
```

### Step 2: Install Markdown Plugin (Optional, 2 minutes)

1. Open Android Studio
2. Settings → Plugins
3. Search "Markdown"
4. Install "Markdown Navigator Enhanced"
5. Restart Android Studio

### Step 3: Quick Access Setup (2 minutes)

**Create shortcuts:**

1. **Favorites**
   - Right-click `docs/dev-agent` → Add to Favorites
   - Access via Alt+2 (or ⌘2 on Mac)

2. **Bookmarks**
   - Open `SYSTEM-PROMPT.md`
   - Toggle Bookmark (⌘F3 or F11)
   - Access quickly via bookmarks panel

3. **Recent Files**
   - Ctrl+E (or ⌘E) shows recent files
   - Frequently accessed docs appear here

### Step 4: Usage in Android Studio

**Quick file access:**
```
Ctrl+Shift+N (or ⌘+Shift+N)
Type: "compose.skill"
Press Enter
```

**Reference while coding:**
1. Split screen (right-click file → Split Right)
2. Left: Your code
3. Right: Relevant skill doc

**During code review:**
1. Open SYSTEM-PROMPT.md
2. Check if code follows safety rules
3. Reference relevant skills

### Usage Examples

**Before writing code:**
```
1. Open work-planning.skill.md
2. Create task breakdown
3. Start coding
```

**During development:**
```
1. Split screen
2. Code on left
3. compose.skill.md on right
4. Reference patterns as you code
```

**Code review:**
```
1. Open PR diff
2. Reference security.skill.md
3. Check for violations
```

### Create Live Templates (Optional, 10 minutes)

**For even faster coding:**

1. Settings → Editor → Live Templates
2. Click "+" → Template Group
3. Name: "Android MVI"
4. Add templates:

**Template 1: MVI State**
```
Abbreviation: mvistate
Template text:
data class $NAME$State(
    val isLoading: Boolean = false,
    val data: $DATA_TYPE$? = null,
    val error: String? = null
)
```

**Template 2: MVI Intent**
```
Abbreviation: mviintent
Template text:
sealed interface $NAME$Intent {
    data object Refresh : $NAME$Intent
    data class On$ACTION$($PARAMS$) : $NAME$Intent
}
```

**Template 3: Use Case**
```
Abbreviation: usecase
Template text:
class $NAME$UseCase(
    private val repository: $REPO$
) : UseCase<$PARAMS$, $RETURN$> {
    override suspend fun invoke(params: $PARAMS$): Result<$RETURN$> {
        return runCatching {
            $END$
        }
    }
}
```

### Pros & Cons

✅ **Pros:**
- Always available in IDE
- Quick reference
- No separate tool needed
- Great for code review

❌ **Cons:**
- Just documentation (no automation)
- Manual code writing
- Can't create files automatically

---

## 🌐 Method 4: Web-Based Claude Code

**Best for:** Cloud-based development, async work

### Step 1: Go to Claude Code Web

Visit: https://claude.ai/code

### Step 2: Connect GitHub

1. Click "Connect Repository"
2. Authorize GitHub
3. Select your Android project repo

### Step 3: Copy Agent Files

```bash
# In your repo, create PR
git checkout -b add-dev-agent
mkdir -p .claude
cp -r ~/Downloads/dev-agent/* .claude/
git add .claude
git commit -m "Add Android dev agent"
git push

# Merge PR
```

### Step 4: Use in Web Interface

1. Go to claude.ai/code
2. Select your repo
3. Start new task:
   ```
   Create authentication feature following .claude/ patterns
   ```

4. Claude Code will:
   - Read your repo
   - Load agent
   - Create files
   - Create PR with changes

### Pros & Cons

✅ **Pros:**
- No local installation
- Works in browser
- Async (can run in background)
- Creates PRs automatically

❌ **Cons:**
- Requires GitHub
- Internet dependent
- Less immediate feedback

---

## 🎯 Recommended Setups by Role

### Individual Developer
```
Primary: Claude Code (terminal)
Secondary: Claude.ai Chat (quick questions)
```

### Team Lead
```
Primary: Claude Code + Android Studio reference
Secondary: Claude.ai Chat (design discussions)
Setup: Share agent in team repo
```

### Junior Developer
```
Primary: Claude.ai Chat (learning)
Secondary: Android Studio reference (while coding)
Later: Upgrade to Claude Code
```

### Open Source Contributor
```
Primary: Claude Code Web (async PRs)
Secondary: Claude.ai Chat (questions)
```

---

## 🚀 First Steps After Setup

### Test 1: Simple Screen
```
Create a settings screen with:
- Dark mode toggle
- Language selection
- Notification preferences
```

**Expected:** Complete component + store + UI with Material 3

### Test 2: Complete Feature
```
Create a product catalog feature with:
- List products from API
- Search and filter
- Offline caching with Room
- Details screen with navigation
```

**Expected:** Full Domain + Data + Presentation layers

### Test 3: Refactoring
```
Refactor this code to use MVI pattern:
[paste your existing code]
```

**Expected:** Proper State + Intent + Event + Store

---

## 🔧 Customization After Setup

### 1. Update Tech Stack

Edit `.claude/memory/project-context.json`:
```json
{
  "techStack": {
    "frameworks": {
      "compose": "1.7.0",  // ← Update versions
      "material3": "1.3.0"
    }
  }
}
```

### 2. Add Team Conventions

```json
{
  "teamConventions": {
    "commits": {
      "format": "type(scope): subject",
      "types": ["feat", "fix", "refactor"] // ← Your types
    },
    "codeReview": {
      "minimumApprovals": 2  // ← Your requirement
    }
  }
}
```

### 3. Create Custom Skill

If you use a special library:

```bash
# Create new skill
cat > .claude/skills/firebase.skill.md << 'EOF'
---
name: firebase
description: Firebase integration patterns for Android
---

# Firebase Skill

[Your Firebase patterns and examples]
EOF
```

---

## ⚙️ Advanced Configuration

### Multiple Projects

```bash
# Project 1
~/Projects/AppA/.claude/

# Project 2  
~/Projects/AppB/.claude/

# Each has own memory files!
```

### Team Sharing

```bash
# In your repo
.claude/
├── SYSTEM-PROMPT.md      # Team safety rules
├── skills/                # Team patterns
└── memory/
    ├── project-context.json  # Project info
    └── team-conventions.json # Team standards
```

### Version Control

```bash
# .gitignore
.claude/memory/user-preferences.json  # Personal

# Commit team files
git add .claude/SYSTEM-PROMPT.md
git add .claude/skills/
git add .claude/memory/project-context.json
```

---

## 🆘 Troubleshooting

### Claude Code Not Found
```bash
# Reinstall
npm uninstall -g @anthropic-ai/claude-code
npm install -g @anthropic-ai/claude-code

# Or use direct path
/usr/local/bin/claude
```

### Skills Not Loading
```
Problem: Claude doesn't use skills
Fix 1: Make skill descriptions more explicit
Fix 2: Explicitly mention: "Use compose.skill.md"
Fix 3: Check CLAUDE.md references .claude/ correctly
```

### Files Not Created
```
Problem: Claude generates code but doesn't create files
Fix: You're in chat mode, not Claude Code
Solution: Either:
  - Use Claude Code (terminal)
  - Or download files from chat
```

### Wrong Code Style
```
Problem: Code doesn't match your style
Fix: Update memory/user-preferences.json
Example:
{
  "codingPreferences": {
    "naming": {
      "components": "YourStyle"
    }
  }
}
```

---

## ✅ Setup Checklist

### For Claude.ai Chat
- [ ] Downloaded agent folder
- [ ] Uploaded to Claude.ai
- [ ] Tested with example
- [ ] Updated memory files

### For Claude Code
- [ ] Installed Claude Code
- [ ] Copied to project .claude/
- [ ] Created CLAUDE.md
- [ ] Customized memory files
- [ ] Tested with `claude` command

### For Android Studio
- [ ] Copied to docs/dev-agent/
- [ ] Installed Markdown plugin
- [ ] Created bookmarks
- [ ] Created live templates (optional)

### For All Methods
- [ ] Read SYSTEM-PROMPT.md
- [ ] Read README.md
- [ ] Customized project-context.json
- [ ] Customized user-preferences.json
- [ ] Ran test examples

---

## 🎓 Next Steps

1. **Day 1:**
   - Complete setup
   - Run test examples
   - Read core skills

2. **Week 1:**
   - Build 2-3 features
   - Customize memory files
   - Add team conventions

3. **Month 1:**
   - Create custom skills if needed
   - Share with team
   - Gather feedback

---

**Need help?** Check README.md or FILE-INDEX.md

**Ready to code!** 🚀

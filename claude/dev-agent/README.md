# Android Dev Agent v2.0

**Professional, Safe, Modular Android Development Assistant**

## 🎯 What's New in V2

### Major Improvements

**1. Safety-First Architecture**
- ✅ Mandatory security rules in system prompt
- ✅ Risk assessment before actions
- ✅ Explicit warnings for dangerous operations
- ✅ No hardcoded secrets ever
- ✅ OWASP Mobile Security compliance

**2. Modular Skill System**
- ✅ Technology-specific skills (auto-loaded)
- ✅ Smaller context footprint
- ✅ Easier to maintain and update
- ✅ Mix and match as needed

**3. Enhanced Memory System**
- ✅ Project context (architecture, tech stack)
- ✅ User preferences (coding style, workflow)
- ✅ Domain knowledge (Android fundamentals)

**4. Clear Separation of Concerns**
- 🎯 System Prompt = Safety + General behavior
- 🎯 Skills = Technology-specific knowledge
- 🎯 Memory = Project + User context
- 🎯 Tools = Available actions

---

## 📁 Structure

```
android-dev-agent-v2/
├── SYSTEM-PROMPT.md          ← Core safety rules & behavior
│
├── skills/                    ← Technology-specific skills
│   ├── compose.skill.md       → Jetpack Compose & Material 3
│   ├── decompose.skill.md     → Navigation & components
│   ├── essenty.skill.md       → Lifecycle management
│   ├── gradle.skill.md        → Build configuration
│   ├── testing.skill.md       → Unit, UI, integration tests
│   ├── refactoring.skill.md   → Code improvement
│   ├── work-planning.skill.md → Feature planning
│   ├── networking.skill.md    → API integration
│   ├── room.skill.md          → Local database
│   └── performance.skill.md   → Optimization
│
├── memory/                    ← Context & preferences
│   ├── project-context.json   → Tech stack, architecture
│   ├── user-preferences.json  → Your coding style
│   └── domain-knowledge.md    → Android fundamentals
│
├── tools/                     ← Tool configurations
│   └── available-tools.md     → What Claude can do
│
└── docs/                      ← Documentation
    ├── SETUP-GUIDE.md
    ├── SKILL-DEVELOPMENT.md
    └── SAFETY-GUIDELINES.md
```

---

## 🚀 Quick Start

### Option 1: Use in This Chat (Immediate)

**Step 1:** Tell me to load the system:
```
"Load android-dev-agent-v2 system prompt and relevant skills"
```

**Step 2:** Start coding:
```
"Create a product list feature with offline caching"
```

I'll automatically:
- Follow safety rules
- Load appropriate skills (compose, decompose, etc.)
- Use your project context
- Generate safe, production-ready code

### Option 2: Upload to Claude.ai (Future Chats)

1. Download the entire `android-dev-agent-v2` folder
2. In any Claude chat, upload the folder
3. Say: "Use this Android dev agent for all Android development"
4. All skills and safety rules apply automatically!

### Option 3: Claude Code (Terminal Integration)

```bash
# 1. Add to your project
cd your-android-project
cp -r ~/Downloads/dev-agent .claude/

# 2. Update CLAUDE.md
cat >> CLAUDE.md << 'EOF'
# Android Dev Agent v2

## System
Use .claude/android-dev-agent-v2/SYSTEM-PROMPT.md

## Skills
Auto-load from .claude/android-dev-agent-v2/skills/

## Memory
- Project: .claude/android-dev-agent-v2/memory/project-context.json
- Preferences: .claude/android-dev-agent-v2/memory/user-preferences.json
EOF

# 3. Customize for your project
# Edit memory/project-context.json with your tech stack
# Edit memory/user-preferences.json with your preferences

# 4. Use Claude Code
claude
```

---

## 🎨 How It Works

### Automatic Skill Loading

```
You ask: "Create a login screen with Material 3"
          ↓
System loads: SYSTEM-PROMPT.md (safety + general rules)
          ↓
Auto-detects: "Compose" + "Material 3" keywords
          ↓
Loads skills: compose.skill.md + decompose.skill.md
          ↓
Loads memory: project-context.json + user-preferences.json
          ↓
Result: Safe, compliant code following your exact patterns!
```

### Safety Checks (Automatic)

Before every action, Claude checks:
1. **Security**: Any hardcoded secrets? Insecure patterns?
2. **Risk**: Could this break the app? Cause data loss?
3. **Reversibility**: Can user undo this easily?
4. **Compliance**: Follows OWASP guidelines?

If HIGH risk → Warns you and asks confirmation

### Context Awareness

Claude loads:
- ✅ Your tech stack from `project-context.json`
- ✅ Your coding style from `user-preferences.json`
- ✅ Android fundamentals from `domain-knowledge.md`
- ✅ Technology-specific patterns from skills

Result: Code that matches YOUR project exactly!

---

## 📚 Skill System

### Available Skills

| Skill | When to Use | Auto-Triggers On |
|-------|-------------|------------------|
| **compose** | UI development | "compose", "UI", "screen", "composable" |
| **decompose** | Navigation | "navigate", "routing", "screen transition" |
| **testing** | Writing tests | "test", "testing", "TDD", "MockK" |
| **gradle** | Build config | "build", "gradle", "dependency" |
| **refactoring** | Code cleanup | "refactor", "improve", "clean up" |
| **work-planning** | Task breakdown | "plan", "tasks", "estimate" |
| **networking** | API calls | "API", "network", "Ktor", "Retrofit" |
| **room** | Database | "database", "Room", "SQLite" |
| **performance** | Optimization | "optimize", "performance", "slow" |

### Multiple Skills Can Load

Example:
```
Request: "Create a product list with API and caching"

Loads: compose.skill.md (for UI)
     + networking.skill.md (for API)
     + room.skill.md (for caching)
     + decompose.skill.md (for navigation)
```

### Skills Are Modular

Each skill is independent:
- ✅ Update one without affecting others
- ✅ Add new skills easily
- ✅ Remove skills you don't need
- ✅ Customize per project

---

## 🛡️ Safety Features

### What's Protected

**Security:**
- ❌ Never generates hardcoded API keys
- ❌ Never logs sensitive data
- ❌ Never disables SSL validation
- ✅ Always uses Android Keystore for secrets
- ✅ Always validates inputs
- ✅ Always uses HTTPS

**Code Safety:**
- ⚠️ Warns before destructive operations
- ⚠️ Requires confirmation for high-risk changes
- ⚠️ Suggests backups before refactoring
- ✅ Maintains backward compatibility
- ✅ Preserves existing functionality

**Risk Levels:**
- 🟢 **Low**: New feature, isolated changes
- 🟡 **Medium**: Modifying existing code
- 🔴 **High**: Database migrations, auth changes

For HIGH risk:
1. Explicit warning with 🛑
2. Explanation of consequences
3. Suggestion to backup
4. Request for confirmation
5. Rollback instructions provided

---

## 🎨 Customization

### 1. Project Context

Edit `memory/project-context.json`:

```json
{
  "projectName": "YourApp",
  "techStack": {
    "compose": "1.7.0",  ← Your version
    "minSdk": 26,         ← Your min SDK
    ...
  },
  "moduleStructure": {
    "type": "single-module"  ← or "multi-module"
  }
}
```

### 2. User Preferences

Edit `memory/user-preferences.json`:

```json
{
  "codingPreferences": {
    "verbosity": {
      "comments": "minimal",  ← or "moderate", "detailed"
      "explanations": "concise"
    },
    "naming": {
      "components": "FeatureVM"  ← Your convention
    }
  },
  "communicationStyle": {
    "tone": "casual"  ← or "professional"
  }
}
```

### 3. Add New Skills

Create `skills/your-skill.skill.md`:

```markdown
---
name: your-skill
description: What it does and when to trigger
---

# Your Skill

[Your technology-specific knowledge]
```

---

## 💡 Usage Examples

### Example 1: Create Feature

**You:**
```
"Create a user authentication feature with:
- Email/password login
- Biometric authentication
- JWT token management
- Offline support"
```

**Agent:**
1. Loads: compose + decompose + networking + room skills
2. Checks: project-context.json for tech stack
3. Applies: Safety rules (no hardcoded keys!)
4. Generates: Complete feature with all layers
5. Warns: "This includes sensitive auth code - review security carefully"

### Example 2: Refactor Code

**You:**
```
"Refactor this ViewModel to use MVI pattern:
[paste code]"
```

**Agent:**
1. Loads: refactoring.skill.md
2. Assesses risk: 🟡 Medium (existing code modification)
3. Warns: "This will change state management - ensure tests pass"
4. Suggests: "Create git commit first"
5. Provides: Refactored code + migration guide

### Example 3: Plan Work

**You:**
```
"I need to build a shopping cart. Create a work plan."
```

**Agent:**
1. Loads: work-planning.skill.md
2. Generates: Detailed breakdown
   - Requirements analysis
   - Technical approach
   - Task checklist
   - Time estimates
   - Success criteria

---

## 🔧 Advanced Features

### Custom Safety Rules

Add to `SYSTEM-PROMPT.md`:

```markdown
## Company-Specific Rules

**For Our Project:**
- Always use our internal analytics SDK
- Never use third-party crash reporting
- API keys stored in BuildConfig only
```

### Skill Combinations

```
Feature needs: UI + Navigation + Network + Database

Auto-loads:
- compose.skill.md
- decompose.skill.md
- networking.skill.md
- room.skill.md

Result: Integrated solution using all patterns!
```

### Memory Updates

Update context as project evolves:

```json
// memory/project-context.json
{
  "currentContext": {
    "activeSprint": "Sprint 23",
    "focusAreas": ["Performance", "Testing"],
    "recentChanges": [
      "Migrated to Compose 1.7",
      "Added analytics integration"
    ]
  }
}
```

---

## 📊 Comparison: V1 vs V2

| Feature | V1 | V2 |
|---------|----|----|
| **Safety Rules** | Basic | Comprehensive + Risk Assessment |
| **Structure** | Monolithic docs | Modular skills |
| **Context** | Manual loading | Auto-loaded memory |
| **Customization** | Edit large files | Edit small JSON/MD files |
| **Skill Loading** | All or nothing | Smart auto-loading |
| **Security** | Mentioned | Enforced |
| **Maintainability** | Difficult | Easy |

---

## 🎓 Learning Path

**Day 1:**
1. Read `SYSTEM-PROMPT.md` (understand safety)
2. Browse `skills/` (see what's available)
3. Check `memory/` (project context)
4. Try example: "Create a simple screen"

**Week 1:**
1. Customize `memory/project-context.json`
2. Customize `memory/user-preferences.json`
3. Build 2-3 features
4. Note what works well

**Month 1:**
1. Add custom skills if needed
2. Refine safety rules for your team
3. Share with team
4. Gather feedback

---

## 🆘 Troubleshooting

**Skills not loading?**
- Check skill description includes relevant keywords
- Make description more "pushy"
- Explicitly mention skill: "Use compose.skill.md for this"

**Too much context?**
- Reduce number of skills
- Be more specific in requests
- Use minimal verbosity in preferences

**Code doesn't match project?**
- Update `memory/project-context.json`
- Update `memory/user-preferences.json`
- Provide example of preferred code

**Safety warnings annoying?**
- They're protecting you! But you can adjust in preferences
- Lower risk threshold in `user-preferences.json`

---

## 🤝 Contributing

**Add a New Skill:**
1. Create `skills/your-skill.skill.md`
2. Follow existing skill format
3. Test with various requests
4. Update this README

**Improve Safety:**
1. Identify security gap
2. Add to `SYSTEM-PROMPT.md` safety section
3. Test that it catches the issue
4. Document in safety guidelines

---

## 📄 Files Overview

### Core Files (Must Have)
- ✅ `SYSTEM-PROMPT.md` - Safety + behavior
- ✅ `memory/project-context.json` - Your project
- ✅ `memory/user-preferences.json` - Your style

### Essential Skills (Recommended)
- ✅ `skills/compose.skill.md`
- ✅ `skills/decompose.skill.md`
- ✅ `skills/testing.skill.md`

### Optional Skills (Add as Needed)
- 📦 `skills/gradle.skill.md`
- 📦 `skills/networking.skill.md`
- 📦 `skills/room.skill.md`
- 📦 `skills/performance.skill.md`

---

## 🎯 Best Practices

**Do:**
- ✅ Customize memory files for your project
- ✅ Update context as project evolves
- ✅ Add project-specific safety rules
- ✅ Create skills for your special workflows
- ✅ Version control your agent setup

**Don't:**
- ❌ Skip safety customization
- ❌ Load all skills always
- ❌ Ignore security warnings
- ❌ Forget to update tech versions

---

## 🚀 Ready to Use!

**Start now:**
```
"Load android-dev-agent-v2 and create a settings screen"
```

**Or customize first:**
1. Edit `memory/project-context.json`
2. Edit `memory/user-preferences.json`
3. Then start building!

---

**Version:** 2.0  
**Last Updated:** March 2, 2026  
**Maintained by:** Your Development Team

**Remember:** This agent is YOUR assistant. Customize it to match YOUR workflow! 🎉

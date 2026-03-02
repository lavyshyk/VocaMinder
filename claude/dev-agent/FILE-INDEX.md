# Android Dev Agent v2.0 - Complete Overview

## 📋 What's Included

This is a complete, production-ready Android development assistant with:

✅ **Comprehensive safety rules**  
✅ **16 specialized skills** for different technologies  
✅ **Smart context management** with memory files  
✅ **Tool documentation** for safe operations  
✅ **Modular architecture** for easy customization  

---

## 📁 Complete File Structure

```
android-dev-agent-v2/
│
├── 📄 SYSTEM-PROMPT.md              ← Core safety & behavior (MUST READ)
├── 📄 README.md                     ← Complete setup guide
├── 📄 MIGRATION-GUIDE.md            ← Upgrade from v1
├── 📄 FILE-INDEX.md                 ← This file
│
├── 📁 skills/                       ← Technology-specific skills (16 files)
│   ├── mvi.skill.md                 → MVI architecture pattern ⭐
│   ├── compose.skill.md             → Jetpack Compose & Material 3 ⭐
│   ├── decompose.skill.md           → Navigation & components ⭐
│   ├── essenty.skill.md             → Lifecycle management
│   ├── testing.skill.md             → Unit, UI, integration tests ⭐
│   ├── gradle.skill.md              → Build configuration
│   ├── networking.skill.md          → API integration (Ktor) ⭐
│   ├── room.skill.md                → Local database ⭐
│   ├── refactoring.skill.md         → Code improvement
│   ├── work-planning.skill.md       → Feature planning
│   ├── performance.skill.md         → Optimization techniques
│   ├── security.skill.md            → Security best practices ⭐
│   ├── summarizing.skill.md         → Code documentation
│   └── cicd.skill.md                → Automated deployment
│
├── 📁 memory/                       ← Context & preferences
│   ├── project-context.json         → Your tech stack & architecture ⭐
│   ├── user-preferences.json        → Your coding style ⭐
│   └── domain-knowledge.md          → Android fundamentals
│
├── 📁 tools/                        ← Tool documentation
│   └── available-tools.md           → What Claude can do
│
└── 📁 docs/                         ← Additional documentation
    ├── SETUP-GUIDE.md               → Detailed setup instructions
    └── SKILL-DEVELOPMENT.md         → How to create skills
```

**⭐ = Most Important Files**

---

## 🎯 Quick Start Priority

### Day 1: Essential Reading (30 minutes)

1. **SYSTEM-PROMPT.md** (10 min)
   - Understand safety rules
   - Learn general behavior
   - See what's enforced

2. **README.md** (15 min)
   - Quick start options
   - How skills work
   - Basic examples

3. **memory/project-context.json** (5 min)
   - Update with your tech stack
   - Set your architecture

### Week 1: Explore Skills (1 hour)

**Core Skills to Read:**
- `skills/mvi.skill.md` - Your architecture pattern
- `skills/compose.skill.md` - UI development
- `skills/decompose.skill.md` - Navigation
- `skills/testing.skill.md` - Testing strategies

**Supporting Skills (skim):**
- `skills/networking.skill.md` - API integration
- `skills/room.skill.md` - Database
- `skills/security.skill.md` - Security rules

### Month 1: Customize & Master

1. Fully customize `memory/project-context.json`
2. Fully customize `memory/user-preferences.json`
3. Add custom skills if needed
4. Share with team

---

## 📚 Skills Reference

### 🏗️ Architecture Skills

**mvi.skill.md**
- State, Intent, Event patterns
- Store implementation
- Component integration
- Testing MVI components
- **Use for:** Every feature you build

**compose.skill.md**
- Composable patterns
- Material 3 components
- State management
- Performance optimization
- **Use for:** All UI development

**decompose.skill.md**
- Type-safe navigation
- Component lifecycle
- State preservation
- Back handling
- **Use for:** Screen navigation

**essenty.skill.md**
- Lifecycle management
- StateKeeper for config changes
- Back button handling
- InstanceKeeper
- **Use for:** Component lifecycle

### 🔧 Development Skills

**testing.skill.md**
- Unit tests (JUnit, MockK)
- Store tests
- Compose UI tests
- Integration tests
- **Use for:** Writing tests

**gradle.skill.md**
- Build configuration
- Dependency management
- Build variants
- Version catalogs
- **Use for:** Build issues

**refactoring.skill.md**
- Extract function/class
- Remove duplication
- Improve code quality
- Refactoring patterns
- **Use for:** Code cleanup

**work-planning.skill.md**
- Feature breakdown
- Task estimation
- Development planning
- Success criteria
- **Use for:** Planning new features

### 🌐 Data Skills

**networking.skill.md**
- Ktor client setup
- Error handling
- DTOs
- Retry logic
- WebSocket
- **Use for:** API integration

**room.skill.md**
- Database setup
- Entities & DAOs
- Migrations
- Relationships
- Offline-first patterns
- **Use for:** Local database

### 🛡️ Quality Skills

**performance.skill.md**
- Compose optimization
- Memory management
- Image loading
- Profiling
- **Use for:** App optimization

**security.skill.md**
- Secure storage
- Network security
- Input validation
- Certificate pinning
- **Use for:** Security implementation

**cicd.skill.md**
- GitHub Actions
- GitLab CI
- Fastlane
- Automated deployment
- **Use for:** CI/CD setup

### 📖 Utility Skills

**summarizing.skill.md**
- Code documentation
- KDoc generation
- Explaining complex code
- **Use for:** Documentation

---

## 🎨 How Skills Work

### Automatic Loading

```
You: "Create a login screen with Material 3"
     ↓
System loads: SYSTEM-PROMPT.md (safety)
     ↓
Detects keywords: "screen" + "Material 3"
     ↓
Auto-loads: compose.skill.md
     +       decompose.skill.md (for navigation)
     +       mvi.skill.md (for architecture)
     ↓
Loads context: project-context.json
     +         user-preferences.json
     ↓
Generates: Safe, compliant code following your patterns!
```

### Multiple Skills at Once

Example: "Create product list with API and caching"

**Loads:**
- `mvi.skill.md` (architecture)
- `compose.skill.md` (UI)
- `decompose.skill.md` (navigation)
- `networking.skill.md` (API)
- `room.skill.md` (caching)

Result: Complete feature with all layers!

---

## 🔒 Safety System

### Security Rules (SYSTEM-PROMPT.md)

**NEVER generates:**
- ❌ Hardcoded API keys
- ❌ Logged sensitive data
- ❌ Disabled SSL validation
- ❌ Plain text passwords
- ❌ Insecure crypto

**ALWAYS includes:**
- ✅ Android Keystore usage
- ✅ Input validation
- ✅ HTTPS enforcement
- ✅ Error handling
- ✅ Security warnings

### Risk Assessment

**Every action is assessed:**

🟢 **Low Risk** - New feature, isolated
- Proceed normally
- Standard code generation

🟡 **Medium Risk** - Modifying existing code
- Explain changes
- Suggest testing
- Provide rollback

🔴 **High Risk** - Database, auth, refactoring
- ⚠️ Explicit warning
- 💾 Suggest backup
- 📝 Explain consequences
- ✅ Ask confirmation
- 🔙 Provide rollback steps

---

## 💾 Memory Files

### project-context.json

**What it stores:**
- Project name & description
- Architecture pattern (MVI, MVVM, etc.)
- Tech stack versions
- Module structure
- Build system configuration
- Team conventions

**When to update:**
- Upgrade library versions
- Change architecture
- Add new modules
- Update conventions

### user-preferences.json

**What it stores:**
- Your experience level
- Code verbosity preferences
- Naming conventions
- Communication style
- Workflow preferences
- Safety settings

**When to update:**
- Change coding style
- Prefer different explanations
- Adjust safety thresholds
- Update workflow

### domain-knowledge.md

**What it contains:**
- Android SDK basics
- Kotlin language features
- Common patterns
- Best practices

**No need to update** - This is reference material

---

## 🛠️ Tools Documentation

See `tools/available-tools.md` for:
- File operations (create, edit, view)
- Bash commands (with safety rules)
- Output presentation
- Usage guidelines

---

## 📊 File Dependency Map

```
SYSTEM-PROMPT.md
    ↓ (references)
    ├─→ skills/*.skill.md (auto-loaded by topic)
    ├─→ memory/project-context.json (project info)
    ├─→ memory/user-preferences.json (user style)
    └─→ tools/available-tools.md (what I can do)

skills/*.skill.md
    ↓ (may reference)
    └─→ memory/domain-knowledge.md (Android basics)

memory/project-context.json
    ↓ (used by)
    └─→ All skills (for tech versions, architecture)

memory/user-preferences.json
    ↓ (used by)
    └─→ SYSTEM-PROMPT.md (for behavior customization)
```

---

## ✅ Checklist: Did You?

### Before First Use
- [ ] Read SYSTEM-PROMPT.md
- [ ] Read README.md
- [ ] Update memory/project-context.json
- [ ] Update memory/user-preferences.json

### For Android Studio Integration
- [ ] Copy to project (`.claude/` folder)
- [ ] Create CLAUDE.md in project root
- [ ] Install Markdown plugin (optional)

### For Claude Code Integration
- [ ] Install Claude Code
- [ ] Copy files to project `.claude/` folder
- [ ] Update CLAUDE.md
- [ ] Test with simple command

### For Claude.ai Chat
- [ ] Upload entire folder
- [ ] Say "Use this Android dev agent"
- [ ] Test with example: "Create a settings screen"

---

## 🆘 Common Issues

### Skills Not Loading?
→ **Fix:** Make skill description more explicit  
→ **Or:** Explicitly mention: "Use compose.skill.md for this"

### Code Doesn't Match Project?
→ **Fix:** Update `memory/project-context.json`  
→ **Fix:** Update `memory/user-preferences.json`

### Too Many Warnings?
→ **Fix:** Adjust safety preferences in `user-preferences.json`  
→ **Note:** Warnings protect you - consider keeping them!

### Want Different Patterns?
→ **Fix:** Edit relevant skill file  
→ **Or:** Create custom skill  
→ **Or:** Update project-context.json

---

## 🎓 Learning Path

**Day 1:** Read core files, test basic commands  
**Week 1:** Explore skills, build 2-3 features  
**Week 2:** Customize memory files, add team conventions  
**Week 3:** Create custom skill if needed  
**Month 1:** Full team adoption, refinement  

---

## 📞 Support

**Documentation:**
- README.md - Setup & usage
- SYSTEM-PROMPT.md - Behavior & safety
- MIGRATION-GUIDE.md - Upgrade from v1

**Customization:**
- skills/*.skill.md - Technology patterns
- memory/*.json - Project context

**Community:**
- GitHub: Share custom skills
- Team: Collaborate on conventions
- Feedback: Improve based on usage

---

**Version:** 2.0  
**Last Updated:** March 2, 2026  
**Total Files:** 25+ files  
**Total Skills:** 16 specialized skills  
**Status:** Production Ready ✅

---

**Ready to build amazing Android apps!** 🚀

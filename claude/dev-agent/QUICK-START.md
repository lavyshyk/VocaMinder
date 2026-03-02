# Quick Start - Get Coding in 5 Minutes! ⚡

The fastest way to start using Android Dev Agent v2.0.

---

## 🚀 3-Step Quick Start

### Step 1: Choose Your Method (30 seconds)

**Want to try it NOW?** → Claude.ai Chat (no install)  
**Want maximum power?** → Claude Code (5 min install)  
**Just need reference?** → Android Studio (2 min)

### Step 2: Follow Your Path (2-5 minutes)

Pick one below ↓

### Step 3: Test It (1 minute)

Try: "Create a settings screen with dark mode toggle"

Done! 🎉

---

## 📱 Path A: Claude.ai Chat (FASTEST)

**Time:** 2 minutes  
**Best for:** Trying it out, quick questions

```
1. Go to https://claude.ai

2. Upload android-dev-agent-v2 folder

3. Say: "Use this Android dev agent"

4. Test: "Create a product list screen"

✅ Done! You're coding!
```

**What you can do:**
- Generate code → Download → Add to project
- Ask questions → Get answers
- Plan features → Get breakdown
- Review code → Get feedback

**Limitations:**
- Manual file creation (you copy/paste)
- Can't read your existing code
- Can't run tests

---

## 💻 Path B: Claude Code (MOST POWERFUL)

**Time:** 5 minutes  
**Best for:** Real development

```bash
# 1. Install (1 min)
curl -fsSL https://claude.ai/install.sh | sh

# 2. Add to project (2 min)
cd your-android-project
mkdir .claude
cp -r ~/Downloads/dev-agent/* .claude/

# 3. Create CLAUDE.md (1 min)
cat > CLAUDE.md << 'EOF'
# Android Dev Agent
Use .claude/SYSTEM-PROMPT.md and .claude/skills/
Load .claude/memory/project-context.json
EOF

# 4. Customize (1 min)
# Edit .claude/memory/project-context.json with your tech stack

# 5. Start! (10 sec)
claude

# ✅ Done! Files created directly in project!
```

**What you can do:**
- Create files automatically
- Read your existing code
- Run tests
- Refactor multiple files
- Everything from Path A, but automated!

---

## 🔧 Path C: Android Studio Reference

**Time:** 2 minutes  
**Best for:** Reference while coding

```bash
# 1. Copy to project
cd your-android-project
mkdir -p docs/dev-agent
cp -r ~/Downloads/dev-agent/* docs/dev-agent/

# 2. Open in Android Studio
# File → Open → docs/dev-agent/

# 3. Bookmark files (optional)
# Right-click SYSTEM-PROMPT.md → Add Bookmark

# ✅ Done! Quick reference available!
```

**What you can do:**
- Quick file access (Ctrl+Shift+N)
- Split screen reference
- Copy patterns
- Code review checklist

---

## 🧪 Test Examples (Try These!)

### Test 1: Simple Screen
```
Create a settings screen with:
- Dark mode toggle
- Language selection
- Notification preferences
```

**Expected:**
- SettingsComponent.kt
- SettingsStore.kt
- SettingsState/Intent/Event.kt
- SettingsScreen.kt (Compose)

### Test 2: Complete Feature
```
Create a product catalog with:
- List products from API
- Search and filter
- Offline caching
- Details screen with navigation
```

**Expected:**
- Domain layer (Use Cases, Repository)
- Data layer (API, Database, Mappers)
- Presentation layer (Components, UI)
- All following MVI + Clean Architecture

### Test 3: Ask Questions
```
How should I structure a multi-module project?
What's the best way to handle errors in MVI?
Show me how to test a Store.
```

**Expected:**
- Clear explanations
- Code examples
- Best practices
- References to skills

---

## ⚙️ Quick Customization (Optional, 2 minutes)

### Update Your Tech Stack

Edit `.claude/memory/project-context.json`:

```json
{
  "projectName": "MyApp",  // ← Your app name
  "techStack": {
    "frameworks": {
      "compose": "1.6.10",   // ← Your version
      "ktor": "2.3.11"       // ← Your version
    }
  }
}
```

### Set Your Preferences

Edit `.claude/memory/user-preferences.json`:

```json
{
  "codingPreferences": {
    "verbosity": {
      "comments": "minimal"  // or "moderate", "detailed"
    }
  },
  "communicationStyle": {
    "tone": "casual"  // or "professional"
  }
}
```

---

## 🎯 Common First Tasks

### Create Your First Feature

```
Create a user authentication feature with:
- Email/password login
- Remember me functionality
- Forgot password flow
- Token management
```

### Refactor Existing Code

```
Refactor this ViewModel to use MVI pattern:
[paste your ViewModel code]
```

### Plan a Sprint

```
I need to build a shopping cart. Create a detailed work plan with:
- Task breakdown
- Time estimates
- Dependencies
```

### Review Code

```
Review this code for security issues:
[paste your code]
```

---

## 💡 Pro Tips

### Tip 1: Be Specific
```
❌ "Create a screen"
✅ "Create a product list screen with Material 3, search, and pull-to-refresh"
```

### Tip 2: Mention Technologies
```
❌ "Add database"
✅ "Add Room database for offline caching"
```

### Tip 3: Ask for Explanations
```
✅ "Create login screen and explain the MVI pattern used"
✅ "Why did you structure it this way?"
```

### Tip 4: Request Multiple Skills
```
✅ "Create API client using Ktor and test it with MockK"
(Loads networking + testing skills)
```

### Tip 5: Set Context
```
✅ "I'm building an e-commerce app. Create product catalog feature."
(Agent understands broader context)
```

---

## 🆘 Troubleshooting (Quick Fixes)

### "Skills not loading"
```
Fix: Be more explicit
Say: "Use compose.skill.md to create a screen"
```

### "Code doesn't match my style"
```
Fix: Update preferences
Edit: .claude/memory/user-preferences.json
```

### "Wrong tech stack"
```
Fix: Update context
Edit: .claude/memory/project-context.json
```

### "Too many warnings"
```
This is good! Warnings protect you.
But if annoying: Adjust in user-preferences.json
```

### "Claude Code not found"
```
Fix: Reinstall
npm uninstall -g @anthropic-ai/claude-code
npm install -g @anthropic-ai/claude-code
```

---

## 📚 What to Read Next

**Completed quick start?** Great! Now:

### Day 1 (30 min)
1. Read `SYSTEM-PROMPT.md` (safety rules)
2. Skim `skills/compose.skill.md`
3. Skim `skills/decompose.skill.md`

### Week 1 (1 hour)
1. Build 2-3 features
2. Customize memory files fully
3. Read `docs/SETUP-GUIDE.md` for advanced setup

### Month 1
1. Create custom skill if needed
2. Share with team
3. Refine based on usage

---

## ✅ Success Checklist

After quick start, you should be able to:

- [ ] Generate complete features (Domain + Data + Presentation)
- [ ] Ask questions and get clear answers
- [ ] Get code that follows MVI + Clean Architecture
- [ ] See Material 3 components in generated UI
- [ ] Understand safety warnings
- [ ] Customize for your project

**If any of these don't work, check troubleshooting above!**

---

## 🎉 You're Ready!

**Next steps:**
1. Try the test examples above
2. Build a real feature
3. Explore more skills
4. Customize to your needs

**Need help?**
- README.md → Complete guide
- SETUP-GUIDE.md → Detailed setup
- SAFETY-GUIDELINES.md → Security info
- SKILL-DEVELOPMENT.md → Create skills

---

**Total time:** 5 minutes
**Result:** Professional Android development assistant ready to use!

**Let's build amazing apps!** 🚀

# Migrating from v1 to v2

## 🎯 Key Differences

### Architecture Changes

**V1 (Monolithic)**
```
android-dev-agent/
├── system-prompt.md (EVERYTHING)
├── docs/
│   ├── mvi-architecture.md
│   ├── clean-architecture.md
│   └── ...
└── memory/
    └── *.json
```

**V2 (Modular)**
```
android-dev-agent-v2/
├── SYSTEM-PROMPT.md (Safety + General ONLY)
├── skills/ (Auto-loaded by topic)
│   ├── compose.skill.md
│   ├── decompose.skill.md
│   └── ...
└── memory/ (Enhanced context)
```

### What's Better in V2

✅ **Safety First**
- Comprehensive security rules
- Risk assessment
- Explicit warnings

✅ **Modular Skills**
- Smaller, focused files
- Auto-loading based on context
- Easy to update independently

✅ **Enhanced Memory**
- Project context
- User preferences
- Domain knowledge

✅ **Better Maintainability**
- Change one skill without affecting others
- Add new technologies easily
- Team-specific customization

---

## 📋 Migration Checklist

### Step 1: Understand the Changes
- [ ] Read v2 README.md
- [ ] Review SYSTEM-PROMPT.md (safety rules)
- [ ] Browse skills/ folder

### Step 2: Migrate Your Customizations

**If you customized v1 system-prompt.md:**
→ Extract tech-specific parts to v2 skills
→ Keep safety/general rules in SYSTEM-PROMPT.md

**If you customized v1 memory files:**
→ Merge into v2 memory/project-context.json
→ Add preferences to memory/user-preferences.json

### Step 3: Add Your Project Details

Edit `memory/project-context.json`:
```json
{
  "projectName": "YourApp",
  "techStack": {
    "frameworks": {
      "compose": "YOUR_VERSION",
      "decompose": "YOUR_VERSION"
    }
  }
}
```

Edit `memory/user-preferences.json`:
```json
{
  "codingPreferences": {
    "naming": {
      "components": "YourConvention"
    }
  }
}
```

### Step 4: Test

Try these:
```
"Create a simple screen" (tests compose skill)
"Add navigation" (tests decompose skill)
"Write tests" (tests testing skill)
```

---

## 🔄 What to Keep from V1

### Keep:
- ✅ Your project context (update format)
- ✅ Your coding standards (add to preferences)
- ✅ Your architecture decisions (in project-context.json)

### Can Discard:
- ❌ V1 monolithic docs (v2 skills are better)
- ❌ Old memory format (v2 has enhanced version)

---

## 💡 Quick Migration Examples

### Example 1: Your Custom Tech Stack

**V1:**
```markdown
# system-prompt.md
...
Ktor version: 2.3.0
Room version: 2.5.0
...
```

**V2:**
```json
// memory/project-context.json
{
  "techStack": {
    "frameworks": {
      "ktor": "2.3.0",
      "room": "2.5.0"
    }
  }
}
```

### Example 2: Your Coding Style

**V1:**
```markdown
# system-prompt.md
...
Use PascalCase for components
Add detailed comments
...
```

**V2:**
```json
// memory/user-preferences.json
{
  "codingPreferences": {
    "naming": {
      "components": "PascalCase"
    },
    "verbosity": {
      "comments": "detailed"
    }
  }
}
```

### Example 3: Your Safety Rules

**V1:**
```markdown
# system-prompt.md
Never use third-party analytics
Always use our internal SDK
```

**V2:**
```markdown
# SYSTEM-PROMPT.md (add to safety section)
## Company-Specific Security Rules

- Never use third-party analytics
- Always use our internal analytics SDK
```

---

## 🎯 Advantages After Migration

**Before (V1):**
- Large system prompt (hard to find things)
- Everything loads always (slow)
- Hard to update (one big file)

**After (V2):**
- Clear structure (easy to navigate)
- Smart loading (only what's needed)
- Easy updates (edit one skill)
- Better safety (explicit rules)
- More customizable (preferences file)

---

## ⚡ Quick Start After Migration

1. Place v2 in your project
2. Edit memory files
3. Test with: "Create a screen"
4. Adjust as needed

Done! 🎉

---

## 🆘 If Something Breaks

**Skills not loading?**
→ Check skill descriptions include keywords
→ Explicitly mention: "Use compose skill"

**Code different from v1?**
→ Update memory/project-context.json
→ Update memory/user-preferences.json

**Missing features from v1?**
→ Create a custom skill
→ Or add to existing skill

---

## 📊 Side-by-Side Comparison

| Feature | V1 | V2 |
|---------|----|----|
| Safety rules | Basic | Comprehensive |
| File structure | Monolithic | Modular |
| Skill loading | Manual | Automatic |
| Customization | Edit big files | Edit JSON |
| Context size | Large | Optimized |
| Maintainability | Hard | Easy |

---

**Recommendation:** Start fresh with v2. It's worth it! 🚀

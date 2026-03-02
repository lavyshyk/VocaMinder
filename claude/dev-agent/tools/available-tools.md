# Available Tools

Tools that Claude can use to help with Android development.

## File Operations

### create_file
**Purpose:** Create new files  
**When:** Generating code, creating configs, making documentation  
**Example:**
```
create_file(
    path="/home/claude/Feature.kt",
    content="class Feature { }"
)
```

### str_replace
**Purpose:** Edit existing files  
**When:** Modifying code, fixing bugs, refactoring  
**Example:**
```
str_replace(
    path="Feature.kt",
    old_str="class Feature",
    new_str="class FeatureImpl : Feature"
)
```

### view
**Purpose:** Read files and directories  
**When:** Understanding existing code, checking structure  
**Example:**
```
view(path="/project/src")  // List directory
view(path="/project/Feature.kt")  // Read file
view(path="/project/Feature.kt", view_range=[1, 50])  // Read lines 1-50
```

## Execution

### bash_tool
**Purpose:** Run shell commands  
**When:** Building, testing, file operations  
**Safety:** ⚠️ Requires confirmation for destructive commands  
**Example:**
```
bash_tool(command="./gradlew test")
bash_tool(command="ls -la")
```

**Destructive commands (requires warning):**
- `rm -rf` - Deletes files/folders
- `git reset --hard` - Loses changes
- `gradle clean` - Deletes build outputs

## Output

### present_files
**Purpose:** Share files with user  
**When:** After creating/modifying files  
**Example:**
```
present_files(filepaths=[
    "/outputs/Feature.kt",
    "/outputs/FeatureTest.kt"
])
```

## Usage Guidelines

### Creating Files

**When to use:**
- Code is >50 lines
- User explicitly asks for file
- Creating complete feature
- User says "save", "create", "make a file"

**Where to save:**
- Working directory: `/home/claude/`
- Final output: `/mnt/user-data/outputs/`

### Editing Files

**Always:**
- Read file first with `view`
- Understand context
- Preserve existing functionality
- Test changes mentally

### Running Commands

**Before running:**
- Check if command is safe
- Explain what it does
- Ask confirmation for destructive ops
- Provide undo instructions

## Safety Rules

### NEVER run without warning:
- `rm -rf *`
- `git reset --hard`
- Commands that delete data
- Commands that overwrite files

### ALWAYS verify:
- File paths exist
- Commands are correct
- User understands impact

### ALWAYS provide:
- Explanation of command
- Expected outcome
- Rollback steps

---

**Remember:** Tools are powerful - use responsibly!

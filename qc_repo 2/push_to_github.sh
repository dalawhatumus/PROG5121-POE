#!/usr/bin/env bash
#
# QuickChat Part 1 - push to GitHub
# --------------------------------
# This folder is already a git repository with 11 commits on the `main` branch.
# You just need to point it at your own empty GitHub repo and push.
#
# STEP 1: Create a new EMPTY repo on GitHub (no README, no .gitignore, no licence)
#         e.g. https://github.com/new  ->  name it: quickchat  (or PROG5121-POE)
#
# STEP 2: Copy the repo URL GitHub shows you. It looks like one of:
#           https://github.com/YOUR-USERNAME/quickchat.git
#           git@github.com:YOUR-USERNAME/quickchat.git
#
# STEP 3: Run this script with that URL, from inside this folder:
#           bash push_to_github.sh https://github.com/YOUR-USERNAME/quickchat.git
#
# That's it. All 11 commits will appear on GitHub, and the GitHub Actions
# workflow (.github/workflows/TestJava.yml) will run your tests automatically.

set -e

REMOTE_URL="$1"

if [ -z "$REMOTE_URL" ]; then
  echo "ERROR: please pass your GitHub repo URL."
  echo "Usage: bash push_to_github.sh https://github.com/YOUR-USERNAME/quickchat.git"
  exit 1
fi

# Set your real name/email for the commits going forward (optional but tidy).
# git config user.name  "Your Name"
# git config user.email "your@email.com"

# Point at your repo (replaces any existing 'origin').
git remote remove origin 2>/dev/null || true
git remote add origin "$REMOTE_URL"

# Push main and set it as the tracking branch.
git branch -M main
git push -u origin main

echo ""
echo "Done. Open your repo on GitHub - you should see 11 commits and an Actions run starting."

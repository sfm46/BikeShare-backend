Set-Location -Path "C:\Users\Admin\Music\BikeShare\backend"
if (-not (Test-Path ".git")) {
    git init
}
git config user.name "Shaik Saif"
git config user.email "shaik.saif7g@gmail.com"
git add .
# Commit might fail if there are no changes, but we try anyway
git commit -m "Fix PostgreSQL configuration and prepare Render deployment"
git branch -M main

$remotes = git remote
if ($remotes -contains "origin") {
    git remote set-url origin https://github.com/sfm46/BikeShare-backend.git
} else {
    git remote add origin https://github.com/sfm46/BikeShare-backend.git
}

git push -u origin main

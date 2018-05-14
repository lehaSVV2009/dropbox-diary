docker login -u _ -p "$HEROKU_TOKEN" registry.heroku.com
docker build -t registry.heroku.com/dropbox-diary-api/web -f dropbox-diary-api/Dockerfile dropbox-diary-api/.
docker push registry.heroku.com/dropbox-diary-api/web
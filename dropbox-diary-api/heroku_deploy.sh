echo "$HEROKU_PASSWORD" | docker login -u "$HEROKU_USERNAME" --password-stdin registry.heroku.com
docker build -t lehasvv2009/dropbox-diary-api .
docker tag registry.heroku.com/dropbox-diary-api/web
docker push registry.heroku.com/dropbox-diary-api/web
docker-compose -f docker-compose.main.yml down --remove-orphans
docker-compose -f docker-compose.app.yml down --remove-orphans
docker-compose -f docker-compose.env.yml down
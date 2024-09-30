docker-compose -f docker-compose.env.yml up -d
docker-compose -f docker-compose.app.yml up -d
# seata依赖nacos启动
until curl -s http://localhost:8848/nacos/ > /dev/null; do
  echo "wait for nacos start..."
  sleep 3
done
docker-compose -f docker-compose.main.yml up -d
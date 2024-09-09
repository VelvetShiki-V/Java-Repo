# 检查8080端口是否被占用
pid=$(sudo lsof -t -i:7777)

# 判断是否有进程占用该端口
if [ -z "$pid" ]; then
  echo "7777端口未被占用"
else
  echo "检测到7777端口被进程 $pid 占用，正在结束该进程..."
  sudo kill -9 $pid
  
  if [ $? -eq 0 ]; then
    echo "进程 $pid 已成功结束"
  else
    echo "无法结束进程 $pid，请检查权限或其他问题"
  fi
fi

# 执行jar包
mvn clean package
java -jar myemc_gms_main/target/myemc_gms_main-0.0.1-SNAPSHOT.jar

# -pl指定运行模块，-am自动构建依赖模块，通过spring-boot:run插件直接运行项目
# 必须在mvn clean install，在本地安装依赖后，方可执行
# mvn -pl myemc_gms_main spring-boot:run

# 编译打包，不运行测试
# mvn clean package -DskipTests

# 调试模式运行
# mvn -pl myemc_gms_main -am spring-boot:run -Dspring-boot.run.fork=false -Dmaven.surefire.debug

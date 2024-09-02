#!/bin/bash

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

# 执行maven
mvn clean compile
mvn -pl myemc_gms_main spring-boot:run
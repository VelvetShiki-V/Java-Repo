# gms项目

配置文件格式
- 配置在gms_main主项目中
```yaml
spring:
  # 服务名称
  application:
    name: myemc_gms

  # mysql连接
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://www.velvetshiki.cn:3306/gms_db?rewriteBatchedStatements=true&characterEncoding=UTF-8&useUnicode=true&characterSetResults=UTF-8
    username: root
    password: zcx984288627

  # 多实例场景下，如果多个 RedissonClient 实例使用相同的连接池配置，可能会导致连接池资源的竞争或其他并发问题。
  # 根据需求需要配置redis最大连接数max connection或调整启动项目的连接池数量，避免资源竞争
  # redis连接
  data:
    redis:
      host: www.velvetshiki.cn
      port: 6379
      password: 123456
      database: 1
      lettuce:
        # 配置redis连接池，分别为最大用户连接数，最大空闲连接数，最小空闲连接数，最大连接等待时间
        pool:
          max-active: 10
          max-idle: 5
          min-idle: 0
          max-wait: 100ms

# redisson配置(configurationProperties注入，需要lombok配合使用)
redisson:
  address: redis://1.94.180.58:6379
  password: 123456
  connectionPoolSize: 10          #  最大连接数
  connectionMinimumIdleSize: 10

# Tomcat相关配置
server:
  # 0端口自动使用未被占用
  port: 6666
#  port: ${gms.server.port}
  address: 0

# mybatis-plus
mybatis-plus:
  type-aliases-package: com.vs.pojo
  mapper-locations: "classpath*:/mapper/**/*.xml"

# global constants
global-dynamic-constants:
  redis_cache_max_ttl_minutes: 30
  redisson_lock_wait_max_seconds: 5
  jwt_expire_duration_seconds: 1800000
  thread_pool_max_size: 10

```

示例数据
```json
[
    {
        "Name": "Cap-1048-A",
        "Id": "TCDT15JX5518",
        "Owner": "shiki",
        "Ts": "2023-09-14T15:25:30+08:00",
        "Labels": ["Admin", "Manager", "Leader", "Member"],
        "Properties": [
            {
                "K": "C",
                "V": "0.47uF"
            },
            {
                "K": "Qualified",
                "V": true
            },
            {
                "K": "Manufacturer",
                "V": "TDK"
            }
        ]
    },
    {
        "Name": "Res-1055-T",
        "Id": "TWET14JW4126",
        "Owner": "velvet",
        "Ts": "2023-09-14T15:28:24+08:00",
        "Labels": ["Guest"],
        "Properties": [
            {
                "K": "R",
                "V": "50Ohm"
            },
            {
                "K": "Qualified",
                "V": true
            },
            {
                "K": "Manufacturer",
                "V": "K.K.Y"
            }
        ]
    },
    {
        "Name": "Conductor Store",
        "Id": "TBET11JR3091",
        "Owner": "Docter H",
        "Ts": "2023-09-12T17:13:50+08:00",
        "Labels": ["Boss"],
        "Properties": [
            {
                "K": "Qualified",
                "V": true
            },
            {
                "K": "Level",
                "V": "H3"
            }
        ],
        "Children": [
            {
                "Id": "TCDT15JX5518"
            },
            {
                "Id": "TWET14JW4126"
            }
        ]
    },
    {
        "Name": "Company X",
        "Id": "TPET10JX1005",
        "Owner": "songf",
        "Ts": "2023-09-11T12:18:33+08:00",
        "Labels": ["Admin", "Auditor"],
        "Properties": [
            {
                "K": "Industry",
                "V": "Electronics and Electrical"
            },
            {
                "K": "Country",
                "V": "China"
            },
            {
                "K": "Number of Employees",
                "V": 1200
            }
        ],
        "Children": [
            {
                "Id": "TBET11JR3091"
            }
        ]
    },
    {
        "Name": "Company X",
        "Id": "TPET10JX1005",
        "Owner": "songf",
        "Ts": "2023-09-11T12:18:33+08:00",
        "Labels": ["Admin", "Auditor"],
        "Properties": [
            {
                "K": "Industry",
                "V": "Electronics and Electrical"
            },
            {
                "K": "Country",
                "V": "China"
            },
            {
                "K": "Number of Employees",
                "V": 1200
            }
        ],
        "Children": [
            {
                "Id": "TBET11JR3091"
            }
        ]
    }
]
```
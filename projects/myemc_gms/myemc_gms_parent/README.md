# gms项目

配置文件格式
- 配置在gms_main主项目中
```yaml
spring:
  application:
    name: myemc_gms

  # mysql连接
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url:
    username:
    password:

  # redis连接
  data:
    redis:
      host:
      port:
      password:
      database:
      lettuce:
        # 配置redis连接池，分别为最大用户连接数，最大空闲连接数，最小空闲连接数，最大连接等待时间
        pool:
          max-active:
          max-idle:
          min-idle:
          max-wait:

server:
  # 0端口自动使用未被占用
  port:
  address:

# mybatis-plus
mybatis-plus:
  type-aliases-package:
  mapper-locations:
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
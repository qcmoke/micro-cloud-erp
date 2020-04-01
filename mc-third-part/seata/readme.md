1、下载https://github.com/seata/seata/releases/download/v1.0.0/seata-server-1.0.0.tar.gz得到seata服务端安装包。

2、解压并修改配置conf/file.conf和conf/registry.conf
file.conf的配置如下：
```shell script
(1)修改service节点的vgroup_mapping.my_test_tx_group为vgroup_mapping.mc_tx_group
service {
  ...
  vgroup_mapping.mc_tx_group = "default"
  ...
}

(2)修改store的db节点配置数据库
  db {
   url = "jdbc:mysql://39.106.195.202:3306/mc-seata"
   user = "root"
   password = "qcmoke)[good](boy"
  }
```

registry.conf的配置：
```shell script
(1)设置registry的type为nacos,并配置nacos服务端的ip:port
registry {
  type = "nacos"

  nacos {
    serverAddr = "39.106.195.202:8848"
    namespace = ""
    cluster = "default"
  }

  ...
  ...
}
```

3、在mysql中创建一个名称为`mc-seat`的全局事务数据库，并导入`db_store.sql`。

4、将配置好的file.conf和registry.conf复制到需要seata的业务模块的resources资源文件目录里。

5、在所有分支事务相关的数据库里导入`db_undo_log.sql`。

6、进入bin目录并执行如下命令启动服务端
```shell script
$ sh seata-server.sh

#如果希望本地开发的服务能从nacos找到seata服务端，那么需要选择seata服务端的公网ip将服务注册到nacos，那么可执行如下命令，其中ip为seata-server所在服务器的公网ip
sh seata-server.sh  -h 39.106.195.202
```


7、如果使用docker-compose安装，可如下操作：

操作以上1到5步，然后安装下面的步骤操作

（1）将配置好的`file.conf`和`registry.conf`上传到`/micro-cloud-erp/seata/config/`。

（2）然后在docker-compose.yml（mc-third-part/docker-compose.yml）所在的目录执行命令`docker-compose up -d seata`启动。




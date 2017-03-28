Slf4j2elk

ELK(elasticsearch、logstash、kibana)

下载以下程序并解压运行

- [elasticsearch-5.2.2](https://denghb.com/dl/elasticsearch-5.2.2.zip)
```
# 解压
unzip elasticsearch-5.2.2.zip
# 运行
cd elasticsearch-5.2.2/bin
nohup ./elasticsearch &
# PS：不能以root用户运行
```

- [logstash-5.2.2](https://denghb.com/dl/logstash-5.2.2.zip)
```
# 解压
unzip logstash-5.2.2.zip
cd logstash-5.2.2/bin/

# 创建配置文件并写入以下配置
vim logstash.conf 
input {
  http {
    host => "127.0.0.1"
    port => 31311
  }
}

filter {
  mutate {
     remove_field => ["headers"]
  }
}

output {
  elasticsearch { hosts => ["localhost:9200"] }
  stdout { codec => rubydebug }
}
# 启动
nohup ./logstash -f logstash.conf &
```

- [kibana-5.2.2](https://denghb.com/dl/kibana-5.2.2-darwin-x86_64.tar.gz)
```
# 解压
tar -zxvf kibana-5.2.2-darwin-x86_64.tar.gz
# 启动
cd kibana-5.2.2-darwin-x86_64/bin
nohup ./kibana &
```



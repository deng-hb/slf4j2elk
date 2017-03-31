#Slf4j2elk

demo版暂不能在生产中使用

基于`slf4j-simple`源码修改

Maven中使用在pom.xml
```
<dependency>
  <groupId>com.denghb</groupId>
  <artifactId>slf4j2elk</artifactId>
  <version>1.0.1</version>
</dependency>
```


项目classpath创建`slf4j2elk.properties`并写入一下内容
```
# 日志级别
com.denghb.slf4j2elk.level=DEBUG
# (可选)应用ID
com.denghb.slf4j2elk.id=10001
# (可选)ELK logstash 日志input http地址
com.denghb.slf4j2elk.server=http://localhost:31311
# (可选)本地写入日志文件目录
com.denghb.slf4j2elk.file=/tmp/slf4j2elk
```


ELK(elasticsearch、logstash、kibana) 服务安装运行

下载以下程序并解压运行
官网下载[https://www.elastic.co/downloads](https://www.elastic.co/downloads)打开很慢

- [elasticsearch-5.2.2](https://denghb.com/dl/elasticsearch-5.2.2.zip)
```
# 解压
unzip elasticsearch-5.2.2.zip
# 启动并后台运行
cd elasticsearch-5.2.2/bin
nohup ./elasticsearch &
# PS：不能以root用户运行
```
查看服务状态[Elasticsearch](http://localhost:9200) http://localhost:9200

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
# 启动并后台运行
nohup ./logstash -f logstash.conf &

# 查看日志
tail -100f nohup.out
```
测试发送日志
```
curl -H "Content-Type: application/json" -XPUT 'http://127.0.0.1:31311' -d '{"appId":"10001","level":"ERROR","content":"hello world"}'
```

- [kibana-5.2.2](https://denghb.com/dl/kibana-5.2.2-darwin-x86_64.tar.gz)
```
# 解压
tar -zxvf kibana-5.2.2-darwin-x86_64.tar.gz
# 启动并后台运行
cd kibana-5.2.2-darwin-x86_64/bin
nohup ./kibana &
```
查看日志 [Kibana](http://localhost:5601) http://localhost:5601

[![](https://raw.githubusercontent.com/deng-hb/slf4j2elk/master/Kibana-1.png)]()
[![](https://raw.githubusercontent.com/deng-hb/slf4j2elk/master/Kibana-2.png)]()






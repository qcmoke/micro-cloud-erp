#服务使用的版本是latest，如果使用0.9.0的版本，则可以使用这个Dockerfile安装
FROM java:8
MAINTAINER qcmoke "qcmoke@gmail.com"

LABEL version=0.9.0 \
      desc="seata:0.9.0" \
      author="qcmoke"

ENV SEATA_USER="seata" \
    TIME_ZONE="Asia/Shanghai"

ARG SEATA_VERSION=0.9.0

WORKDIR /seata

RUN set -x \
    && wget  https://github.com/seata/seata/releases/download/v${SEATA_VERSION}/seata-server-${SEATA_VERSION}.tar.gz -P /seata \
    && tar -xzvf seata-server-${SEATA_VERSION}.tar.gz \
    && mv seata/* ./ \
    && rm -rf seata* \
    && ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo '$TIME_ZONE' > /etc/timezone

# 设置额外参数
ENV EXTRA_JVM_ARGUMENTS="-XX:MaxDirectMemorySize=1024M"

ENTRYPOINT ["sh","/seata/bin/seata-server.sh"]
EXPOSE 8091
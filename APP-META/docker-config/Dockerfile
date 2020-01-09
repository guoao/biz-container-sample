FROM centos:7
MAINTAINER 景焕 <guoao.ga@alibaba-inc.com>
# 安装打包必备软件
RUN yum install -y wget unzip telnet lsof net-tools bind-utils tree
ENV APP_NAME=biz-container-sample
ENV CATALINA_HOME=/home/admin/taobao-tomcat
# 设置 EDAS-Container/Pandora 应用容器版本
ENV EDAS_CONTAINER_VERSION V3.5.3
LABEL pandora V3.5.3
# 下载安装 OpenJDK
RUN yum -y install java-1.8.0-openjdk-devel
# 根据环境变量，下载安装 EDAS Contaienr/Pandora 应用容器版本
RUN mkdir -p ${CATALINA_HOME}/deploy/
RUN wget http://edas-hz.oss-cn-hangzhou.aliyuncs.com/edas-plugins/edas.sar.${EDAS_CONTAINER_VERSION}/taobao-hsf.tgz -O /tmp/taobao-hsf.tgz && \
    tar -xvf /tmp/taobao-hsf.tgz -C ${CATALINA_HOME}/deploy/ && \
    rm -rf /tmp/taobao-hsf.tgz

# 将fatjar解压
COPY container-sample-start/target/biz-container-sample.jar /home/admin/${APP_NAME}/target/biz-container-sample.jar
RUN unzip /home/admin/${APP_NAME}/target/biz-container-sample.jar  -d /home/admin/${APP_NAME}/target/${APP_NAME}

# 将应用启动脚本和配置文件复制到镜像中
COPY APP-META/docker-config/environment/bin/ /home/admin/${APP_NAME}/bin
# 设置文件操作权限
RUN chmod -R a+x /home/admin/${APP_NAME}/bin/

# just for debug
#RUN tree /home/admin

# 挂载数据卷,指定目录挂载到宿主机上面,为了能够保存（持久化）数据以及共享容器间的数据，为了实现数据共享，例如日志文件共享到宿主机或容器间共享数据.
VOLUME /home/admin/$APP_NAME/logs \
       /home/admin/logs \

# 启动应用
WORKDIR /home/admin/$APP_NAME/bin
#CMD ["/bin/bash"]
ENTRYPOINT ["/home/admin/biz-container-sample/bin/start.sh"]
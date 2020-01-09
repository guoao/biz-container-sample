#!/bin/bash

# SETENV_SETTED promise run this only once.
# eg.
# -Djava.security.egd=file:/dev/./urandom
# -Dcatalina.logs=$CATALINA_HOME/logs
# -Dpandora.location=$CATALINA_HOME/deploy/taobao-hsf.sar
# -Dhalo.container.enable=true  "/home/admin/$APP_NAME/xxx.jar"
# --server.context-path=/
# --server.port=8080
# --server.tomcat.uri-encoding=ISO-8859-1
# --server.tomcat.max-threads=400

if [ -z $SETENV_SETTED ]; then
    SETENV_SETTED="true"
    # app
    # set ${APP_NAME}, if empty $(basename "${APP_HOME}") will be used.
    APP_HOME=$(cd $(dirname ${BASH_SOURCE[0]})/..; pwd)
    if [[ "${APP_NAME}" = "" ]]; then
        APP_NAME=$(basename "${APP_HOME}")
    fi

    export JAVA_HOME=/usr/lib/jvm/java-openjdk
    export PATH=${PATH}:${JAVA_HOME}/bin
    ulimit -c unlimited

    echo "INFO: OS max open files: "`ulimit -n`

    #set container
    export HALO_CONTAINER_ENABLE=true

    #move plugin from lib to app
    ARTIFACT="extension-sample2"
    VERSION="1.0-SNAPSHOT"
    if [ -f ${APP_HOME}/target/${APP_NAME}/BOOT-INF/lib/${ARTIFACT}-${VERSION}.jar ]; then
        mkdir -p ${APP_HOME}/target/${APP_NAME}/BOOT-INF/app/${ARTIFACT}
        echo "move ${APP_HOME}/target/${APP_NAME}/BOOT-INF/lib/${ARTIFACT}-${VERSION}.jar to ${APP_HOME}/target/${APP_NAME}/BOOT-INF/app/${ARTIFACT}/${ARTIFACT}-${VERSION}.jar"
        mv ${APP_HOME}/target/${APP_NAME}/BOOT-INF/lib/${ARTIFACT}-${VERSION}.jar ${APP_HOME}/target/${APP_NAME}/BOOT-INF/app/${ARTIFACT}/${ARTIFACT}-${VERSION}.jar
    fi

    # when stop pandora boot process, will try to stop old tomcat process
    export CATALINA_HOME=/home/admin/taobao-tomcat
    export CATALINA_BASE=$APP_HOME/.default
    export CATALINA_PID=$CATALINA_BASE/catalina.pid
    export PANDORA_LOCATION=$CATALINA_HOME/deploy/taobao-hsf.sar

    TOMCAT_PORT=8080

    if [[ ! -f ${APP_HOME}/target/${APP_NAME}/bin/appctl.sh ]]; then
        # env for service(pandora boot)
        export JAVA_FILE_ENCODING=UTF-8
        export URI_ENCODING=ISO-8859-1

        export SECURITY_EGD=file:/dev/./urandom
        export CONTEXT_PATH=/
        export TOMCAT_MAX_THREADS=400

        mkdir -p "$APP_HOME"/.default
        export SERVICE_PID=$APP_HOME/.default/${APP_NAME}.pid
        export SERVICE_OUT=$APP_HOME/logs/service_stdout.log

#        SERVICE_OPTS="${SERVICE_OPTS} -DJM.LOG.PATH=${MIDDLEWARE_LOGS}"
#        SERVICE_OPTS="${SERVICE_OPTS} -DJM.SNAPSHOT.PATH=${MIDDLEWARE_SNAPSHOTS}"

        if [ -z "$SERVICE_TMPDIR" ] ; then
            # Define the java.io.tmpdir to use for Service(pandora boot)
            SERVICE_TMPDIR="${APP_HOME}"/.default/temp
            mkdir -p "${APP_HOME}"/.default/temp
        fi

        SERVICE_OPTS="${SERVICE_OPTS} -server"

        SERVICE_OPTS="${SERVICE_OPTS} -Xms512m -Xmx512m"
        SERVICE_OPTS="${SERVICE_OPTS} -Xmn512m"

        SERVICE_OPTS="${SERVICE_OPTS} -Dfile.encoding=${JAVA_FILE_ENCODING}"
        SERVICE_OPTS="${SERVICE_OPTS} -Dproject.name=${APP_NAME}"
        SERVICE_OPTS="${SERVICE_OPTS} -Dserver.port=${TOMCAT_PORT}"

        SERVICE_OPTS="${SERVICE_OPTS} -Dcatalina.logs=${CATALINA_HOME}/logs"

        SERVICE_OPTS="${SERVICE_OPTS} -Djava.security.egd=${SECURITY_EGD}"

        SERVICE_OPTS="${SERVICE_OPTS} -Dpandora.location=$CATALINA_HOME/deploy/taobao-hsf.sar"
        SERVICE_OPTS="${SERVICE_OPTS} -Dhalo.container.enable=${HALO_CONTAINER_ENABLE}"

    else
        # compatible with the existing jar application
        export LANG=zh_CN.UTF-8
    fi
fi
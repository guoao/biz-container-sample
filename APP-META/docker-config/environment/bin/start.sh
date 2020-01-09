#!/bin/bash
APP_HOME=$(cd $(dirname ${BASH_SOURCE[0]})/..; pwd)
source "${APP_HOME}/bin/setenv.sh"


# delete old SERVICE_OUT, keep last 20 logs
ls -t "$SERVICE_OUT".* 2>/dev/null | tail -n +$((20 + 1)) | xargs --no-run-if-empty rm -f
if [ -e "$SERVICE_OUT" ]; then
    mv "$SERVICE_OUT" "$SERVICE_OUT.$(date '+%Y%m%d%H%M%S')" || exit1
fi
mkdir -p "$(dirname "${SERVICE_OUT}")" || exit1
touch "$SERVICE_OUT" || exit1

echo "INFO: Pandora boot service log: $SERVICE_OUT"

echo "\"$JAVA_HOME/bin/java\"" $CATALINA_OPTS $SERVICE_OPTS \
                 -classpath "\"${APP_HOME}/target/${APP_NAME}\"" \
                 -Dapp.location="\"${APP_HOME}/target/${APP_NAME}\"" \
                 -Djava.io.tmpdir="\"$SERVICE_TMPDIR\"" \
                 com.taobao.pandora.boot.loader.SarLauncher "$@"

eval exec "\"$JAVA_HOME/bin/java\"" $CATALINA_OPTS $SERVICE_OPTS \
            -classpath "\"${APP_HOME}/target/${APP_NAME}\"" \
            -Dapp.location="\"${APP_HOME}/target/${APP_NAME}\"" \
            -Djava.io.tmpdir="\"$SERVICE_TMPDIR\"" \
            com.taobao.pandora.boot.loader.SarLauncher "$@" \
#            >> "$SERVICE_OUT" 2>&1 "&"

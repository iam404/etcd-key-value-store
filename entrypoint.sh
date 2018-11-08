#!/usr/bin/env sh
env

if [ -z "$SERVE_PORT" ]
then
      echo "NO SERVE_PORT variable set. Exiting ..."
      exit 100;
fi

if [ -z "$ETCD_URL" ]
then
      echo "NO ETCD_URL variable set. Exiting ..."
      exit 100;
fi

if [ -z "$ETCD_PORT" ]
then
      echo "NO ETCD_PORT variable set. Exiting ..."
      exit 100;
fi

nc -vz $ETCD_URL $ETCD_PORT &> /dev/null
if [ $? -ne 0 ]
then

 # Loop 10 times still etcd service ip UP
 n=1
 while [ $n -le 10 ]
 do
   nc -vz $ETCD_URL $ETCD_PORT &> /dev/null
   if [ $? -eq 0 ]
   then
    break;
   fi
   echo "[ALERT] ETCD Service not up at http://$ETCD_URL:$ETCD_PORT"
   echo "sleeping 10s and retrying ..."

   if [ $n -eq 10 ]
   then
     echo "[ERROR] ETCD Service failed to start. Exiting."
     exit 100;
   fi

   sleep 10
   n=$(( n+1 ))

 done
fi


echo "Starting Application ...."
exec java -Dserver.port=$SERVE_PORT -Dzalando.etcd.location="http://$ETCD_URL:$ETCD_PORT" -jar /app.jar

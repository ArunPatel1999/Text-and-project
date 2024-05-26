clusterId=${KAFKA_CLUSTER_ID:-$(kafka-storage.sh random-uuid)}
echo "Kafka Cluster ID : ${clusterId}"

echo "Formatting storage"
kafka-storage.sh format -t $clusterId -c /kafka/config/kraft/server.properties

echo "Starting Kafka"
exec kafka-server-start.sh /kafka/config/kraft/server.properties

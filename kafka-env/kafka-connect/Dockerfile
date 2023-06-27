FROM confluentinc/cp-kafka-connect

ENV CONNECT_PLAGIN_PATH: "usr/share/java,/usr/share/confluent-hub-components"

RUN confluent-hub install --no-prompt debezium/debezium-connector-postgresql:2.2.1

package forever.backend.kafka.partitioner

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster

class IntKeyPartitioner : Partitioner {
    override fun configure(configs: MutableMap<String, *>?) {}

    override fun close() {}

    override fun partition(
        topic: String,
        key: Any,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster
    ): Int {
        // metadata가 즉시 즉시 반영이 안되어 오류가 발생할 수 있다.
        // metadata.max.age.ms(기본값: 3000초, 5분)를 변경 해야 한다. 그래도 타이밍 이슈로 메세지 전송 실패 발생

        val numPartitions = cluster.partitionCountForTopic(topic) ?: 1
        return (key as String).toInt() % numPartitions
    }
}

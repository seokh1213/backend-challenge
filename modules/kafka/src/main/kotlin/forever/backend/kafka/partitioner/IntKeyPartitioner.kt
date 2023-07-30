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
        val numPartitions = cluster.partitionCountForTopic(topic) ?: 1
        return (key as String).toInt() % numPartitions
    }
}

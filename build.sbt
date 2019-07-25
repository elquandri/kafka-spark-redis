import com.typesafe.config.{ConfigFactory, Config}

lazy val appProperties = settingKey[Config]("The application properties")
lazy val fileproperties = ConfigFactory.parseFile(new File("src/main/resources/application.conf"))
lazy val properties = ConfigFactory.load(fileproperties)
lazy val sparkVersion = properties.getString("spark.spark-version")
lazy val sparkKafkaVersion = properties.getString("spark.spark-sql-kafka-version")
lazy val kafkaVersion = properties.getString("kafka.kafka-client")
lazy val sparkStreamingkafkaVersion = properties.getString("spark.spark-streaming-kafka_2.10")
lazy val appName = properties.getString("scala.name")
lazy val appVersion = properties.getString("scala.version")
lazy val appscalaVersion = properties.getString("scala.scalaVersion")
lazy val sparkRedisVersion = properties.getString("spark.spark-redis-connector")
lazy val redisVersion = properties.getString("redis.redis-client-version")



name := appName



version := appVersion

scalaVersion := appscalaVersion

libraryDependencies ++= Seq(
  "org.apache.spark"%"spark-streaming-kafka_2.10"% sparkStreamingkafkaVersion,
  "org.apache.spark"%%"spark-streaming"% sparkVersion %"provided",
  "org.apache.spark"%%"spark-core"% sparkVersion %"provided",
  "org.apache.spark"%%"spark-sql"% sparkVersion %"provided",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkKafkaVersion % "provided",
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % sparkKafkaVersion)

libraryDependencies += "com.redislabs" % "spark-redis" % sparkRedisVersion
libraryDependencies += "redis.clients" % "jedis" % redisVersion

libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.1"
libraryDependencies += "net.virtual-void" %%  "json-lenses" % "0.6.2"

test in assembly := {}



assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "log4j.properties"                                  => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}

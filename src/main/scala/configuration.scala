import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.SparkSession


trait configuration {


  val config:Config=ConfigFactory.load("KafkaRedis.conf")
  val kafkaProd =config.getString("kafkaProd")
  val kafkaCon =config.getString("kafkaCon")
  val topic=config.getString("topic")
  val appName=config.getString("appName")
  val master =config.getString("master")
  val server =config.getString("server")
  val pathResult =config.getString("pathResult")
  val resultPrefix = config.getString("resultPrefix")
  val pathHdfs = config.getString("pathHdfs")
  val redisHost = config.getString("redisHost")
  val redisPortC = config.getString("redisPortC")
  val redisPort = config.getString("redisPort")
  val redisHostC = config.getString("redisHostC")


  // sparkConsumer configuration
  val spark = SparkSession.builder
    .appName(appName).master(master)
    .config(redisHostC,redisHost)
    .config(redisPortC,redisPort)
    .getOrCreate()

  // configuration du topic
  val topics = List(topic)


}

import java.io.BufferedOutputStream

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode}
import resultcheck.generate

object kafkaToRedis extends App with configuration {

  val dfKafka = createDfKafka()
  dfKafka.show()
  dfKafka.count()
  initifile()
  writeRedis(dfKafka)
  checkresult(dfKafka)


  def createDfKafka(): DataFrame ={

    import spark.implicits._

    val df: DataFrame = spark
      .read
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaCon)
      .option("subscribe", topic)
      .load()

    val data: Dataset[(String, String)] = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]
    return data.toDF()

  }

  def writeRedis(df: DataFrame){
    df.write
      .format("org.apache.spark.sql.redis")
      .option("table", "kafka")
      .option("key.column", "key")
      .mode(SaveMode.Overwrite)
      .save()
  }

  def checkresult(df : DataFrame): Unit = {

    var c=""

    val dfRead = spark.read
      .format("org.apache.spark.sql.redis")
      .option("table", "kafka")
      .option("key.column", "key")
      .load()
    dfRead.show()

    val diff = dfRead.except(df)

    if (diff.count() ==0)
    {
      c = generate("passed").toString
    }
    else if(diff.count() !=0) {
      c = generate("failed").toString
    }
    else {
      c = generate("bloqued").toString
    }

    saveResultFile(c)

  }

  def initifile(): Unit =
  {
    val c =generate("failed").toString
    saveResultFile(c)
  }

  def saveResultFile(content: String) = {
    val conf = new Configuration()
    conf.set("fs.defaultFS", pathHdfs)

    val date = java.time.LocalDate.now.toString
    val filePath = pathResult + resultPrefix + "_" + date + ".json"

    val fs = FileSystem.get(conf)
    val output = fs.create(new Path(filePath), true)

    val writer = new BufferedOutputStream(output)

    try {
      writer.write(content.getBytes("UTF-8"))
    }
    finally {
      writer.close()
    }
  }
  spark.stop()

}

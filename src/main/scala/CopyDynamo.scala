import org.apache.spark.sql.SparkSession
import org.apache.hadoop.io.Text
import org.apache.hadoop.dynamodb.DynamoDBItemWritable
import org.apache.hadoop.dynamodb.read.DynamoDBInputFormat
import org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat
import org.apache.hadoop.mapred.JobConf

object CopyDynamo {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("CopyDynamo").getOrCreate()
    var sc = spark.sparkContext
    var jobConf = new JobConf(sc.hadoopConfiguration)
    jobConf.set("dynamodb.input.tableName", "ContactV2-preprodstage")
    jobConf.set("dynamodb.throughput.read.percent", "0.5")
    jobConf.set("mapred.input.format.class", "org.apache.hadoop.dynamodb.read.DynamoDBInputFormat")
    jobConf.set("mapred.output.format.class", "org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat")
    jobConf.set("dynamodb.endpoint","http://dynamodb.us-east-1.amazonaws.com/");
    jobConf.set("dynamodb.regionid","us-east-1");
    var jobConf2 = new JobConf(sc.hadoopConfiguration)
    jobConf2.set("dynamodb.output.tableName", "Contacts-temp-stage")
    jobConf2.set("dynamodb.throughput.write.percent", "0.5")
    jobConf2.set("mapred.input.format.class", "org.apache.hadoop.dynamodb.read.DynamoDBInputFormat")
    jobConf2.set("mapred.output.format.class", "org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat")
    var contacts = sc.hadoopRDD(jobConf, classOf[DynamoDBInputFormat], classOf[Text], classOf[DynamoDBItemWritable])
    // Add Filter/Maps here to filter/transform data before saving.
    // contacts.map(x => ???) or contacts.filter(x => ???) 
    contacts.saveAsHadoopDataset(jobConf2)
    spark.stop()
  }
}

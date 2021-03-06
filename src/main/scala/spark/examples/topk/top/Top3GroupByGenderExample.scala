package spark.examples.topk.top

import org.apache.spark.{SparkConf, SparkContext}

object Top3GroupByGenderExample {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Top3GroupByGenderExample").setMaster("local[*]")

    val BASE_PATH = "src/main/resources/data/examples"
    val sc = new SparkContext(conf)

    val data = sc.textFile(BASE_PATH + "/groupAndsort.txt")

    val pairs = data.map(_.split(","))
      .map(document => (document(2), (document(0), document(1), document(2), document(3).toInt)))
        .groupByKey()

    // 元组自定义排序
    implicit  val sorting = new Ordering[(String, String, String, Int)] {
      override def compare(x: (String, String,String,  Int), y: (String, String, String, Int)): Int = {
        -x._4.compareTo(y._4)
      }
    }

    val result = pairs.mapValues(x => x.toList.sorted(sorting).take(3))

    result.foreach(println)

    sc.stop()
  }
}

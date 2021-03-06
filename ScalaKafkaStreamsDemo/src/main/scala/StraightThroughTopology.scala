import java.time.Duration

import org.apache.kafka.streams.Topology
import java.util.Properties

import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.KafkaStreams
import utils.Settings


class StraightThroughTopology extends App {

  import Serdes._

  val props: Properties = Settings.createBasicStreamProperties(
    "straight-through-application", "localhost:9092")

  run()

  private def run(): Unit = {
    val topology = createTopolgy()
    val streams: KafkaStreams = new KafkaStreams(topology, props)
    streams.start()
    sys.ShutdownHookThread {
      streams.close(Duration.ofSeconds(10))
    }
  }

  def createTopolgy(): Topology = {

    val builder: StreamsBuilder = new StreamsBuilder
    val textLines: KStream[String, String] =
      builder.stream[String, String]("InputTopic")
    textLines.mapValues(v => v).to("OutputTopic")
    builder.build()
  }
}

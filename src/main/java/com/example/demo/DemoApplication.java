package com.example.demo;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
// Copy of https://github.com/spring-cloud/spring-cloud-stream-samples/blob/96d63c732b8d0a080999061b6c125b70a8e415a5/kafka-streams-samples/kafka-streams-message-channel/src/main/java/kafka/streams/message/channel/KafkaStreamsWordCountApplication.java#L36
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public static class WordCountProcessorApplication {

		@Bean
		public Function<KStream<Object, String>, KStream<?, WordCount>> process() {

			return input -> input
					.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
					.map((key, value) -> new KeyValue<>(value, value))
					.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
					.windowedBy(TimeWindows.of(Duration.ofSeconds(60)))
					.count(Materialized.as("WordCounts-1"))
					.toStream()
					.map((key, value) -> new KeyValue<>(null,
							new WordCount(key.key(), value, new Date(key.window().start()), new Date(key.window().end()))));
		}

		@Bean
		public Consumer<String> sink() {
			return s -> System.out.println("FOOBAR -- " + s);
		}
	}

	static class WordCount {

		private String word;

		private long count;

		private Date start;

		private Date end;

		WordCount(String word, long count, Date start, Date end) {
			this.word = word;
			this.count = count;
			this.start = start;
			this.end = end;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

		public Date getStart() {
			return start;
		}

		public void setStart(Date start) {
			this.start = start;
		}

		public Date getEnd() {
			return end;
		}

		public void setEnd(Date end) {
			this.end = end;
		}
	}
}
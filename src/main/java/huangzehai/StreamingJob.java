/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package huangzehai;

import huangzehai.model.VehicleEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;

import java.time.LocalDateTime;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(10000);


        /*
         * Here, you can start creating your execution plan for Flink.
         *
         * Start with getting some data from the environment, like
         * 	env.readTextFile(textPath);
         *
         * then, transform the resulting DataStream<String> using operations
         * like
         * 	.filter()
         * 	.flatMap()
         * 	.join()
         * 	.coGroup()
         *
         * and many more.
         * Have a look at the programming guide for the Java API:
         *
         * http://flink.apache.org/docs/latest/apis/streaming/index.html
         *
         */
        DataStreamSource<String> lines = env.socketTextStream("localhost", 9000);

        lines.filter(StringUtils::isNotBlank)
                .map(line -> {
                    VehicleEvent vehicleEvent = new VehicleEvent();
                    String[] items = line.split(",");
                    vehicleEvent.setId(Integer.valueOf(StringUtils.trim(items[0])));
                    vehicleEvent.setVin(StringUtils.trim(items[1]));
                    vehicleEvent.setDateTime(LocalDateTime.parse(StringUtils.trim(items[2])));
                    vehicleEvent.setEvent(StringUtils.trim(items[3]));
                    vehicleEvent.setAlerts(StringUtils.trim(items[4]));
                    return vehicleEvent;
                }).filter(event -> event.getEvent().equalsIgnoreCase("alert"))
                .addSink(new PrintSinkFunction<>());
        // execute program
        env.execute("Flink Streaming Java API Skeleton");
    }
}

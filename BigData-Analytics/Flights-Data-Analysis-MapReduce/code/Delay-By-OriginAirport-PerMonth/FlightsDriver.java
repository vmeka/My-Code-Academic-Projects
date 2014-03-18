package hw1airportmonth;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class FlightsDriver extends MapReduceBase {

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		JobClient client = new JobClient();
		JobConf conf = new JobConf(hw1airportmonth.FlightsDriver.class);

		conf.setJobName("Delay Percentage of Flights by Origin Airport per Month");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(hw1airportmonth.FlightsMapper.class);
		conf.setReducerClass(hw1airportmonth.FlightsReducer.class);
		
		//conf.setNumReduceTasks(8);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.nanoTime();

		System.out.println("The Time taken is : " + (endTime - startTime)/1.0e9 + "seconds");
	}

}

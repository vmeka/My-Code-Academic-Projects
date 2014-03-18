package hw1airport;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class FlightsMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	private final static IntWritable noDelay_flight = new IntWritable(1);
	private final static IntWritable delay_flight = new IntWritable(0);
	private Text origin = new Text();
	
	@Override
	public void map(LongWritable _key, Text value,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
	 
		String[] rows = (value.toString()).split(",");
		String originID = rows[16];
		String departureDelay = rows[15];
		
		origin.set(originID);
		
		if((departureDelay.equals("NA") || Integer.parseInt(departureDelay) > 0)){
			output.collect(origin, delay_flight);
		}
		else{
			output.collect(origin, noDelay_flight);
		}
	}

}

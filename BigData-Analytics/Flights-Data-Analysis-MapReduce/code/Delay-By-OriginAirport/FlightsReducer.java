package hw1airport;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class FlightsReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {


	@Override
	public void reduce(Text _key, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
					throws IOException {
		Text key = _key;
		
		int finalNonDelay = 0;
		int finalDelay = 0;
		int delayPercentage = 0;
		
		while(values.hasNext()){
			IntWritable value = (IntWritable)values.next();
			if(value.get() == 0){
				finalDelay += 1;
			}
			else{
				finalNonDelay+= value.get();
			}
			delayPercentage = (finalDelay*100)/(finalDelay + finalNonDelay);
		}
		output.collect(key, new IntWritable(delayPercentage));
	}
}

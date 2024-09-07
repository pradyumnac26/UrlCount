import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class UrlCount {

  public static class UrlMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text url = new Text();
      
    private static final Pattern URLPattern = Pattern.compile("href=\"[^\"]*\"");

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      Matcher matcher = URLPattern.matcher(value.toString());
      while (matcher.find()) {
        String urlStr = matcher.group();
        urlStr = urlStr.substring(6, urlStr.length() - 1);
        url.set(urlStr);
        context.write(url, one);
      }
    }
  }

  public static class UrlReducer extends Reducer<Text, IntWritable, Text, IntWritable> { 
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> { 
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      if (sum > 5) {
        context.write(key, result);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "URL count");
    job.setJarByClass(UrlCount.class);
    job.setMapperClass(UrlMapper.class);
    //job.setCombinerClass(UrlReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}

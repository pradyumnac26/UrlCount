# URLCount Summary 

## How it Works

UrlCount is designed using the Hadoop's mapReduce framework to count the number of URLs from text data in articles/websites and then filters out the URLs that occur fewer than 5 times. We do this by slightly modifying the WordCount project where it counts the number of words present in the document or article. The basic idea is we find the links/URLS through href using regex regular expressions.

Basically it has 3 phases : 

- Mapper phase

  The Matcher processes each line of input to find all matching URLs using the Pattern class that matches href=\"[^\"]*\" for the href attribute and then updates the count of each URL to 1.
    
  

- Combiner phase

    A combiner can be used to optimize performance by reducing the amount of data transferred between the mapper and the reducer.

- Reducer phase

    The Reducer takes the input from the mapper/combiner and aggregates the count of URLs and filters URLs that occur less than 5 times.
  

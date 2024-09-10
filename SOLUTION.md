# URLCount Summary 

## How it Works

UrlCount is designed using the Hadoop's mapReduce framework to count the number of URLs from text data in articles/websites. We do this by slightly modifying the WordCount project where it counts the number of words present in the document or article. The basic idea is we find the links/URLS through href using regex regular expressions.

Basically it has 3 phases : 
- Mapper phase

- Combiner phase

- Reducer phase

Scrabble Word Suggester
=======================

Design
------
The code base is split into 4 modules:
`core` - contains code shared by both the scrabble-indexer and scrabble-suggester jars. This module contains the bulk of the implementation.
`indexer` - contains the main executable class for invoking scrabble-indexer
`suggester` - contains the main executable class for invoking scrabble-suggester
`benchmarker` - contains JMH benchmark code for testing the performance of the scrabble-suggester code.


I approached the problem by first of all considering how to create the on-disk index. The following is what I came up with:
1. Read all lines from the word list file, as specified by the command line argument
2. Calculate the score of each word and then build its suffix array
3. Build up suffix strings in the following format: `<suffix>+<original_word>+<score>`
4. Sort the list of output by suffix
5. Write the list to `./output/INDEX.txt`

To implement the suggester, I did the following:
1. Read all lines from the index file located at `./output/INDEX.txt` into memory
2. Determine the list of candidate top-scoring words by finding all suffix strings that start with the query string `Q`
3. From the candidate list, find the `K` top-scoring words and print them to System.out


Building and Running the Code
-----------------------------

You will need to have installed:
- Java 8 JDK
- Maven

To build all the executable jars, in the main project directory, run `mvn package`
This will produce the following executable jars:

`indexer/target/scrabble-indexer.jar` which you can execute with the following command:
`java -jar indexer/target/scrabble-indexer.jar <word_list_file>`

`suggester/target/scrabble-suggester.jar` which you can execute with the following command:
`java -jar suggester/target/scrabble-suggester.jar <query_string> <num_matching_words>`

`benchmarker/target/scrabble-benchmarker.jar` which you can execute with the following command:
`java -jar benchmarker/target/scrabble-benchmarker.jar` and will run the benchmarks as detailed in the next section.


Performance Analysis
--------------------

### Method

I decided to use [JMH](http://openjdk.java.net/projects/code-tools/jmh/) to benchmark the `scrabble-suggester` implementation as 
correctly and robustly writing benchmarks for small Java applications is difficult to do correctly. The way I have implemented 
the JMH benchmark is as follows:

1. Benchmark four different aspects of the application:
   - The loading of the index file into memory 
   - The searching of the in-memory index for the given query string and number of matches
   - The end-to-end running time, from loading the index from file to searching for the query string
   - The time it takes to write the suggested words to the console output
   
2. The structure of the benchmark is:
Some state setup (in the `IndexState` class which is used by the search-only benchmark, this state setup is excluded from the benchmarking, 
so we can test the in-memory searching in isolation.

The three separate benchmarks are all run in `SingleShotTime` mode because this mode most accurately reflects the cold-start of the JVM 
which is incurred every time the suggester is invoked.

Additionally, I wanted to test the performance of scrabble-suggester with different input parameters, so I chose the following two profiles:

- A query string of 'E' with number of matches '106758'. This represents the largest number of suffix strings in the index, but is obviously an unrealistic use case given that a someone 
  probably doesn't really want 106,758 results. This is to stress-test the code and get results for a worst case scenario.
- A query string of 'AT' with number of matches '30'. This represents what is probably closer to an average search scenario.


### Results

Note that all these benchmarks were run on a 2.6 GHz Intel Core i7 Macbook Pro with 16GB of RAM, running OS X Yosemite.

Below is the output of the benchmark run:

|Benchmark                                  | Mode | Cnt |  Score  |   Error  | Units |
|-------------------------------------------|------|-----|---------|----------|-------| 
|SuggesterBenchmark.testEndToEnd_Large      |   ss |  10 | 243.814 |±  42.611 | ms/op |
|SuggesterBenchmark.testEndToEnd_Regular    |   ss |  10 | 226.615 |±  78.511 | ms/op |
|SuggesterBenchmark.testPrintOutput_Large   |   ss |  10 | 933.571 |± 236.150 | ms/op |
|SuggesterBenchmark.testPrintOutput_Regular |   ss |  10 |   3.090 |±   0.483 | ms/op |
|SuggesterBenchmark.testReadIndexFromFile   |   ss |  10 | 148.574 |±  21.562 | ms/op |
|SuggesterBenchmark.testSearchIndex_Large   |   ss |  10 |  65.404 |±  17.630 | ms/op |
|SuggesterBenchmark.testSearchIndex_Regular |   ss |  10 |  83.899 |±  11.544 | ms/op |


The time to load the index file into memory is around 150ms, and the time to perform the index searching is around 70ms. 
The end to end run, which include   s reading the index into memory and then performing the search is around 250ms.
What's interesting is that the variance between different input parameters isn't large except in the case of printing the output to the console, a difference of around 900ms which is significant,
but given that a user of the scrabble-suggester is unlikely to want to retrieve thousands of results, then performance of normal usage parameters is acceptable.



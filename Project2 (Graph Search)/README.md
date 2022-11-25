CS2103 2022 B-Term -- Project 2 -- Graph Search

Prof. Jacob Whitehill

Introduction
============

In this project you will parse files from the Internet Movie Database (IMDB) dataset into a graph, and also create a "search engine" to find **shortest paths** between any pair of nodes in any graph (which could be IMDB data but could also be any other graph). Using this infrastructure that you build, the user can find shortest paths between any two actors (see the "Actor 1" and "Actor 2" in the lower-right of the figure below, where the user is prompted to enter the actors for which a shortest path should be found).

![Interaction.png](/courses/39079/files/5102594/preview)

Parsing the IMDB data
=====================

One of the tedious but crucial aspects of real-world computer programming is converting data from one format to another. It's important to become efficient and reliable at doing this, and this project provides some practice (though much has been done for you). In particular, you will create a class IMDBGraphImpl that implements the IMDBGraph interface. Implementing this class will require you to parse data files that contain information on actors and the movies in which they starred.

Data format
-----------

IMDB consists of multiple data files, but in this project you only need to concern yourself with two of them: name.basics.tsv.gz and title.basics.tsv.gz. The "tsv" stands for "tab-separated-values" and means that different columns in the same row (see below) are separated by tab ("\\t" in Java) characters. The "gz" stands for "GZip", which is a kind of data compression algorithm (this is handy because, uncompressed, the data files would be huge).

To give a sense of what each of these files contains, see the example data below that contain some of the _people_ in IMDB:

...
nm0000355	Anthony Daniels	1946	\\N	actor,producer,writer	tt0080684,tt0076759,tt2488496,tt2527336
nm0000426	Jennifer Grey	1960	\\N	actress,soundtrack	tt2013293,tt0092890,tt0087985,tt0091042
nm0000595	Victoria Principal	1950	\\N	actress,producer,executive	tt0075393,tt0077000,tt0068853,tt0071455
nm0001962	Lizzie Borden	1958	\\N	director,writer,producer	tt0102340,tt0085267,tt0092238,tt0144642
nm0002825	Robert Morris	\\N	\\N	production\_designer	tt0185400,tt0146516,tt0119330,tt0222375
...

The first column contains an anonymous identifier of the person, and the second column contains the person's full name. The fifth column (we'll ignore the other ones) contains the set of _roles_ the person has taken -- this could be actor (or actress), writer, producer, etc. The last column in each row contains a list of movie identifiers (separated by commas). For instance, Anthony Daniels was an actor, and he worked on the movies with movie IDs tt0080684, tt0076759, tt2488496, and tt2527336. (Note: it's _not_ listed in this file whether he was an _actor_ in each of these movies; it's simply reported that he _worked_ on them in some role. He may have been a producer or writer for some of them.)

Let's next look at a sample of the movie title data:

...
tt0065202	movie	Weite Strassen stille Liebe	Weite Strassen stille Liebe	0	1969	\\N	76	Comedy,Drama,Romance
tt1777639	movie	Y el río sigue corriendo	Y el río sigue corriendo	0	2010	\\N	70	Documentary
tt2950408	movie	Kôshoku onsen geisha: Hitô nozoki	Kôshoku onsen geisha: Hitô nozoki	0	1995	\\N	60	\\N
tt2609684	movie	Shinkon nureppa nashi	Shinkon nureppa nashi	0	1991	\\N	60	\\N
tt5654044	movie	Fuck Off 2 - Images from Finland	Perkele 2: Kuvia Suomesta vuonna 2016	0	2017	\\N	89	Documentary
...

Similar to the actors file, the movies file is organized into columns, and the first column contains movie identifiers -- these are the same identifiers referenced in the name.basics.tsv.gz file. The second column indicates what kind of production it is ("movie", "tvSeries", "tvEpisode", etc.). The third column contains the title of the movie.

Goal: A Graph of IMDB Actors and Movies
---------------------------------------

After parsing the IMDB data files, you should end up with a graph whose nodes/vertices are the actors & actresses as well as the movies, and whose edges represent who acted in which movies. Below is an example (note that the actors and titles shown in the image below might not match exactly the IMDB data):  
![IMDBGraph.png](/courses/39079/files/5102606/preview)  
Here, Tom Cruise acted in _Top Gun: Maverick_, and so did Jennifer Connelly. (He also acted in some other movies, but they are not shown.) Jennifer Connelly also acted in _Requiem for a Dream_ (which is sad but worth watching), and so did Marlon Wayans. The nodes in the graph will be represented in your code as IMDBNode objects, and the edges between them are represented using the collection of \_neighbors in each node.

Keeping the memory usage tractable
----------------------------------

IMDB is huge. To keep things tractable, you should parse only those names that are marked as being either an "actor" or "actress". Moreover, you should parse only those titles that are marked as being a "movie". (This has been done for you already in the starter code.) Note that it is possible that you will parse (and store into your IMDB graph) a person who is an actor (of any gender), but who has only acted in _non_\-movie productions (TV series, etc.). In that case, your graphs should _still_ contain a node for the actor, even though they will not be connected to any neighbors in the graph.

Developing on a data subset
---------------------------

It's far preferable to develop and debug your code on a smaller subset of the huge IMDB dataset. To facilitate this, we have provided some _uncompressed_ files containing just a few thousand lines each -- see someActors.tsv and someMovies.tsv on Canvas. Being uncompressed, they can be opened using any standard text editor. You can also pass the names of these files to the constructor of your IMDBGraphImpl class and check that your parsing code is working correctly.

Finding shortest paths between nodes in a graph
===============================================

To find a shortest path between node s and t in a graph, you should implement a **breadth-first search (BFS)**, as described during class. Once you have found a shortest path -- if one exists -- you then need to **backtrack** from t back to s and record the sequence of nodes that were traversed. The result should then be returned back to the caller. Even for large social network graphs consisting of millions of nodes, BFS will be very fast, as its performance scales linearly (rather than quadratically or something worse) in the number of nodes in the graph. For this part of the assignment, you should create a class called GraphSearchEngineImpl that implements the GraphSearchEngine interface. The findShortestPath method of your GraphSearchEngineImpl should return an instance of type List<Node> in which the **first** element of the list is s, the **last** element of the list is t, and the **intermediate** nodes constitute a shortest path (alternating between movies and actresses/actors) that connect nodes s and t. If no shortest path exists between the pair of nodes, then your method should return null. If a path is requested between a node and itself, then return a list containing just the one node.

**Make sure that your GraphSearchEngineImpl is not "tied" to the IMDB data in any way** -- the search engine should be useful for **any** graph of Node objects. In particular, I will run _your_ GraphSearchEngineImpl against _my_ IMDBGraphImpl, and it should still work perfectly!

Requirements
============

1.  **R1** (12 points): Finish implementing of the processActors method within the IMDBGraphImpl classs.:
2.  **R2** (18 points): Implement BFS within a class GraphSearchEngineImpl that implements GraphSearchEngine. Your search engine should be able to find shortest paths between **any** pair of Node objects (or return null if no such path exists).
3.  **R3** (10 points): Write unit tests (at least 5 good ones) of GraphSearchEngineImpl. Put them in GraphTester.java.

Teamwork
========

You may work as a team on this project; the maximum team size is 2.

Design and Style
================

Your code must adhere to reasonable Java style. In particular, please adhere to the following guidelines:

*   Class names should be in CamelCase; variables should be in mixedCase.
*   Avoid "magic numbers" in your code (e.g., for (int i = 0; i < 999 /\*magic number\*/; i++)). Instead, use **constants**, e.g., private static final int NUM\_ELEPHANTS\_IN\_THE\_ROOM = 999;, defined at the top of your class file.
*   Use whitespace consistently.
*   No method should exceed 50 lines of code (for a "reasonable" maximum line length, e.g., 100 characters). If your method is larger than that, it's probably a sign it should be decomposed into a few helper methods.
*   Use comments to explain non-trivial aspects of code.
*   Use the **most restrictive** access modifiers (e.g., private, default, protected\>, public), for both variables and methods, that you can. Note that this does not mean you can never use non-private access; it just means you should have a good reason for doing so.
*   Declare variables using the **weakest type** (e.g., an interface rather than a specific class implementation) you can; ithen instantiate new objects according to the actual class you need. This will help to ensure **maximum flexibility** of your code. For example, instead of  
    ArrayList<String> list = new ArrayList();  
    use  
    List<String> list = new ArrayList<String>();  
    If, on the other hand, you have a good reason for using the actual type of the object you instantiate (e.g., you need to access specific methods of ArrayList that are not part of the List interface), then it's fine to declare the variable with a stronger type.

Getting started
===============

Download the Project2.zip starter code from Canvas.  
Download the IMDB data files [actors Links to an external site.](https://datasets.imdbws.com/name.basics.tsv.gz) and [title Links to an external site.](https://datasets.imdbws.com/title.basics.tsv.gz). Then, set the IMDB\_DIRECTORY variable in IMDBGraphImpl.java to point to the directory containing these files. These files are what you can ultimately load and search through and answer some interesting queries. For development and debugging purposes, however, you should download and use someActors.tsv and someMovies.tsv (available on Canvas), as they are much easier to deal with -- just change the arguments passed to the IMDBGraphImpl constructor accordingly.

**Note**: the GraphSearchGUI class is the motivating application of this project, but you are not required to use it or extend it in any way. It's mainly just for fun. If you want to run it, you will need to have the JavaFX library installed as well.

What to Submit
==============

Create a Zip file containing IMDBGraphImpl.java, GraphSearchEngineImpl.java, and GraphSearchEngineTester.java, along with any .tsv files that facilitate your tests, and any other classes that your code requires. Submit the Zip file you created to Canvas.
